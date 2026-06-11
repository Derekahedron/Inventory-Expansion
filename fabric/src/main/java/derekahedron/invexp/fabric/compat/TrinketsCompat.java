package derekahedron.invexp.fabric.compat;

import derekahedron.invexp.util.ExtraInventoryRegistry;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;

import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Entrypoint for general Trinkets compatibility.
 */
public class TrinketsCompat {

    public static final String QUIVER_SLOT_GROUP = "chest";
    public static final String QUIVER_SLOT_NAME = "quiver";

    /**
     * Initializes Trinkets.
     */
    public static void init() {
        // Registers the trinkets inventory for inserting items into container items
        ExtraInventoryRegistry.registerExtraInventory(entity -> TrinketsApi.getTrinketComponent(entity)
                .stream()
                .map(TrinketComponent::getInventory)
                .flatMap(inventory -> Stream.concat(
                        // First get the quiver slot so it has priority
                        Optional.ofNullable(inventory.get(QUIVER_SLOT_GROUP))
                                .stream()
                                .flatMap(group ->
                                        Optional.ofNullable(group.get(QUIVER_SLOT_NAME)).stream()),
                        // Then get the remaining inventory
                        inventory.entrySet().stream()
                                .flatMap(groupEntry -> {
                                    boolean inQuiverGroup = groupEntry.getKey().equals(QUIVER_SLOT_GROUP);

                                    return groupEntry.getValue().entrySet().stream()
                                            .filter(slotEntry -> !inQuiverGroup
                                                    || !slotEntry.getKey().equals(QUIVER_SLOT_NAME))
                                            .map(Map.Entry::getValue);
                                }))
                        .flatMap(inv -> IntStream.range(0, inv.getContainerSize())
                                .mapToObj(inv::getItem))));
    }
}