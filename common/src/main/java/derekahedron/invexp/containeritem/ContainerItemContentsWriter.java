package derekahedron.invexp.containeritem;

import derekahedron.invexp.InventoryExpansion;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Defines an abstract content wrapper for viewing and modifying contents of a container item.
 * Extended by SackContents and QuiverContents. Generally, the idea is that the Contents object
 * is a NOOP to create and can easily check contents without modifying.
 * If a modification can happen, a builder is created (which copies the items when created and applied)
 * where the modifications occur, so we are only creating the builder when necessary.
 */
public abstract class ContainerItemContentsWriter implements ContainerItemContentsReader {

    public final ItemStack containerStack;

    protected ContainerItemContentsWriter(ItemStack containerStack) {
        this.containerStack = containerStack;
    }

    @Override
    public ItemStack getContainerStack() {
        return containerStack;
    }

    /**
     * Adds the given stack to the contents.
     *
     * @param stack the stack to add
     * @return the number of items added
     */
    public int add(ItemStack stack) {
        if (getMaxAllowed(stack) > 0) {
            Builder builder = getBuilder();
            int added = builder.add(stack, 0);
            if (added > 0) {
                builder.apply();
            }
            return added;
        }
        return 0;
    }

    /**
     * Adds stack from the given slot by the given player.
     *
     * @param slot the slot to add item from
     * @param player the player adding the item
     * @return the number of items added
     */
    public int add(Slot slot, Player player) {
        return add(Stream.of(slot), player);
    }

    /**
     * Adds stream of slots to contents. Useful so we don't have to create and apply
     * a new builder for each addition.
     *
     * @param slots the slots to add item from
     * @param player the player adding the items
     * @return the total number of items added
     */
    public int add(Stream<Slot> slots, Player player) {
        // Do not create builder until needed
        Builder builder = null;
        // Default to using this as the contents checker
        ContainerItemContentsReader checker = this;
        int added = 0;

        for (Slot slot : slots.toList()) {
            ItemStack stack = slot.getItem();
            int maxAllowed = checker.getMaxAllowed(stack);

            if (maxAllowed > 0) {
                stack = slot.safeTake(stack.getCount(), maxAllowed, player);

                if (!stack.isEmpty()) {
                    if (builder == null) {
                        // When we need to add, create a new builder and
                        // set that as the checker
                        builder = getBuilder();
                        checker = builder;
                    }
                    added += builder.add(stack);
                }
            }
        }

        // Only apply if actually added
        if (added > 0) {
            builder.apply();
        }
        return added;
    }

    /**
     * Removes a given stack from the contents.
     *
     * @param stack the stack to remove
     * @return the number of items removed
     */
    public int remove(ItemStack stack) {
        if (isEmpty() || stack.isEmpty()) {
            return 0;
        }
        Builder builder = getBuilder();
        int removed = builder.remove(stack);
        if (removed == stack.getCount()) {
            builder.apply();
            return removed;
        }
        return 0;
    }

    /**
     * Pops single item from the selected stack.
     *
     * @return popped selected stack containing one item; <code>ItemStack.EMPTY</code> if there is none
     */
    public ItemStack popSelectedItem() {
        if (isEmpty()) return ItemStack.EMPTY;

        ItemStack selectedStack = getSelectedStack();

        if (selectedStack.getCount() == 1) {
            return popSelectedStack();
        }

        Builder builder = getBuilder();
        selectedStack = selectedStack.copyWithCount(1);

        if (builder.remove(selectedStack) > 0) {
            builder.apply();
            return selectedStack;
        } else {
            // Should never happen
            InventoryExpansion.LOGGER.warn("ItemStack {} not removed", selectedStack);
            return ItemStack.EMPTY;
        }
    }

    /**
     * Pops selected stack from contents.
     *
     * @return popped selected stack; <code>ItemStack.EMPTY</code> if there is none
     */
    public ItemStack popSelectedStack() {
        if (isEmpty()) return ItemStack.EMPTY;

        Builder builder = getBuilder();
        ItemStack stack = builder.popSelectedStack();

        if (!stack.isEmpty()) {
            builder.apply();
        }
        return stack;
    }

