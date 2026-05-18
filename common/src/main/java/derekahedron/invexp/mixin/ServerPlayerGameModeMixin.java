package derekahedron.invexp.mixin;

import derekahedron.invexp.entity.PlayerEntityDuck;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    /**
     * Stop using the container item right before the inventory is synced to the client user.
     * This is done to prevent a race condition where the non-updated container item is sent to the user, then changes
     * and re-sent on the next update, which causes flickering.
     */
    @Inject(
            method = "useItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/InventoryMenu;sendAllDataToRemote()V"))
    private void stopUsingContainerItemBeforeSync(
            ServerPlayer player,
            Level level,
            ItemStack stack,
            InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir) {
        ((PlayerEntityDuck) player).invexp$stopUsingContainerItem();
    }
}
