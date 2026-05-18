package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.UsableContents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TemptGoal.class)
public class TemptGoalMixin {

    @Shadow
    @Final
    private Ingredient items;

    /**
     * Modifies the argument here to test for the held item in the container item.
     */
    @Inject(
            method = "shouldFollow",
            at = @At(value = "RETURN"),
            cancellable = true)
    private void shouldFollowSelectedStack(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;

        if (items.test(UsableContents.selectedStackOf(entity.getMainHandItem()))
                || items.test(UsableContents.selectedStackOf(entity.getOffhandItem()))) {
            cir.setReturnValue(true);
        }
    }
}
