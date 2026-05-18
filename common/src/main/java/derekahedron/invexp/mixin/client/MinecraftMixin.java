package derekahedron.invexp.mixin.client;

import derekahedron.invexp.entity.PlayerEntityDuck;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    /**
     * Starts player using container item before they use an item.
     */
    @Inject(
            method = "startUseItem",
            at = @At("HEAD"))
    private void beforeItemUse(CallbackInfo ci) {
        Minecraft self = (Minecraft) (Object) this;

        if (self.player != null) {
            ((PlayerEntityDuck) self.player).invexp$startUsingContainerItem();
        }
    }

    /**
     * Stops player using container item after they use an item.
     */
    @Inject(
            method = "startUseItem",
            at = @At("RETURN"))
    private void afterItemUse(CallbackInfo ci) {
        Minecraft self = (Minecraft) (Object) this;

        if (self.player != null) {
            ((PlayerEntityDuck) self.player).invexp$stopUsingContainerItem();
        }
    }
}
