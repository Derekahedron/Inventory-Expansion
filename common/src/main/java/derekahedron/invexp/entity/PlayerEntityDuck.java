package derekahedron.invexp.entity;

import derekahedron.invexp.containeritem.ContainerItemUsage;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Adds methods to the player entity. When a player starts using a container item,
 * {@linkplain ContainerItemUsage usages} are created for the main hand and offhand. If the stack in the main hand or
 * offhand change while using the container item, this will not cause issues as the usage is associated
 * more with the ItemStack than the hand.
 * When the player stops using the container item, changes made to the usage are applied to the container item.
 */
public interface PlayerEntityDuck {

    /**
     * Gets if the given player is currently using a container item.
     *
     * @return if a container item is being used
     */
    boolean invexp$isUsingContainerItem();

    /**
     * Marks the player as using a container item and creates {@linkplain ContainerItemUsage usages} to track the items used.
     */
    void invexp$startUsingContainerItem();

    /**
     * Marks the player as no longer using a container item and applies any changes made to players used items.
     */
    void invexp$stopUsingContainerItem();

    /**
     * Gets the {@link ContainerItemUsage usage} for the player with a container item matching the given stack.
     *
     * @param containerStack the container stack being used
     * @return the container item usage that the given container stack is using; <code>null</code> if there is none
     */
    @Nullable
    ContainerItemUsage invexp$getUsageForContainerStack(ItemStack containerStack);

    /**
     * Gets the usage for the player with a selected stack matching the given stack.
     *
     * @param selectedStack the selected stack to get usage for
     * @return the container item usage associated with the selected stack; <code>null</code> if there is none
     */
    @Nullable
    ContainerItemUsage invexp$getUsageForSelectedStack(ItemStack selectedStack);
}
