package derekahedron.invexp.containeritem;

import derekahedron.invexp.item.ItemStackDuck;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Determines if a type of container item contents can have its contents used as ammo, like a quiver.
 */
public interface ShootableContents {

    /**
     * Gets the projectile stack matching the predicate from the given container item contents.
     *
     * @param contents the contents to check for the ammo
     * @param predicate the predicate to determine what type of stacks can be used
     * @return the ammo stack to be used; <code>ItemStack.EMPTY</code> if none
     */
    static Optional<ItemStack> getProjectileStack(ContainerItemContentsWriter contents, Predicate<ItemStack> predicate) {
        if (contents.isEmpty()) return Optional.empty();

        // Make sure the selected stack matches the predicate
        ItemStack selectedStack = contents.getSelectedStack();
        if (!predicate.test(selectedStack)) return Optional.empty();
        ItemStack ammoStack = selectedStack;

        for (int i = contents.getStacks().size() - 1; i >= 0; i--) {
            ItemStack nestedStack = contents.getStacks().get(i);

            if (ItemStack.isSameItemSameTags(selectedStack, nestedStack)) {
                ammoStack = nestedStack;
                break;
            }
        }

        ammoStack = ammoStack.copy();
        ((ItemStackDuck) (Object) ammoStack).invexp$setContainerItemContents(contents);
        return Optional.of(ammoStack);
    }

    /**
     * Gets the projectile stack matching the predicate from the given container item.
     *
     * @param stack the container item to get the projectile from
     * @param predicate the predicate to determine what type of stacks can be used
     * @return the ammo stack to be used; <code>ItemStack.EMPTY</code> if none
     */
    static ItemStack getProjectileStack(
            ItemStack stack,
            Predicate<ItemStack> predicate) {
        return ContainerItemBehaviors.getShootableContents(stack)
                .flatMap(contents -> ShootableContents.getProjectileStack(contents, predicate))
                .orElse(ItemStack.EMPTY);
    }
}
