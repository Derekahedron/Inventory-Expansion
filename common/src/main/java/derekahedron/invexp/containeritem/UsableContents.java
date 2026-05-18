package derekahedron.invexp.containeritem;

import derekahedron.invexp.entity.PlayerEntityDuck;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Determines if a type of container item contents can use items directly from the stack, like a sack.
 */
public interface UsableContents {

    /**
     * Gets whether the given contents can be used
     *
     * @return if the contents can be used
     */
    default boolean canUse() {
        return true;
    }

    /**
     * Gets the usable selected stack of a given container stack. Returns the given item if not applicable.
     *
     * @param stack the stack to check the contents of
     * @return the selected stack of the given stack; returns itself if not applicable
     */
    static ItemStack selectedStackOf(ItemStack stack) {
        ContainerItemContentsWriter contents = ContainerItemBehaviors.getUsableContents(stack)
                .orElse(null);

        if (contents != null && !contents.isEmpty()) {
            return contents.getSelectedStack();
        } else {
            return stack;
        }
    }

    /**
     * Gets the usable selected stack of a given container stack. Returns the given item if not applicable.
     * If a given user is actually currently "using" the stack, return the exact stack instance being used.
     *
     * @param stack the stack to check the contents of
     * @param user the potentially holding the stack
     * @return the selected stack of the given stack; returns itself if not applicable
     */
    static ItemStack selectedStackOf(ItemStack stack, @Nullable LivingEntity user) {
        if (user instanceof Player player) {
            ContainerItemUsage usage = ((PlayerEntityDuck) player).invexp$getUsageForContainerStack(stack);

            if (usage != null) {
                ContainerItemContentsWriter contents = ContainerItemBehaviors.getUsableContents(usage.containerStack)
                        .orElse(null);

                if (contents != null && !contents.isEmpty()) {
                    ItemStack selectedStack = contents.getSelectedStack();
                    if (ItemStack.matches(usage.selectedStack, selectedStack)) {
                        return usage.selectedStack;
                    } else {
                        return selectedStack;
                    }
                } else {
                    return usage.containerStack;
                }
            }
        }

        // Fallback to just getting the contents
        return selectedStackOf(stack);
    }
}
