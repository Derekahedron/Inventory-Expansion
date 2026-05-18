package derekahedron.invexp.mixin.client;

import derekahedron.invexp.containeritem.UsableContents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ModifyVariableMayUseName")
@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {

    /**
     * Uses the selected stack for the main hand when getting the main hand render type.
     */
    @ModifyVariable(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At("STORE"),
            ordinal = 0)
    private ItemStack getRenderTypeForMainHand(
            ItemStack itemstack,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            LivingEntity livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        if (livingEntity instanceof Player player
                && UsableContents.selectedStackOf(itemstack, player) == player.getUseItem()) {
            return player.getUseItem();
        }
        return itemstack;
    }

    /**
     * Uses the selected stack for the offhand when getting the offhand render type.
     */
    @ModifyVariable(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At("STORE"),
            ordinal = 1)
    private ItemStack getRenderTypeForOffHand(
            ItemStack itemstack1,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            LivingEntity livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        if (livingEntity instanceof Player player
                && UsableContents.selectedStackOf(itemstack1, player) == player.getUseItem()) {
            return player.getUseItem();
        }
        return itemstack1;
    }
}
