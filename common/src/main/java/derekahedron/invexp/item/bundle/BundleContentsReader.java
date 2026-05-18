package derekahedron.invexp.item.bundle;

import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import derekahedron.invexp.mixin.BundleItemInvoker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.math.Fraction;

public interface BundleContentsReader extends ContainerItemContentsReader {

    @Override
    default boolean isFull() {
        return (getTotalWeight().compareTo(getMaxWeight()) >= 0)
                || (getStacks().size() >= getMaxStacks());
    }

    @Override
    default boolean canTryInsert(ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems();
    }

    @Override
    default Fraction getWeight(ItemStack stack) {
        if (getContainerStack().getItem() instanceof BetterBundleItem bundleItem) {
            return bundleItem.getWeight(getContainerStack(), stack);
        } else {
            return Fraction.getFraction(BundleItemInvoker.invexp$callGetWeight(stack), 64);
        }
    }

    @Override
    default boolean canAddStack() {
        return getStacks().size() < getMaxStacks();
    }

    /**
     * Gets max bundle stacks of the holding bundle stack.
     *
     * @return  max stacks the bundle can hold
     */
    default int getMaxStacks() {
        if (getContainerStack().is(Items.BUNDLE)) {
            return 64;
        } else if (getContainerStack().getItem() instanceof BetterBundleItem bundleItem) {
            return bundleItem.getMaxStacks(getContainerStack());
        } else {
            return 0;
        }
    }

    @Override
    default Fraction getMaxWeight() {
        if (getContainerStack().is(Items.BUNDLE)) {
            return Fraction.ONE;
        } else if (getContainerStack().getItem() instanceof BetterBundleItem bundleItem) {
            return bundleItem.getMaxWeight(getContainerStack());
        } else {
            return Fraction.ZERO;
        }
    }
}
