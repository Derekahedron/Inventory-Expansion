package derekahedron.invexp.client.util;

import derekahedron.invexp.mixin.client.AbstractContainerScreenAccessor;
import derekahedron.invexp.mixin.client.AbstractContainerScreenInvoker;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Defines rules for when a container item can be dragged over an inventory screen to collect
 * or remove items.
 */
public interface ContainerItemSlotDragger {

    /**
     * Checks if the container item is empty.
     *
     * @return <code>true</code> if the container item is empty; <code>false</code> otherwise
     */
    boolean isEmpty();

    /**
     * Checks if you can try to insert a given item in to the container item.
     *
     * @param stack the ItemStack to try to insert
     * @return <code>true</code> if you can try to insert the ItemStack into the container item;
     * <code>false</code> otherwise
     */
    boolean canTryInsert(ItemStack stack);

    /**
     * Simulates clicking a slot when you drag a container item of a slot.
     *
     * @param slot the slot that was dragged over
     * @param screen the current screen
     */
    default void onHover(Slot slot, AbstractContainerScreen<?> screen) {
        ItemStack stack = slot.getItem();

        if (screen.getMenu().canDragTo(slot)) {
            int quickCraftingType = ((AbstractContainerScreenAccessor) screen).invexp$getQuickCraftingType();

            if (quickCraftingType == 1) {
                if (stack.isEmpty() && !isEmpty()) {
                    ((AbstractContainerScreenInvoker) screen).invokeSlotClicked(
                            slot, slot.getContainerSlot(), quickCraftingType, ClickType.PICKUP);
                }
            } else if (quickCraftingType == 0) {
                if (!stack.isEmpty() && canTryInsert(stack)) {
                    ((AbstractContainerScreenInvoker) screen).invokeSlotClicked(
                            slot, slot.getContainerSlot(), quickCraftingType, ClickType.PICKUP);
                }
            }
        }
    }
}
