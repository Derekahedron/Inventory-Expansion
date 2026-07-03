package derekahedron.invexp.mixin;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.containeritem.ContainerItemContents;
import derekahedron.invexp.item.ItemStackDuck;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackDuck {

    @Shadow
    public abstract int getCount();

    @Shadow
    public abstract ItemStack copyWithCount(int count);

    @Unique
    @Nullable
    private ContainerItemContentsWriter invexp$containerItemContents;

    @Unique
    @Nullable
    private ContainerItemContents invexp$cachedContents;

    @Override
    public void invexp$setContainerItemContents(@Nullable ContainerItemContentsWriter contents) {
        this.invexp$containerItemContents = contents;
    }

    @Override
    public void invexp$setCachedContents(@Nullable ContainerItemContents cachedContents) {
        this.invexp$cachedContents = cachedContents;
    }

    @Override
    @Nullable
    public ContainerItemContents invexp$getCachedContents() {
        return this.invexp$cachedContents;
    }

    /**
     * Decreases the count inside the container item that a projectile is being fired from.
     */
    @Inject(
            method = "setCount",
            at = @At("HEAD"))
    private void setCountOfContents(int count, @Nullable CallbackInfo ci) {
        if (invexp$containerItemContents == null) return;

        int countDiff = count - getCount();

        if (countDiff < 0) {
            invexp$containerItemContents.remove(copyWithCount(-countDiff));
        } else if (countDiff > 0) {
            InventoryExpansion.LOGGER.warn(
                    "Container ItemStack count increased unexpectedly! Potential loss of items");
        }
    }

    @Inject(
            method = "copy",
            at = @At("RETURN"))
    private void copyCachedContents(CallbackInfoReturnable<ItemStack> cir) {
        if (invexp$cachedContents == null) return;

        ItemStack stack = cir.getReturnValue();
        if (stack != null && !stack.isEmpty()) {
            ((ItemStackDuck) (Object) stack).invexp$setCachedContents(invexp$cachedContents);
        }
    }
}
