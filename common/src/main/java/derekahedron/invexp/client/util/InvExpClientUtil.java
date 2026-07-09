package derekahedron.invexp.client.util;

import derekahedron.invexp.network.SetSelectedIndexPacket;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.util.ModdedInventoriesEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Contains client-only utility methods for Inventory Expansion.
 */
public class InvExpClientUtil {

    /**
     * Sends an update packet for updating the selected index of an item in a slot.
     *
     * @param player the player to update the index of
     * @param slot the slot to update the index for
     * @param selectedIndex the selected index to update
     * @return <code>true</code> if the selected index was updated; <code>false</code> otherwise
     */
    public static boolean sendSetSelectedIndexPacket(Player player, Slot slot, int selectedIndex) {
        if (player.containerMenu instanceof CreativeModeInventoryScreen.ItemPickerMenu) {
            return sendSetSelectedIndexPacket(player, slot.getItem(), selectedIndex);
        } else if (sendModdedSetSelectedIndexPacket(player, slot.getItem(), selectedIndex)) {
            return true;
        } else {
            Services.NETWORK_HANDLER.sendC2S(new SetSelectedIndexPacket(slot.index, selectedIndex));
            return true;
        }
    }

    /**
     * Sends an update packet for updating the selected index of an item in the players modded inventory.
     *
     * @param player the player to update the index of
     * @param stack the stack to update the index of
     * @param selectedIndex the selected index to update
     * @return <code>true</code> if the selected index was updated; <code>false</code> otherwise
     */
    public static boolean sendModdedSetSelectedIndexPacket(Player player, ItemStack stack, int selectedIndex) {
        ModdedInventoriesEvent.SelectedIndexSlotHandler handler = ModdedInventoriesEvent.getHandlers(player)
                .filter(h -> h.getStack() == stack)
                .findFirst()
                .orElse(null);

        if (handler != null) {
            handler.setSelectedIndex(selectedIndex);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sends an update packet for updating the selected index of an item in the players inventory.
     *
     * @param player the player to update the index of
     * @param stack the stack to update the index of
     * @param selectedIndex the selected index to update
     * @return <code>true</code> if the selected index was updated; <code>false</code> otherwise
     */
    public static boolean sendSetSelectedIndexPacket(Player player, ItemStack stack, int selectedIndex) {
        if (sendModdedSetSelectedIndexPacket(player, stack, selectedIndex)) return true;

        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof CreativeModeInventoryScreen.ItemPickerMenu) {
            menu = player.inventoryMenu;
        }

        for (Slot slot : menu.slots) {
            if (slot.getItem() == stack) {
                Services.NETWORK_HANDLER.sendC2S(new SetSelectedIndexPacket(slot.index, selectedIndex));
                return true;
            }
        }

        return false;
    }

    /**
     * Generates a count label for a given item count. If the count is over the provided maximum,
     * it is rendered in yellow.
     *
     * @param count the count of the item
     * @param maxCount the max count to cap this at
     * @return a string formatted to display the count
     */
    public static String getCountLabel(int count, int maxCount) {
        return count <= maxCount
                ? String.valueOf(count)
                : ChatFormatting.YELLOW + String.valueOf(maxCount);
    }
}
