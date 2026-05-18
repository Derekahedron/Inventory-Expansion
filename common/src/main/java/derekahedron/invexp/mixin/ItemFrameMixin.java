package derekahedron.invexp.mixin;

import derekahedron.invexp.entity.PlayerEntityDuck;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public class ItemFrameMixin {

    /**
     * Stops using the container item early when interacting with ItemFrame,
     * as to not add it to the frame.
     */
    @Inject(
            method = "interact",
            at = @At("HEAD"))
    private void stopUsingContainerItem(
            Player player,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir) {
        ((PlayerEntityDuck) player).invexp$stopUsingContainerItem();
    }
}
