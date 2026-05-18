package derekahedron.invexp.client.util;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.mixin.client.GuiGraphicsAccessor;
import derekahedron.invexp.util.OpenItemTexturesRegistry;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Stores open texture location for a given item. Open textures are the "front" and "back" textures associated with
 * items that can open up, like bundles and sacks.
 *
 * @param backTexture the resource location of the back texture of the item
 * @param frontTexture the resource location of the front texture of the item
 */
@SuppressWarnings("resource")
public record OpenItemTextures(ModelResourceLocation backTexture, ModelResourceLocation frontTexture) {

    private static final HashMap<Item, OpenItemTextures> OPEN_TEXTURES = new HashMap<>();

    /**
     * Gets the stored open item textures for the given item.
     *
     * @param item the item to get the open textures for
     * @return the open textures if they exist; <code>null</code> otherwise
     */
    @Nullable
    public static OpenItemTextures getTextures(Item item) {
        return OPEN_TEXTURES.getOrDefault(item, null);
    }

    /**
     * Gets a list of all front and back model locations for all items that were registered as having open textures.
     *
     * @return a list of {@linkplain ModelResourceLocation ModelResourceLocations} to register
     */
    public static List<ModelResourceLocation> setupLocations() {
        OPEN_TEXTURES.clear();
        List<Item> items = OpenItemTexturesRegistry.getItems();
        List<ModelResourceLocation> locations = new ArrayList<>(items.size() * 2);

        for (Item item : items) {
            BuiltInRegistries.ITEM.getResourceKey(item).ifPresent(itemResource -> {
                String namespace = itemResource.location().getNamespace();
                String path = itemResource.location().getPath();
                OpenItemTextures textures = new OpenItemTextures(
                        new ModelResourceLocation(namespace, path + "_open_back", "inventory"),
                        new ModelResourceLocation(namespace, path + "_open_front", "inventory"));
                OPEN_TEXTURES.put(item, textures);
                locations.add(textures.backTexture);
                locations.add(textures.frontTexture);
            });
        }
        return locations;
    }

    /**
     * Renders an opened up item on the screen.
     *
     * @param guiGraphics renders to the screen
     * @param stack the ItemStack to be rendered
     * @param x the x position of item
     * @param y the y position of the item
     * @param level the level where the item is being rendered
     * @param entity the entity that this item is attached to
     * @param seed the random seed to render this item with
     */
    public static void renderOpenItem(
            GuiGraphics guiGraphics,
            ItemStack stack,
            int x,
            int y,
            @Nullable Level level,
            @Nullable LivingEntity entity,
            int seed) {
        ItemRenderer renderer = ((GuiGraphicsAccessor) guiGraphics).invexp$getMinecraft().getItemRenderer();
        OpenItemTextures textures = OpenItemTextures.getTextures(stack.getItem());

        if (textures == null) return;

        BakedModel backModel = resolveModelOverride(
                renderer.getItemModelShaper().getModelManager().getModel(textures.backTexture()),
                stack,
                level,
                entity,
                seed);
        BakedModel frontModel = resolveModelOverride(
                renderer.getItemModelShaper().getModelManager().getModel(textures.frontTexture()),
                stack,
                level,
                entity,
                seed);

        guiGraphics.pose().pushPose();
        try {
            guiGraphics.pose().translate(x + 8, y + 8, 150);
            guiGraphics.pose().mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
            guiGraphics.pose().scale(16.0F, 16.0F, 16.0F);

            Lighting.setupForFlatItems();

            guiGraphics.pose().pushPose();
            try {
                guiGraphics.pose().translate(0, 0, -1.0F);
                renderer.render(stack,
                        ItemDisplayContext.GUI,
                        false,
                        guiGraphics.pose(),
                        guiGraphics.bufferSource(),
                        0xF000F0,
                        OverlayTexture.NO_OVERLAY,
                        backModel
                );
            } catch (Throwable throwable) {
                InventoryExpansion.LOGGER.error("Error rendering open back model");
            }
            guiGraphics.pose().popPose();

            guiGraphics.pose().pushPose();
            try {
                guiGraphics.pose().translate(0, 0, 1.0F);
                renderer.render(stack,
                        ItemDisplayContext.GUI,
                        false,
                        guiGraphics.pose(),
                        guiGraphics.bufferSource(),
                        0xF000F0,
                        OverlayTexture.NO_OVERLAY,
                        frontModel
                );
            } catch (Throwable throwable) {
                InventoryExpansion.LOGGER.error("Error rendering open front model");
            }
            guiGraphics.pose().popPose();

            guiGraphics.flush();
            Lighting.setupFor3DItems();
        } catch (Throwable throwable) {
            InventoryExpansion.LOGGER.error("Error rendering open model");
        }
        guiGraphics.pose().popPose();
    }

    public static BakedModel resolveModelOverride(
            BakedModel model,
            ItemStack stack,
            @Nullable Level level,
            @Nullable LivingEntity entity,
            int seed) {
        ClientLevel clientLevel = level instanceof ClientLevel ? (ClientLevel) level : null;
        BakedModel bakedmodel = model.getOverrides().resolve(model, stack, clientLevel, entity, seed);
        return bakedmodel == null ? model : bakedmodel;
    }
}
