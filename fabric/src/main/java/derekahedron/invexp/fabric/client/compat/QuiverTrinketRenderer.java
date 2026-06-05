package derekahedron.invexp.fabric.client.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import derekahedron.invexp.client.render.QuiverRenderer;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/**
 * Renderer for the Quiver trinket.
 */
public class QuiverTrinketRenderer extends QuiverRenderer implements TrinketRenderer {

    public static final String BACK_SLOT_GROUP = "chest";
    public static final String BACK_SLOT_NAME = "back";

    @Override
    public void render(
            ItemStack stack,
            SlotReference slotReference,
            EntityModel<? extends LivingEntity> entityModel,
            PoseStack matrixStack,
            MultiBufferSource renderTypeBuffer,
            int light,
            LivingEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        if (!(entityModel instanceof HumanoidModel<?> parentModel)) return;

        // When playing without mods that add a back slot, we want to only allow one slot for quivers.
        // If there is no back slot, the quiver renders on the back. Otherwise, it should render on the waist as to
        // not get in the way of trinkets on the back.
        SlotType slotType = slotReference.inventory().getSlotType();
        boolean renderOnBack = (slotType.getName().equals(BACK_SLOT_NAME) && slotType.getGroup().equals(BACK_SLOT_GROUP))
                || !hasBackSlot(entity);

        render(
                stack,
                entity,
                parentModel,
                matrixStack,
                renderTypeBuffer,
                light,
                partialTicks,
                renderOnBack);
    }

    /**
     * Checks if the given entity has the trinket back slot.
     *
     * @param entity the entity to check for the slot
     * @return <code>true</code> if the entity has the back slot; <code>false</code> otherwise
     */
    public static boolean hasBackSlot(LivingEntity entity) {
        return Optional.ofNullable(TrinketsApi.getEntitySlots(entity).get(BACK_SLOT_GROUP))
                .map(slot -> slot.getSlots().containsKey(BACK_SLOT_NAME))
                .orElse(false);
    }
}
