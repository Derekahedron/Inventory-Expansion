package derekahedron.invexp.item.quiver;

import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

/**
 * Checker for contents of a quiver.
 */
public interface QuiverContentsReader extends ContainerItemContentsReader {

    @Override
    default boolean canTryInsert(ItemStack stack) {
        if (!stack.getItem().canFitInsideContainerItems()) return false;

        if (getContainerStack().getItem() instanceof QuiverItem quiverItem) {
            return quiverItem.canTryInsert(this, stack);
        } else {
            return false;
        }
    }

    @Override
    default Fraction getWeight(ItemStack stack) {
        if (getContainerStack().getItem() instanceof QuiverItem quiverItem) {
            return quiverItem.getWeight(this, stack);
        } else {
            return Fraction.ONE;
        }
    }

    @Override
    default Fraction getMaxWeight() {
        if (getContainerStack().getItem() instanceof QuiverItem quiverItem) {
            return quiverItem.getMaxWeight(this);
        } else {
            return Fraction.ZERO;
        }
    }

    default int getMaxStacks() {
        if (getContainerStack().getItem() instanceof QuiverItem quiverItem) {
            return quiverItem.getMaxStacks(this);
        } else {
            return 0;
        }
    }

    @Override
    default boolean isFull() {
        return getTotalWeight().compareTo(getMaxWeight()) >= 0
                || getStacks().size() >= getMaxStacks();
    }

    @Override
    default boolean canAddStack() {
        return getStacks().size() < getMaxStacks();
    }
}
