package derekahedron.invexp.client.util;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Contains client-only utility methods for Inventory Expansion.
 */
public class InvExpClientUtil {

    /**
     * Finds the true {@link Slot} to use for network events. The creative inventory uses its own slots,
     * so we need to adjust for this when using slot ids
     *
     * @param slot the original slot
     * @param player the {@link Player} who's inventory is open
     * @return the true slot to use for syncing
     */
    @Nullable
    public static Slot getTrueSlot(Slot slot, @Nullable Player player) {
        if (player == null) return null;

        // Creative screens do not have slot ids that are synced, so we must find the corresponding slot
        // in the creative inventory
        if (player.containerMenu instanceof CreativeModeInventoryScreen.ItemPickerMenu) {
            ItemStack stack = slot.getItem();
            return player.inventoryMenu.slots.stream()
                    .filter(s -> s.getItem() == stack)
                    .findFirst().orElse(null);
        }
        return slot;
    }
}
