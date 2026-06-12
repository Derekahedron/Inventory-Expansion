package derekahedron.invexp.client.util;

import derekahedron.invexp.network.SetSelectedIndexPacket;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.util.ModdedInventoriesEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Contains client-only utility methods for Inventory Expansion.
 */
public class InvExpClientUtil {

    /**
     * Gets the selected index handler for a given slot and a given player.
     *
     * @param slot the slot to get the handler for
     * @param player the player to get the handler for
     * @return a slot handler for setting the selected index of the given slot
     */
    public static ModdedInventoriesEvent.SelectedIndexSlotHandler getHandler(Slot slot, Player player) {
        ItemStack stack = slot.getItem();
        ModdedInventoriesEvent.SelectedIndexSlotHandler handler = ModdedInventoriesEvent.getHandlers(player)
                .filter(h -> h.getStack() == stack)
                .findFirst()
                .orElse(null);

        if (handler != null) return handler;

        int slotId;
        // The creative menu has slots that are desynced from the inventory menu, so we have to search the basic
        // inventory menu instead.
        if (player.containerMenu instanceof CreativeModeInventoryScreen.ItemPickerMenu) {
            slotId = player.inventoryMenu.slots.stream()
                    .filter(s -> s.getItem() == stack)
                    .map(s -> s.index)
                    .findFirst()
                    .orElse(slot.index);
        } else {
            slotId = slot.index;
        }

        return new ModdedInventoriesEvent.SelectedIndexSlotHandler() {
            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void setSelectedIndex(int selectedIndex) {
                Services.NETWORK_HANDLER.sendC2S(new SetSelectedIndexPacket(slotId, selectedIndex));
            }
        };
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
