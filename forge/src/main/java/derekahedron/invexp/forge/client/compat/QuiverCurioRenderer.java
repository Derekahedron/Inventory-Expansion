package derekahedron.invexp.forge.client.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import derekahedron.invexp.client.render.QuiverRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

/**
 * Renderer for the Quiver curio.
 */
public class QuiverCurioRenderer extends QuiverRenderer implements ICurioRenderer {

    public static final String BACK_SLOT_IDENTIFIER = "back";

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer,
            int light,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        if (!(renderLayerParent.getModel() instanceof HumanoidModel<?> parentModel)) return;

        // When playing without mods that add a back slot, we want to only allow one slot for quivers.
        // If there is no back slot, the quiver renders on the back. Otherwise, it should render on the waist as to
        // not get in the way of curios on the back.
        boolean renderOnBack = slotContext.identifier().equals(BACK_SLOT_IDENTIFIER)
                || !CuriosApi.getEntitySlots(slotContext.entity()).containsKey(BACK_SLOT_IDENTIFIER);

        render(
                stack,
                slotContext.entity(),
                parentModel,
                matrixStack,
                renderTypeBuffer,
                light,
                partialTicks,
                renderOnBack);
    }
}
