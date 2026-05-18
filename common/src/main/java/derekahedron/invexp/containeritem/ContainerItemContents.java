package derekahedron.invexp.containeritem;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.List;

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
        if (isEmpty()) return ItemStack.EMPTY;
        return getStacks().get(clampIndex(getSelectedIndex()));
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
        if (isEmpty()) return -1;
        startingIndex = clampIndex(startingIndex);

        // First check stacks after starting index
        for (int i = startingIndex; i < getStacks().size(); i++) {
            if (ItemStack.isSameItemSameTags(stack, getStacks().get(i))) {
                return i;
            }
        }

        // Next check stacks before starting index
        for (int i = startingIndex - 1; i >= 0; i--) {
            if (ItemStack.isSameItemSameTags(stack, getStacks().get(i))) {
                return i;
            }
        }

        return -1;
    }
}
