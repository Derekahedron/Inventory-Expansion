package derekahedron.invexp.platform.services;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/**
 * Registers item model overrides.
 */
public interface IItemOverrideRegistrar {

    /**
     * Registers an item model override.
     *
     * @param item the supplier for the item to register
     * @param resourceLocation the resource location to register
     * @param itemPropertyFunction the override function
     */
    void register(
            Supplier<Item> item,
            ResourceLocation resourceLocation,
            ClampedItemPropertyFunction itemPropertyFunction);
}
