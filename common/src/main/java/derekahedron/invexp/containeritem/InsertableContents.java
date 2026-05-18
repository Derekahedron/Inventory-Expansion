package derekahedron.invexp.containeritem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

/**
 * Determines if an item can be picked up into a container item, like from double-clicking or picking up an item.
 */
public interface InsertableContents {

    /**
     * Gets whether the given stack can be inserted into the container item.
     *
     * @param stack the stack to be inserted
     * @return if the stack can be inserted
     */
    default boolean canInsert(ItemStack stack) {
        return true;
    }

    /**
     * Gets the priority that this item should have when inserting items during pickup.
     * Used to make quivers have a higher priority than sacks.
     *
     * @return the priority this container item has when picking up items
     */
    default int getPickupPriority() {
        return 0;
    }

    /**
     * Plays a sound after double-clicking and inserting items into the container item.
     *
     * @param entity the entity to play the sound from
     */
    void playInsertSound(Entity entity);
}
