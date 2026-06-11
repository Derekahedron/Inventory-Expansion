package derekahedron.invexp.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Holds callbacks for implementing modded inventories to insert items into and shoot items from.
 */
public class ExtraInventoryRegistry {

    private static final List<Function<LivingEntity, Stream<ItemStack>>> INVENTORY_GETTERS = new ArrayList<>();

    /**
     * Registers a getter function that returns a stream of {@linkplain ItemStack ItemStacks} in the modded inventory.
     *
     * @param getter the function to register
     */
    public static void registerExtraInventory(Function<LivingEntity, Stream<ItemStack>> getter) {
        INVENTORY_GETTERS.add(getter);
    }

    /**
     * Gets all modded inventories for the given entity.
     *
     * @param entity the entity to get the modded inventories for
     * @return a stream of all non-empty ItemStacks in the modded inventories
     */
    public static Stream<ItemStack> getExtraInventory(LivingEntity entity) {
        return INVENTORY_GETTERS.stream()
                .flatMap(getter -> getter.apply(entity))
                .filter(stack -> !stack.isEmpty());
    }
}
