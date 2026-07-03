package derekahedron.invexp.containeritem;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Manages all registered container item behaviors. Making sacks usable and allowing arrows to be fired from
 * quivers is registered here.
 */
public class ContainerItemBehaviors {

    private static final ArrayList<ContainerItemBehavior> BEHAVIORS = new ArrayList<>();

    /**
     * Registers a behavior to be used in events.
     *
     * @param behavior the {@link ContainerItemBehavior} being registered.
     */
    public static void register(ContainerItemBehavior behavior) {
        BEHAVIORS.add(behavior);
    }

    /**
     * Gets the contents from the given container item stack.
     *
     * @param stack the container item stack
     * @return the contents that the given stack has attached
     */
    public static Optional<ContainerItemContentsWriter> getContents(ItemStack stack) {
        for (ContainerItemBehavior behavior : BEHAVIORS) {
            Optional<ContainerItemContentsWriter> contents = behavior.getContents(stack);

            if (contents.isPresent()) {
                return contents;
            }
        }
        return Optional.empty();
    }

    /**
     * Gets the insertable contents from the given container item stack. These contents can be inserted into, like
     * when picking up an item.
     *
     * @param stack the container item stack
     * @return the insertable contents that the given stack has attached
     */
    public static Optional<ContainerItemContentsWriter> getInsertableContents(ItemStack stack) {
        return getContents(stack)
                .filter(InsertableContents.class::isInstance);
    }

    /**
     * Gets the usable contents from the given container item stack. These contents can have their selected stack used
     * by the player.
     *
     * @param stack the container item stack
     * @return the contents that the given stack has attached
     */
    public static Optional<ContainerItemContentsWriter> getUsableContents(ItemStack stack) {
        return getContents(stack)
                .filter(UsableContents.class::isInstance)
                .filter(contents -> ((UsableContents) contents).canUse());
    }


    /**
     * Gets the contents from the given container item stack. These contents can have their stored stacks used as ammo.
     *
     * @param stack the container item stack
     * @return the contents that the given stack has attached
     */
    public static Optional<ContainerItemContentsWriter> getShootableContents(ItemStack stack) {
        return getContents(stack)
                .filter(ShootableContents.class::isInstance);
    }

    /**
     * Stores a function for getting a container item contents from the given stack. Is run during events to
     * determine what contents a given stack hold.
     */
    public interface ContainerItemBehavior {

        /**
         * Gets the contents from a given stack, if applicable.
         *
         * @param stack the stack to get the contents of
         * @return the optional container item contents to use for the event
         */
        Optional<ContainerItemContentsWriter> getContents(ItemStack stack);
    }
}