    /**
     * Pops selected stack into a slot
     *
     * @param slot the slot to pop the stack into
     * @return <code>true</code> if the stack was popped; <code>false</code> otherwise
     */
    public boolean popSelectedStack(Slot slot) {
        if (isEmpty()) return false;

        ItemStack selectedStack = getSelectedStack();
        int toRemove = selectedStack.getCount() - slot.safeInsert(selectedStack.copy()).getCount();

        if (toRemove == 0) {
            return false;
        } else if (toRemove == selectedStack.getCount()) {
            popSelectedStack();
            return true;
        }

        Builder builder = getBuilder();
        if (builder.remove(selectedStack, toRemove) > 0) {
            builder.apply();
            return true;
        } else {
            // Should never happen
            InventoryExpansion.LOGGER.warn(
                    "ItemStack {} not removed. Potential duplicate in slot {}",
                    selectedStack, slot);
            return false;
        }
    }

    /**
     * Removes and returns all stacks.
     *
     * @return the list of removed stacks
     */
    public List<ItemStack> popAllStacks() {
        if (isEmpty()) return List.of();

        Builder builder = getBuilder();
        List<ItemStack> stacks = builder.popAllStacks();
        builder.apply();
        return stacks;
    }

    /**
     * Sets selected index of contents.
     *
     * @param selectedIndex the new selected index
     */
    public void setSelectedIndex(int selectedIndex) {
        selectedIndex = clampIndex(selectedIndex);
        if (selectedIndex != getSelectedIndex()) {
            Builder builder = getBuilder();
            builder.setSelectedIndex(selectedIndex);
            builder.apply();
        }
    }

    /**
     * Sets the selected stack of the contents to the given stack. If the
     * stack cannot be added, the leftovers are sent through the given consumer
     *
     * @param selectedStack the new selected stack
     * @param leftoverStackConsumer a consumer that handles leftover stacks
     */
    public void updateSelectedStack(
            ItemStack selectedStack,
            Consumer<ItemStack> leftoverStackConsumer) {
        ItemStack leftoverStack = ItemStack.EMPTY;

        if (isEmpty()) {
            if (selectedStack.isEmpty()) {
                // Do nothing if both are empty
                return;
            }

            Builder builder = getBuilder();
            leftoverStack = selectedStack.copy();
            if (builder.add(leftoverStack, 0) > 0) {
                builder.apply();
            }
        }else {
            ItemStack oldStack = getSelectedStack();

            if (selectedStack.isEmpty()) {
                // If new stack is empty, remove old stack
                Builder builder = getBuilder();
                if (builder.remove(oldStack) != oldStack.getCount()) {
                    InventoryExpansion.LOGGER.warn("ItemStack {} not fully removed.", oldStack);
                }
                builder.apply();
            } else if (ItemStack.isSameItemSameTags(oldStack, selectedStack)) {

                // If stacks match, add/remove difference
                int countDiff = selectedStack.getCount() - oldStack.getCount();
                if (countDiff > 0) {
                    // Add stacks for positive difference
                    Builder builder = getBuilder();
                    leftoverStack = selectedStack.copyWithCount(countDiff);
                    if (builder.add(leftoverStack, 0) > 0) {
                        builder.apply();
                    }
                } else if (countDiff < 0) {
                    // Remove stacks for negative difference
                    Builder builder = getBuilder();
                    if (builder.remove(oldStack, -countDiff) != -countDiff) {
                        InventoryExpansion.LOGGER.warn(
                                "Count difference ({}) for {} not fully removed",
                                -countDiff, oldStack
                        );
                    }
                    builder.apply();
                } else {
                    // No change
                    return;
                }
            } else {
                // Stack is fully replaced
                Builder builder = getBuilder();
                leftoverStack = selectedStack.copy();
                if (builder.replaceSelectedStack(leftoverStack)) {
                    builder.apply();
                }
            }
        }

        if (!leftoverStack.isEmpty()) {
            leftoverStackConsumer.accept(leftoverStack);
        }
        copySelectedStack(selectedStack);
    }

    /**
     * Gets if the contents can be scrolled to change their selected index. Used in events.
     *
     * @return <code>true</code> if the contents can be scrolled; <code>false</code> otherwise
     */
    public boolean canScroll() {
        return true;
    }

    /**
     * Creates a new builder for modifying contents.
     *
     * @return the created builder for contents
     */
    public abstract Builder getBuilder();

    /**
     * Builder for modifying contents. Changes made do not take effect until the builder is applied.
     */
    public abstract static class Builder implements ContainerItemContentsReader {

        public final ArrayList<ItemStack> stacks;
        public int selectedIndex;
        public Fraction totalWeight;

