package derekahedron.invexp.item;

import derekahedron.invexp.containeritem.ContainerItemContentsWriter;

import javax.annotation.Nullable;

/**
 * Adds methods for setting a tracked container item contents to an ItemStack, which can be used to
 * decrease the count of the item inside a quiver when fired from teh quiver.
 */
public interface ItemStackDuck {

    /**
     * Tracks a ContainerItemContents to update when the stack is decreased.
     *
     * @param contents The contents to update
     */
    void invexp$setContainerItemContents(@Nullable ContainerItemContentsWriter contents);
}
