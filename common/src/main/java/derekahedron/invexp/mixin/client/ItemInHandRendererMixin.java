package derekahedron.invexp.mixin.client;

import derekahedron.invexp.containeritem.UsableContents;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ModifyVariableMayUseName")
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    /**
     * Uses the selected stack for the main hand when getting the hand render type.
     */
    @ModifyVariable(
            method = "evaluateWhichHandsToRender",
            at = @At("STORE"),
            ordinal = 0)
    private static ItemStack getRenderTypeForMainHand(ItemStack itemstack, LocalPlayer player) {
        return UsableContents.selectedStackOf(itemstack, player);
    }

    /**
     * Uses the selected stack for the offhand when getting the hand render type.
     */
    @ModifyVariable(
            method = "evaluateWhichHandsToRender",
            at = @At("STORE"),
            ordinal = 1)
    private static ItemStack getRenderTypeForOffHand(ItemStack itemstack1, LocalPlayer player) {
        return UsableContents.selectedStackOf(itemstack1, player);
    }

    /**
     * Checks if the selected stack is a charged crossbow to get the proper hand render type.
     */
    @ModifyArg(
            method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;isChargedCrossbow(Lnet/minecraft/world/item/ItemStack;)Z"))
    private static ItemStack isSackChargedCrossbow(ItemStack stack) {
        ContainerItemContentsReader contents = ContainerItemBehaviors.getUsableContents(stack)
                .orElse(null);

        if (contents == null || contents.isEmpty()) {
            return stack;
        }
        return contents.getSelectedStack();
    }

    /**
     * Renders the selected stack in the main hand
     */
    @ModifyVariable(
            method = "tick",
            at = @At("STORE"),
            ordinal = 0)
    private ItemStack updateMainHandSelectedStack(ItemStack itemstack) {
        return UsableContents.selectedStackOf(itemstack, minecraft.player);
    }

    /**
     * Renders the selected stack in the offhand
     */
    @ModifyVariable(
            method = "tick",
            at = @At("STORE"),
            ordinal = 1)
    private ItemStack updateOffHandSelectedStack(ItemStack itemstack1) {
        return UsableContents.selectedStackOf(itemstack1, minecraft.player);
    }
}
