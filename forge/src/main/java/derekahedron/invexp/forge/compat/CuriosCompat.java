package derekahedron.invexp.forge.compat;

import derekahedron.invexp.util.ExtraInventoryRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Entrypoint for general Curios compatibility.
 */
public class CuriosCompat {

    /**
     * Initializes Trinkets.
     */
    public static void init() {
        // Registers the curios inventory for inserting items into container items
        ExtraInventoryRegistry.registerExtraInventory(entity -> CuriosApi.getCuriosInventory(entity)
                .map(handler -> {
                    IItemHandlerModifiable curios = handler.getEquippedCurios();
                    List<ItemStack> stacks = new ArrayList<>(curios.getSlots());

                    for (int i = 0; i < curios.getSlots(); i++) {
                        stacks.add(curios.getStackInSlot(i));
                    }

                    return stacks;
                }).orElse(List.of()));
    }
}
