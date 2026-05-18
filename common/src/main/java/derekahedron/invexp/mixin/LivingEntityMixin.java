package derekahedron.invexp.mixin;

import derekahedron.invexp.entity.PlayerEntityDuck;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow
    @Final
    protected static EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS;

    /**
     * Starts player using container item before ticking active item stack.
     */
    @Inject(
            method = "updatingUsingItem",
            at = @At("HEAD"))
    private void beforeTickActiveItemStack(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player) {
            ((PlayerEntityDuck) player).invexp$startUsingContainerItem();
        }
    }

    /**
     * Stops player using container after ticking active item stack.
     */
    @Inject(
            method = "updatingUsingItem",
            at = @At("RETURN"))
    private void afterTickActiveItemStack(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player) {
            ((PlayerEntityDuck) player).invexp$stopUsingContainerItem();
        }
    }

    /**
     * Starts player using container item before setting tracked data.
     */
    @Inject(
            method = "onSyncedDataUpdated",
            at = @At("HEAD"))
    private void beforeTrackedDataSet(EntityDataAccessor<?> key, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player
                && player.level().isClientSide
                && DATA_LIVING_ENTITY_FLAGS.equals(key)) {
            ((PlayerEntityDuck) player).invexp$startUsingContainerItem();
        }
    }

    /**
     * Stops player using container item after setting tracked data.
     */
    @Inject(
            method = "onSyncedDataUpdated",
            at = @At("RETURN"))
    private void afterTrackedDataSet(EntityDataAccessor<?> key, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player
                && player.level().isClientSide
                && DATA_LIVING_ENTITY_FLAGS.equals(key)) {
            ((PlayerEntityDuck) player).invexp$stopUsingContainerItem();
        }
    }

    /**
     * Starts player using container item before trying to use death protector.
     */
    @Inject(
            method = "checkTotemDeathProtection",
            at = @At("HEAD"))
    private void beforeUseDeathProtector(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player) {
            ((PlayerEntityDuck) player).invexp$startUsingContainerItem();
        }
    }

    /**
     * Stops player using container item after trying to use death protector.
     */
    @Inject(
            method = "checkTotemDeathProtection",
            at = @At("RETURN"))
    private void afterUseDeathProtector(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player) {
            ((PlayerEntityDuck) player).invexp$stopUsingContainerItem();
        }
    }
}
