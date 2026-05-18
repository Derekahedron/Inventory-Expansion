package derekahedron.invexp.item.quiver;

import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

/**
 * Checker for contents of a quiver.
 */
public interface QuiverContentsReader extends ContainerItemContentsReader {

    @Override
    default boolean canTryInsert(ItemStack stack) {
        return stack.is(ItemTags.ARROWS)
                && stack.getItem().canFitInsideContainerItems();
    }

    @Override
    default Fraction getWeight(ItemStack stack) {
        if (getContainerStack().getItem() instanceof QuiverItem quiverItem) {
            return quiverItem.getWeight(getContainerStack(), stack);
        } else {
            return Fraction.ONE;
        }
    }

    @Override
    default Fraction getMaxWeight() {
        if (getContainerStack().getItem() instanceof QuiverItem quiverItem) {
            return quiverItem.getMaxWeight(getContainerStack());
        } else {
            return Fraction.ZERO;
        }
    }

    @Override
    default boolean isFull() {
        return getTotalWeight().compareTo(getMaxWeight()) >= 0
                || getStacks().size() >= getMaxStacks();
    }

    default int getMaxStacks() {
        if (getContainerStack().getItem() instanceof QuiverItem quiverItem) {
            return quiverItem.getMaxStacks(getContainerStack());
        } else {
            return 0;
        }
    }

    @Override
    default boolean canAddStack() {
        return getStacks().size() < getMaxStacks();
    }
}
