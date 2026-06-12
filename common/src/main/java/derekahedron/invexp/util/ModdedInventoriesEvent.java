package derekahedron.invexp.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Holds callbacks for implementing modded inventories to insert items into and shoot items from.
 */
public class ModdedInventoriesEvent {

    private static final ArrayList<Callback> CALLBACKS = new ArrayList<>();

    /**
     * Registers a callback to get all the slot handlers for a modded inventory.
     *
     * @param callback the callback that gets handlers for the players inventory
     */
    public static void register(Callback callback) {
        CALLBACKS.add(callback);
    }

    /**
     * Gets a stream of all {@linkplain ItemStack ItemStacks} in the player's modded inventories.
     *
     * @param player the {@link Player} the get the inventories for
     * @return a {@link Stream} of {@linkplain ItemStack ItemStacks} from the given player's inventories
     */
    public static Stream<ItemStack> getItemStacks(Player player) {
        return getHandlers(player)
                .map(SelectedIndexSlotHandler::getStack);
    }

    /**
     * Gets all {@link SelectedIndexSlotHandler} for a given player's modded inventories.
     *
     * @param player the {@link Player} to get the inventories for
     * @return a {@link Stream} of handlers for the slots in the given players inventory
     */
    public static Stream<SelectedIndexSlotHandler> getHandlers(Player player) {
        return CALLBACKS.stream()
                .flatMap(callback -> callback.getHandlers(player));
    }

    /**
     * Callback for getting slot handlers.
     */
    public interface Callback {

        /**
         * Gets a stream of slot handlers for the player to extend the player's curios inventory.
         *
         * @param player the player to get the slot handlers for
         * @return a stream of slot handlers for the given player
         */
        Stream<SelectedIndexSlotHandler> getHandlers(Player player);
    }

    /**
     * Handler for getting the {@link ItemStack} in a slot and setting the selected index of a slot.
     */
    public interface SelectedIndexSlotHandler {

        /**
         * Gets the stack in a slot for the handler.
         *
         * @return the {@link ItemStack} in the slot
         */
        ItemStack getStack();

        /**
         * Sets the selected index for the given slot.
         *
         * @param selectedIndex the new selected index
         */
        void setSelectedIndex(int selectedIndex);
    }
}
