package derekahedron.invexp.item.sack;

import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;

public interface SackContentsReader extends ContainerItemContentsReader {

    /**
     * Gets all sack types in a sack.
     *
     * @return a list of all sack types in the sack contents
     */
    List<String> getSackTypes();

    @Override
    default boolean isFull() {
        return (getTotalWeight().compareTo(getMaxWeight()) >= 0)|| (getStacks().size() >= getMaxStacks());
    }

    @Override
    default boolean canTryInsert(ItemStack stack) {
        if (!stack.getItem().canFitInsideContainerItems()) return false;

        String sackType = getSackType(stack);
        if (sackType == null) return false;

        return canAddType() || isInTypes(sackType);
    }

    /**
     * Checks if a given sack type is represented in the current types.
     *
     * @param sackType the sack type to check for
     * @return <code>true</code> if the sack type is already in this sack; <code>false</code> otherwise
     */
    default boolean isInTypes(@Nullable String sackType) {
        if (sackType == null) return false;

        for (String nestedType : getSackTypes()) {
            if (sackType.equals(nestedType)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a new sack type can be added.
     *
     * @return <code>true</code> if a new sack type can be added; <code>false</code> otherwise
     */
    default boolean canAddType() {
        return getSackTypes().size() < getMaxSackTypes();
    }

    @Override
    default boolean canAddStack() {
        return getStacks().size() < getMaxStacks();
    }

    /**
     * Gets max stacks of the holding sack stack.
     *
     * @return the max number of distinct stacks the sack can hold
     */
    default int getMaxStacks() {
        if (getContainerStack().getItem() instanceof SackItem sackItem) {
            return sackItem.getMaxStacks(getContainerStack());
        } else {
            return 0;
        }
    }

    /**
     * Gets max sack types than be used in this sack.
     *
     * @return the max number of stack types the sack can hold
     */
    default int getMaxSackTypes() {
        if (getContainerStack().getItem() instanceof SackItem sackItem) {
            return sackItem.getMaxSackTypes(getContainerStack());
        } else {
            return 0;
        }
    }

    @Override
    default Fraction getMaxWeight() {
        if (getContainerStack().getItem() instanceof SackItem sackItem) {
            return sackItem.getMaxWeight(getContainerStack());
        } else {
            return Fraction.ZERO;
        }
    }

    @Nullable
    default String getSackType(ItemStack stack) {
        if (getContainerStack().getItem() instanceof SackItem sackItem) {
            return sackItem.getSackType(getContainerStack(), stack);
        } else {
            return null;
        }
    }

    @Override
    default Fraction getWeight(ItemStack stack) {
        if (getContainerStack().getItem() instanceof SackItem sackItem) {
            return sackItem.getWeight(getContainerStack(), stack);
        } else {
            return Fraction.ONE;
        }
    }
}
