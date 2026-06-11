package derekahedron.invexp.forge.compat;

import derekahedron.invexp.util.ExtraInventoryRegistry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Entrypoint for general Curios compatibility.
 */
public class CuriosCompat {

    public static final String QUIVER_SLOT_IDENTIFIER = "quiver";

    /**
     * Initializes Curios.
     */
    public static void init() {
        // Registers the curios inventory for inserting items into container items
        ExtraInventoryRegistry.registerExtraInventory(entity -> CuriosApi.getCuriosInventory(entity)
                .resolve()
                .stream()
                .map(ICuriosItemHandler::getCurios)
                .flatMap(curios -> Stream.concat(
                        // First get the quiver slot so it has priority
                        Optional.ofNullable(curios.get(QUIVER_SLOT_IDENTIFIER))
                                .stream()
                                .map(ICurioStacksHandler::getStacks),
                        // Then get the rest of the inventory
                        curios.entrySet().stream()
                                .filter(entry -> !entry.getKey().equals(QUIVER_SLOT_IDENTIFIER))
                                .map(Map.Entry::getValue)
                                .map(ICurioStacksHandler::getStacks))
                        .flatMap(stacks -> IntStream.range(0, stacks.getSlots())
                                .mapToObj(stacks::getStackInSlot))));
    }
}