        public Builder(List<ItemStack> stacks, int selectedIndex, Fraction totalWeight) {
            this.stacks = new ArrayList<>(stacks);
            this.selectedIndex = selectedIndex;
            this.totalWeight = totalWeight;
        }

        /**
         * Applies changes made to the original contents and container stack.
         */
        public abstract void apply();

        /**
         * Adds an item stack to the contents. First tries adding to existing stacks,
         * then inserts remainder at given index.
         *
         * @param stack the stack to add
         * @param insertAt the index to insert the new stack at
         * @return the number of items added
         */
        public int add(ItemStack stack, int insertAt) {
            // Hard check for not duplicating items
            if (getContainerStack().getCount() > 1) return 0;
            if (!canTryInsert(stack)) return 0;

            Fraction weight = getWeight(stack);
            int added = 0;
            int toAdd = Math.min(stack.getCount(), getMaxAllowedByWeight(stack));

            if (toAdd > 0) {
                for (int i = 0; i < stacks.size(); i++) {
                    ItemStack nestedStack = stacks.get(i);

                    if (ItemStack.isSameItemSameTags(stack, nestedStack)) {
                        int amount = Math.min(toAdd, nestedStack.getMaxStackSize() - nestedStack.getCount());

                        if (amount > 0) {
                            stacks.set(i, nestedStack.copyWithCount(nestedStack.getCount() + amount));
                            stack.shrink(amount);
                            toAdd -= amount;
                            added += amount;
                            totalWeight = totalWeight.add(weight.multiplyBy(Fraction.getFraction(amount)));
                        }

                        if (toAdd <= 0) {
                            // Return early if all the item was added.
                            return added;
                        }
                    }
                }

                // Add remaining to new stack
                if (canAddStack()) {
                    if (insertAt <= selectedIndex) {
                        selectedIndex++;
                    }
                    added += toAdd;
                    totalWeight = totalWeight.add(weight.multiplyBy(Fraction.getFraction(toAdd)));
                    stacks.add(insertAt, stack.split(toAdd));
                }
            }
            return added;
        }

        /**
         * Removes the given stack from the contents
         *
         * @param stack the stack to remove
         * @param toRemove how many of the given stack to remove
         * @return how many items were removed
         */
        public int remove(ItemStack stack, int toRemove) {
            if (stacks.isEmpty() || stack.isEmpty() || toRemove <= 0) return 0;

            int removed = 0;
            Fraction weight = getWeight(stack);

            // Track if the selected index was removed
            boolean removedSelected = false;
            for (int i = 0; i < stacks.size() && toRemove > 0; i++) {
                ItemStack nestedStack = stacks.get(i);
                if (ItemStack.isSameItemSameTags(stack, nestedStack)) {
                    if (toRemove >= nestedStack.getCount()) {
                        removed += nestedStack.getCount();
                        toRemove -= nestedStack.getCount();
                        totalWeight = totalWeight.subtract(weight.multiplyBy(Fraction.getFraction(nestedStack.getCount())));
                        stacks.remove(i);

                        // update selected index when removing
                        if (i < selectedIndex) {
                            selectedIndex--;
                        } else if (i == selectedIndex) {
                            removedSelected = true;
                        }
                        i--;
                    } else {
                        removed += toRemove;
                        totalWeight = totalWeight.subtract(weight.multiplyBy(Fraction.getFraction(toRemove)));
                        stacks.set(i, nestedStack.copyWithCount(nestedStack.getCount() - toRemove));
                        toRemove = 0;
                    }
                }
            }

            // Update selected index to nearest matching stack
            if (removedSelected) {
                selectedIndex = nextSelectedIndex(stack, selectedIndex);
            }

            return removed;
        }

        /**
         * Pops the selected stack from the contents.
         *
         * @return ItemStack popped from the contents; <code>ItemStack.EMPTY</code> if none
         */
        public ItemStack popSelectedStack() {
            if (isEmpty()) return ItemStack.EMPTY;

            ItemStack selectedStack = stacks.remove(clampIndex(selectedIndex)).copy();
            selectedIndex = nextSelectedIndex(selectedStack, selectedIndex);
            totalWeight = totalWeight.subtract(getWeightOfStack(selectedStack));
            return selectedStack;
        }

