package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.ICreativeItemsRegistrar;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FabricCreativeItemsRegistrar implements ICreativeItemsRegistrar {
    private static final Map<ResourceKey<CreativeModeTab>, ArrayList<PutAfter>> REGISTRATIONS = new HashMap<>();

    @Override
    public void register(ResourceKey<CreativeModeTab> tab, PutAfter putAfter) {
        REGISTRATIONS.computeIfAbsent(tab, k -> new ArrayList<>()).add(putAfter);
    }

    /**
     * Initializes the creative inventory.
     */
    public static void init() {
        REGISTRATIONS.forEach((tab, putAfters) ->
                ItemGroupEvents.modifyEntriesEvent(tab).register(itemGroup -> {
                    for (PutAfter putAfter : putAfters) {
                        ItemStack baseStack = putAfter.baseStack().get();
                        ItemStack newStack;

                        for (Supplier<ItemStack> item : putAfter.stacks()) {
                            newStack = item.get();
                            itemGroup.addAfter(baseStack, newStack);
                            baseStack = newStack;
                        }
                    }
                }));
    }
}
