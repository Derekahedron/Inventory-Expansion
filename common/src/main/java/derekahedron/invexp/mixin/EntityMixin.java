package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.UsableContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    protected abstract Vec3 calculateViewVector(float xRot, float yRot);

    /**
     * Gets the updated hand-holding angle for selected items in a container item.
     */
    @Inject(
            method = "getHandHoldingItemAngle",
            at = @At("HEAD"),
            cancellable = true)
    public void catchAddedStacks(Item item, CallbackInfoReturnable<Vec3> cir) {
        Entity self = (Entity) (Object) this;

        if (self instanceof Player player) {
            boolean inOffhand = UsableContents.selectedStackOf(player.getOffhandItem(), player).is(item) && !player.getMainHandItem().is(item);
            HumanoidArm arm = inOffhand ? player.getMainArm().getOpposite() : player.getMainArm();
            cir.setReturnValue(calculateViewVector(0.0F, self.getYRot() + (float) (arm == HumanoidArm.RIGHT ? 80 : -80)).scale(0.5));
        }
    }
}
