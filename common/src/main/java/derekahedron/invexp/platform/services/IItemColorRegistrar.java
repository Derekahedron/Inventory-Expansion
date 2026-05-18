package derekahedron.invexp.platform.services;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Registers item color overlays.
 */
public interface IItemColorRegistrar {

    /**
     * Registers an item color.
     *
     * @param itemColor the item color to register
     * @param item the item to register the color for
     */
    void register(ItemColor itemColor, Supplier<Item> item);
}