        /**
         * Removes all stacks from contents.
         *
         * @return the List of copies of previous contents
         */
        public List<ItemStack> popAllStacks() {
            List<ItemStack> copies = stacks.stream().map(ItemStack::copy).toList();
            stacks.clear();
            selectedIndex = -1;
            totalWeight = Fraction.ZERO;
            return copies;
        }

        /**
         * Sets the selected index.
         *
         * @param selectedIndex the new selected index
         */
        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        @Override
        public Fraction getTotalWeight() {
            return totalWeight;
        }

        @Override
        public List<ItemStack> getStacks() {
            return stacks;
        }

        @Override
        public int getSelectedIndex() {
            return selectedIndex;
        }

        /**
         * Adds given stack, inserting at the beginning.
         *
         * @param stack the stack to add
         * @return the number of items added
         */
        public int add(ItemStack stack) {
            return add(stack, 0);
        }

        /**
         * Tries to remove entire stack from contents.
         *
         * @param stack the stack to remove
         * @return the number of items removed
         */
        public int remove(ItemStack stack) {
            return remove(stack, stack.getCount());
        }

        /**
         * Tries to replace the selected stack with the given stack. Will
         * remove the selected stack then try to add the given stack in its place.
         *
         * @param stack the stack to replace selected stack
         * @return <code>true</code> if the operation completes; <code>false</code> otherwise
         */
        public boolean replaceSelectedStack(ItemStack stack) {
            return replaceSelectedStack(stack, getSelectedStack().getCount());
        }

        /**
         * Tries to replace a given amount of the selected stack with the given stack. Will
         * remove the selected stack then try to add the given stack in its place.
         *
         * @param stack the stack to replace selected stack
         * @param count the amount of the selected stack to replace
         * @return <code>true</code> if the operation completes; <code>false</code> otherwise
         */
        public boolean replaceSelectedStack(ItemStack stack, int count) {
            if (isEmpty()) return false;

            ItemStack selectedStack = getSelectedStack();
            int insertAt;

            if (count > selectedStack.getCount()) {
                // Fail if asking for more than possible
                InventoryExpansion.LOGGER.warn(
                        "Cannot remove {} from selected stack {}", count, selectedStack);
                return false;
            } else if (count < selectedStack.getCount()) {
                // If trying to remove less than selected stack, remove from start
                if (remove(selectedStack, count) != count) {
                    InventoryExpansion.LOGGER.warn(
                            "Selected Stack {} not fully replaced!", selectedStack);
                }
                // Insert above selected index
                insertAt = clampIndex(getSelectedIndex()) + 1;
            } else {
                // Insert at old selected index
                insertAt = clampIndex(getSelectedIndex());
                // Remove stack
                popSelectedStack();
            }

            ItemStack firstPick;
            ItemStack backupPick;
            if (ItemStack.isSameItem(stack, selectedStack)) {
                // If items are equal (like tool losing durability) prioritize selecting new stack
                firstPick = stack.copy();
                backupPick = selectedStack;
            } else {
                // If items are not equal (item transforms) prioritize selecting old stack
                firstPick = selectedStack;
                backupPick = stack.copy();
            }

            add(stack, insertAt);
            if (selectedIndex != -1) {
                setSelectedIndex(nextSelectedIndex(firstPick, backupPick, insertAt));
            }
            return true;
        }

        /**
         * Finds the next selected index of a matching stack. If none are found,
         * use clamped starting index.
         *
         * @param stack the stack to match with
         * @param startingIndex the index to start search from
         * @return the new index; <code>-1</code> if none
         */
        public int nextSelectedIndex(ItemStack stack, int startingIndex) {
            return nextSelectedIndex(stack, null, startingIndex);
        }

        /**
         * Finds the next selected index matching the given stack. If none are found,
         * default to matching with the backup stack if that is given.
         * Otherwise, clamp the given starting index.
         *
         * @param stack the stack to match with
         * @param backupStack the stack to match with if first stack is not found
         * @param startingIndex the index to start search from
         * @return the new index; <code>-1</code> if none
         */
        public int nextSelectedIndex(ItemStack stack, @Nullable ItemStack backupStack, int startingIndex) {
            if (isEmpty() || startingIndex == -1) return -1;

            int newIndex = indexOf(stack, startingIndex);

            if (newIndex != -1) {
                return newIndex;
            } else if (backupStack != null
                    && (newIndex = indexOf(backupStack, startingIndex)) != -1) {
                return newIndex;
            } else {
                return clampIndex(startingIndex);
            }
        }
    }
}
