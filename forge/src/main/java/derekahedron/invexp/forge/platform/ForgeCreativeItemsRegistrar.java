package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.ICreativeItemsRegistrar;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ForgeCreativeItemsRegistrar implements ICreativeItemsRegistrar {

    private static final Map<ResourceKey<CreativeModeTab>, ArrayList<PutAfter>> REGISTRATIONS = new HashMap<>();

    @Override
    public void register(ResourceKey<CreativeModeTab> tab, PutAfter putAfter) {
        REGISTRATIONS.computeIfAbsent(tab, k -> new ArrayList<>()).add(putAfter);
    }

    /**
     * Initializes the creative inventory.
     *
     * @param event the event to register under
     */
    public static void init(BuildCreativeModeTabContentsEvent event) {
        if (REGISTRATIONS.containsKey(event.getTabKey())) {
            for (PutAfter putAfter : REGISTRATIONS.get(event.getTabKey())) {
                ItemStack baseStack = putAfter.baseStack().get();
                ItemStack newStack;

                for (Supplier<ItemStack> item : putAfter.stacks()) {
                    newStack = item.get();
                    event.getEntries().putAfter(baseStack, newStack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    baseStack = newStack;
                }
            }
        }
    }
}
