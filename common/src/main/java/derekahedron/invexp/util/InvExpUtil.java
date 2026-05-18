package derekahedron.invexp.util;

import derekahedron.invexp.InventoryExpansion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Contains utility methods for Inventory Expansion.
 */
public class InvExpUtil {

    /**
     * Creates a new {@link ResourceLocation} for Inventory Expansion.
     *
     * @param path a {@link String}> to use as the path
     * @return the {@link ResourceLocation} that was created
     */
    public static ResourceLocation location(String path) {
        return new ResourceLocation(InventoryExpansion.MOD_ID, path);
    }

    /**
     * Signals to players screen handler that content was changed.
     *
     * @param player the {@link Player} whose inventory changed
     */
    public static void onContentChanged(Player player) {
        AbstractContainerMenu menu = player.containerMenu;
        menu.slotsChanged(player.getInventory());
    }
}
