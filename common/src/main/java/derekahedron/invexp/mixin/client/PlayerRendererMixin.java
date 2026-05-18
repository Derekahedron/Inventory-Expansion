package derekahedron.invexp.mixin.client;

import derekahedron.invexp.containeritem.UsableContents;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ModifyVariableMayUseName")
@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    /**
     * Uses the arm pose of the selected stack in hand instead of the default pose.
     * This is used for things like spyglasses and tridents.
     */
    @ModifyVariable(
            method = "getArmPose",
            at = @At("STORE"),
            ordinal = 0)
    private static ItemStack getSelectedStackInHand(
            ItemStack itemstack,
            AbstractClientPlayer player,
            InteractionHand hand) {
        if (player.isUsingItem() && player.getUsedItemHand() == hand) {
            return UsableContents.selectedStackOf(itemstack, player);
        }
        return itemstack;
    }
}
