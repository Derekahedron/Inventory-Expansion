package derekahedron.invexp.platform.services;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

/**
 * Registers creative items to the inventory.
 */
public interface ICreativeItemsRegistrar {

    /**
     * Registers items to the creative inventory.
     *
     * @param tab the tab to register the items under
     * @param putAfter how to put the items in the inventory
     */
    void register(ResourceKey<CreativeModeTab> tab, PutAfter putAfter);

    record PutAfter(Supplier<ItemStack> baseStack, List<Supplier<ItemStack>> stacks) {}
}
