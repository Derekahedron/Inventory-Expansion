package derekahedron.invexp.containeritem;

import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;

/**
 * Interface that defines functions needed for checking contents and if an item can be
 * inserted into a container item.
 */
public interface ContainerItemContentsReader extends ContainerItemContents {

    /**
     * Copies the selected stack so it can be used.
     *
     * @return a copy of the selected stack
     */
    default ItemStack copySelectedStack() {
        return copySelectedStack(null);
    }

    /**
     * Copies the selected stack. If the current stack matches the existing stack,
     * instead of copying, sets the count of the passed in stack.
     * This is so the ItemStack object can be the same instance, which is useful in cases like
     * checking for the active item stack.
     *
     * @param currentSelectedStack the selected stack to transform
     * @return a copy of the selected stack to modify
     */
    default ItemStack copySelectedStack(@Nullable ItemStack currentSelectedStack) {
        if (isEmpty()) {
            return ItemStack.EMPTY;
        }
        else {
            ItemStack selectedStack = getSelectedStack();
            if (currentSelectedStack != null && ItemStack.isSameItemSameTags(selectedStack, currentSelectedStack)) {
                currentSelectedStack.setCount(selectedStack.getCount());
                return currentSelectedStack;
            } else {
                return selectedStack.copy();
            }
        }
    }

    /**
     * Gets the total weight of the container item
     *
     * @return the total weight of the container item, representing a fraction of a stack
     */
    Fraction getTotalWeight();

    /**
     * Gets the container item stack that actually hold the contents.
     *
     * @return the container item stack with the contents component
     */
    ItemStack getContainerStack();

    /**
     * Gets if the given stack can even be tried to be inserted into the stack based on the properties of the stack.
     * Checks for things like the type of item, not things like the weight of the container item
     *
     * @param stack the stack to test
     * @return if a stack can be tried to be inserted
     */
    boolean canTryInsert(ItemStack stack);

    /**
     * Gets the maximum number of items of the given stack that can be inserted into the container item.
     * Uses things like weight to determine this, and potentially split up a stack into parts that can be inserted.
     *
     * @param stack the stack being inserted
     * @return the max number of items from the given stack that can be inserted into these contents
     */
    default int getMaxAllowed(ItemStack stack) {
        if (!canTryInsert(stack)) return 0;

        int maxAllowedByWeight = getMaxAllowedByWeight(stack);

        if (maxAllowedByWeight == 0) {
            return 0;
        } else if (canAddStack()) {
            // If we can add a stack, we can always add as much as is allowed by weight
            return maxAllowedByWeight;
        }

        // Iterate through stacks and see how many items can be added by merging
        int maxAllowed = 0;

        for (ItemStack nestedStack : getStacks()) {
            if (ItemStack.isSameItemSameTags(stack, nestedStack)) {
                maxAllowed += nestedStack.getMaxStackSize() - nestedStack.getCount();

                if (maxAllowed >= maxAllowedByWeight) {
                    return maxAllowedByWeight;
                }
            }
        }

        return maxAllowed;
    }

    /**
     * Checks if a stack can be inserted based on the number of total stacks in the container item.
     * Used to enforce a hard limit on the number of stacks for things like quivers, which can hold up to 8 mixed stacks
     * of arrows.
     *
     * @return if a new stack can be added
     */
    boolean canAddStack();

    /**
     * Checks if the contents are considered full and should be rendered as such for things like the item bar.
     *
     * @return if the contents are considered full
     */
    boolean isFull();

    /**
     * Gets a fraction for how full the contents are to be used in places like the progress bar and the item bar.
     *
     * @return a fraction representing how full the contents are
     */
    default Fraction getFillFraction() {
        if (isFull()) {
            return Fraction.ONE;
        } else {
            return getTotalWeight().divideBy(getMaxWeight());
        }
    }

    /**
     * Gets the max weight that this container item can hold.
     *
     * @return the max weight represented by a fraction of total stacks
     */
    Fraction getMaxWeight();

    /**
     * Gets the maximum count of items that be added to this container item by weight alone.
     *
     * @param stack the stack being inserted
     * @return the max number of items from the given stack that can be inserted into these contents by weight
     */
    default int getMaxAllowedByWeight(ItemStack stack) {
        if (stack.isEmpty()) return 0;

        Fraction weight = getWeight(stack);
        if (weight.compareTo(Fraction.ZERO) > 0) {
            Fraction openWeight = getMaxWeight().subtract(getTotalWeight());
            return Math.max(openWeight.divideBy(weight).intValue(), 0);
        } else {
            return stack.getCount();
        }
    }

    /**
     * Gets how much weight a given item takes up.
     *
     * @param stack the stack to get the weight of
     * @return how much weight this item takes up, represented by a fraction of a total stack
     */
    Fraction getWeight(ItemStack stack);

    /**
     * Gets how much weight given item stack takes up.
     *
     * @param stack the stack to get the weight of
     * @return how much weight this entire stack takes up, represented by a fraction of a total stack
     */
    default Fraction getWeightOfStack(ItemStack stack) {
        return getWeight(stack).multiplyBy(Fraction.getFraction(stack.getCount()));
    }

    /**
     * Computes the total weight being used by the contents.
     *
     * @return the total weight of the contents, represented by a fraction of a total stack
     */
    default Fraction computeTotalWeight() {
        Fraction totalWeight = Fraction.ZERO;
        for (ItemStack stack : getStacks()) {
            totalWeight = totalWeight.add(getWeightOfStack(stack));
        }
        return totalWeight;
    }
}
