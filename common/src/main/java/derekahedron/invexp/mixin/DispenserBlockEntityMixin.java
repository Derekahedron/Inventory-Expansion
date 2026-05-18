package derekahedron.invexp.mixin;

import derekahedron.invexp.block.entity.DispenserBlockEntityDuck;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;

@Mixin(DispenserBlockEntity.class)
public class DispenserBlockEntityMixin implements DispenserBlockEntityDuck {

    @Unique
    @Nullable
    ArrayList<ItemStack> invexp$usageBuffer;

    @Override
    public void invexp$setUsageBuffer(@Nullable ArrayList<ItemStack> usageBuffer) {
        this.invexp$usageBuffer = usageBuffer;
    }

    /**
     * Adds stacks to a usage buffer if one exists. After the container item is done being used, items from the
     * usage buffer are added back into the dispenser.
     */
    @Inject(
            method = "addItem",
            at = @At("HEAD"),
            cancellable = true)
    public void catchAddedStacks(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (invexp$usageBuffer != null) {
            invexp$usageBuffer.add(stack);
            cir.setReturnValue(stack.getCount());
        }
    }
}
