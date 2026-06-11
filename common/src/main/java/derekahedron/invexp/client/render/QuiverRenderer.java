package derekahedron.invexp.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import derekahedron.invexp.client.model.InvExpModelLayers;
import derekahedron.invexp.client.model.QuiverModel;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract quiver renderer for mods like curios and trinkets. Contains all code needed to render the
 * quiver on the person dynamically.
 */
public abstract class QuiverRenderer {

    // Map containing the quiver texture location for a quiver item
    private static final Map<Item, ResourceLocation> QUIVER_TEXTURES = new ConcurrentHashMap<>();

    // Offset and rotation for the quiver when on the back
    public static final Vector3f BACK_OFFSET = new Vector3f(
            0.0F,
            5.0F,
            2.0F).div(16.0F);
    public static final Quaternionf BACK_ROTATION =
            Axis.ZP.rotationDegrees(30.0F);

    // Offset and rotation for the quiver when on the waist
    public static final Vector3f WAIST_OFFSET = new Vector3f(
            -1.0F,
            12.0F,
            2.0F).div(16.0F);
    public static final Quaternionf WAIST_ROTATION =
            Axis.ZP.rotationDegrees(85.0F).rotateLocalX((float) Math.toRadians(-1.0F));

    // Offset and rotation for arrows in the quiver
    public static final Vector3f ARROW_OFFSET = new Vector3f(
            0.0F,
            1.0F,
            1.5F).div(16.0F);
    public static final Quaternionf ARROW_ROTATION =
            Axis.XP.rotationDegrees(-90.0F);

    // A list of offset and rotations for arrows that should be rendered in the quiver
    public static final List<Tuple<Vector3f, Quaternionf>> ARROW_POSITIONS = List.of(
            new Tuple<>(
                    new Vector3f(0.0103F, -0.00223F, -0.0237F),
                    Axis.ZP.rotationDegrees(28.808F)),
            new Tuple<>(
                    new Vector3f(-0.0397F, -0.0211F, 0.0086F),
                    Axis.ZP.rotationDegrees(5.691F)),
            new Tuple<>(
                    new Vector3f(0.0423F, -0.0196F, -0.0151F),
                    Axis.ZP.rotationDegrees(43.434F)));

    public final QuiverModel model;

    public QuiverRenderer() {
        this.model = new QuiverModel(
                Minecraft.getInstance().getEntityModels().bakeLayer(InvExpModelLayers.QUIVER));
    }

    /**
     * Renders the quiver dynamically on the given model.
     *
     * @param stack the ItemStack that holds the quiver
     * @param entity the entity wearing the quiver
     * @param parentModel the parent model to render the quiver on
     * @param matrixStack the current matrices for rendering
     * @param renderTypeBuffer the render type buffer
     * @param light an <code>int</code> that describes the lighting on the quiver
     * @param partialTicks a <code>float</code> that holds how long it's been since the last tick
     * @param renderOnBack whether the quiver should render on the back or the waist
     */
    public void render(
            ItemStack stack,
            LivingEntity entity,
            HumanoidModel<?> parentModel,
            PoseStack matrixStack,
            MultiBufferSource renderTypeBuffer,
            int light,
            float partialTicks,
            boolean renderOnBack) {
        matrixStack.pushPose();

        parentModel.body.translateAndRotate(matrixStack);

        // Left-handed players have the quiver mirrored
        boolean shouldMirror = entity.getMainArm() == HumanoidArm.LEFT;
        Vector3f offset = renderOnBack ? BACK_OFFSET : WAIST_OFFSET;
        Quaternionf rotation = renderOnBack ? BACK_ROTATION : WAIST_ROTATION;

        matrixStack.translate(
                shouldMirror ? -offset.x : offset.x,
                offset.y,
                offset.z);
        matrixStack.mulPose(shouldMirror
                ? new Quaternionf(rotation.x, -rotation.y, -rotation.z, rotation.w)
                : rotation);

        model.renderToBuffer(
                matrixStack,
                renderTypeBuffer.getBuffer(model.renderType(getQuiverTexture(stack))),
                light,
                OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);

        // Render all the arrows in the quiver
        ContainerItemBehaviors.getContents(stack)
                .filter(contents -> !contents.isEmpty())
                .ifPresent(contents -> {
                    ItemStack selectedStack = contents.getSelectedStack();
                    int count = 0;

                    for (ItemStack nestedStack : contents.getStacks()) {
                        if (ItemStack.isSameItemSameTags(selectedStack, nestedStack)) {
                            count += nestedStack.getCount();
                        }
                    }

                    renderArrows(
                            entity,
                            matrixStack,
                            renderTypeBuffer,
                            light,
                            partialTicks,
                            count);
                });

        matrixStack.popPose();
    }

    /**
     * Renders the arrows in a quiver.
     *
     * @param entity the entity wearing the quiver
     * @param matrixStack the current matrices for rendering
     * @param renderTypeBuffer the render type buffer
     * @param light an <code>int</code> that describes the lighting on the quiver
     * @param partialTicks a <code>float</code> that holds how long it's been since the last tick
     * @param count how many arrows of the selected stack are in the quiver
     */
    public void renderArrows(
            LivingEntity entity,
            PoseStack matrixStack,
            MultiBufferSource renderTypeBuffer,
            int light,
            float partialTicks,
            int count) {
        AbstractArrow arrow = ((ArrowItem) Items.ARROW).createArrow(entity.level(), new ItemStack(Items.ARROW), entity);

        matrixStack.pushPose();

        matrixStack.translate(ARROW_OFFSET.x, ARROW_OFFSET.y, ARROW_OFFSET.z);
        matrixStack.mulPose(ARROW_ROTATION);

        int numArrows = Math.min(getRenderAmount(count), ARROW_POSITIONS.size());

        for (int i = 0; i < numArrows; i++) {
            matrixStack.pushPose();
            Vector3f offset = ARROW_POSITIONS.get(i).getA();
            Quaternionf rotation = ARROW_POSITIONS.get(i).getB();

            matrixStack.translate(offset.x, offset.y, offset.z);
            matrixStack.mulPose(rotation);

            Minecraft.getInstance().getEntityRenderDispatcher().render(
                    arrow,
                    0, 0, 0,
                    0,
                    partialTicks,
                    matrixStack,
                    renderTypeBuffer,
                    light);

            matrixStack.popPose();
        }

        matrixStack.popPose();
    }

    /**
     * Gets the quiver texture from the quiver item.
     *
     * @param stack the quiver item to get the texture for
     * @return a {@link ResourceLocation} containing the texture for the item
     */
    public static ResourceLocation getQuiverTexture(ItemStack stack) {
        return QUIVER_TEXTURES.computeIfAbsent(stack.getItem(),
                item -> BuiltInRegistries.ITEM.getKey(item)
                        .withPrefix("textures/models/quiver/")
                        .withSuffix(".png"));
    }

    /**
     * Calculates how many arrows should be rendered in the quiver based on the amount of arrows in the quiver.
     *
     * @param count how many arrows are in the quiver
     * @return how many arrows should be rendered in the quiver
     */
    public static int getRenderAmount(int count) {
        if (count > 16) {
            return 3;
        } else if (count > 4) {
            return 2;
        } else {
            return 1;
        }
    }
}
