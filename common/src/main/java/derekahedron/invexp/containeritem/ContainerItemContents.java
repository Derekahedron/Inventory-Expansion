package derekahedron.invexp.containeritem;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Predicate;

/**
 * The contents of a container item. Adds helpers and defines methods needed for the container item contents.
 */
public interface ContainerItemContents {

    /**
     * Gets all the stacks in the container item.
     *
     * @return a list of stacks in the container item
     */
    List<ItemStack> getStacks();

    /**
     * Gets the selected index of the container item.
     *
     * @return the selected index of the container item; <code>-1</code> if there is none
     */
    int getSelectedIndex();

    /**
     * Gets if the container item has any stacks in them.
     *
     * @return if the container item has any stacks
     */
    default boolean isEmpty() {
        return getStacks().isEmpty();
    }

    /**
     * Gets the selected stack from the container item.
     *
     * @return the selected stack of the container item; <code>ItemStack.EMPTY</code> if the contents are empty
     */
    default ItemStack getSelectedStack() {
        return getSelectedStack((stack) -> true);
    }

    /**
     * Gets the selected stack with a given predicate.
     *
     * @param predicate the predicate to search under
     * @return the closest selected stack that matches the predicate; <code>ItemStack.EMPTY</code> if there is none
     */
    default ItemStack getSelectedStack(Predicate<ItemStack> predicate) {
        int index = indexOf(predicate, getSelectedIndex());
        if (index == -1) {
            return ItemStack.EMPTY;
        } else {
            return getStacks().get(index);
        }
    }

    /**
     * Clamps the given index to fit in the contents.
     *
     * @param index the index to clamp to the contents
     * @return the clamped index; <code>-1</code> if the contents are empty
     */
    default int clampIndex(int index) {
        if (isEmpty()) {
            return -1;
        } else {
            return Mth.clamp(index, 0, getStacks().size() - 1);
        }
    }

    /**
     * Searches the contents for a given stack, starting from a given index. Can be used to get the "next" index in the
     * contents, like if a stack changes during use, we need to update the selected index to the "next" stack.
     *
     * @param stack the stack to search for in the contents
     * @param startingIndex the index to start the search at
     * @return the index that the next stack is at; <code>-1</code> if there is none
     */
    default int indexOf(ItemStack stack, int startingIndex) {
        return indexOf((nestedStack) -> ItemStack.isSameItemSameTags(stack, nestedStack), startingIndex);
    }

    /**
     * Searches the contents for a stack matching a given predicate, starting from a given index.
     * Can be used to get the "next" index in the contents, like if we want to get an arrow from the quiver and it
     * needs to match a predicate
     *
     * @param predicate the predicate to search for a stack that matches
     * @param startingIndex the index to start the search at
     * @return the index that the next stack is at; <code>-1</code> if there is none
     */
    default int indexOf(Predicate<ItemStack> predicate, int startingIndex) {
        if (isEmpty()) return -1;
        startingIndex = clampIndex(startingIndex);

        // First check stacks after starting index
        for (int i = startingIndex; i < getStacks().size(); i++) {
            if (predicate.test(getStacks().get(i))) {
                return i;
            }
        }

        // Next check stacks before starting index
        for (int i = startingIndex - 1; i >= 0; i--) {
            if (predicate.test(getStacks().get(i))) {
                return i;
            }
        }

        return -1;
    }
}
