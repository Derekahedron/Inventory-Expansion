package derekahedron.invexp.fabric.compat;

import derekahedron.invexp.util.ExtraInventoryRegistry;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.util.Tuple;

import java.util.List;

/**
 * Entrypoint for general Trinkets compatibility.
 */
public class TrinketsCompat {

    /**
     * Initializes Trinkets.
     */
    public static void init() {
        // Registers the trinkets inventory for inserting items into container items
        ExtraInventoryRegistry.registerExtraInventory(entity ->
                TrinketsApi.getTrinketComponent(entity).map(component ->
                        component.getAllEquipped().stream()
                                .map(Tuple::getB)
                                .toList())
                        .orElse(List.of()));
    }
}