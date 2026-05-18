package derekahedron.invexp.platform.services;

import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Service for registering items.
 */
public interface IItemRegistrar {

    /**
     * Registers an item.
     *
     * @param path the path to register the item under
     * @param supplier the supplier for the item to be registered
     * @return the supplier for the registered item
     */
    Supplier<Item> register(String path, Supplier<Item> supplier);
}
