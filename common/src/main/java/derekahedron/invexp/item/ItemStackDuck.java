package derekahedron.invexp.item;

import derekahedron.invexp.containeritem.ContainerItemContents;
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

    /**
     * Sets the cached container item contents on the item stack. Used to avoid reloading from nbt each time.
     *
     * @param contents the cached contents to set
     */
    void invexp$setCachedContents(@Nullable ContainerItemContents contents);

    /**
     * Gets the cached container item contents stored in the items stack.
     *
     * @return the cached container item contents associated with the item stack; <code>null</code> if there is none
     */
    @Nullable
    ContainerItemContents invexp$getCachedContents();
}
