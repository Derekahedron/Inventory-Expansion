package derekahedron.invexp.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.List;

/**
 * Holds items that have an "open" texture, like the Sack or Bundle.
 * Items registered here have their corresponding <code>_open</code> models registered by the client.
 */
public class OpenItemTexturesRegistry {

    private static final HashSet<Item> OPEN_ITEMS = new HashSet<>();

    /**
     * Prepares an item to have its open models registered by the client.
     *
     * @param item the {@link Item} that should have its open models registered
     */
    public static void addItem(Item item) {
        OPEN_ITEMS.add(item);
    }

    /**
     * Gets a list of all items that were registered to have their open models added.
     *
     * @return a list of all items that were registered
     */
    public static List<Item> getItems() {
        return OPEN_ITEMS.stream().toList();
    }

    static {
        // Immediately add the bundle to the open textures list
        addItem(Items.BUNDLE);
    }
}
