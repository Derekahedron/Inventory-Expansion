package derekahedron.invexp.containeritem;

import derekahedron.invexp.InventoryExpansion;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Stores data when using a container item. These usages are created whenever a container item is used, and tracks
 * the initial state of the container and what the selected stack is. Updates the selected stack with any changes
 * when the container item is done being used.
 */
public class ContainerItemUsage {

    public final ItemStack containerStack;
    public ItemStack selectedStack;
    public ItemStack originalSelectedStack;

    public ContainerItemUsage(ContainerItemContentsWriter contents) {
        this(contents, null);
    }

    /**
     * Creates a new sack usage from an existing sack contents, as well as re-using the given
     * selectedStack instance if it matches the selected stack in the contents.
     *
     * @param contents contents to create the usage from
     * @param previousSelectedStack existing selected stack to possibly carry over from
     */
    public ContainerItemUsage(ContainerItemContentsWriter contents, @Nullable ItemStack previousSelectedStack) {
        containerStack = contents.containerStack;
        selectedStack = contents.copySelectedStack(previousSelectedStack);
        originalSelectedStack = selectedStack.copy();
    }

    /**
     * Applies changes made to the selected stack to the contents.
     *
     * @param leftoverStackConsumer the consumer to handle any leftover stacks
     */
    public void update(Consumer<ItemStack> leftoverStackConsumer) {
        ItemStack leftoverStack = ItemStack.EMPTY;
        ContainerItemContentsWriter contents = ContainerItemBehaviors.getUsableContents(containerStack)
                .orElse(null);
        if (contents == null) {
            InventoryExpansion.LOGGER.warn("Contents of {} is invalid when updating usage!", containerStack);
            return;
        }

        // Original selected stack should never be empty, but we can handle it if it is
        if (originalSelectedStack.isEmpty()) {
            if (!selectedStack.isEmpty()) {
                ContainerItemContentsWriter.Builder builder = contents.getBuilder();
                leftoverStack = selectedStack.copy();

                if (builder.add(leftoverStack, 0) > 0) {
                    builder.apply();
                }
            }
        } else {
            if (selectedStack.isEmpty()) {
                // Remove original selected stack if selected stack is now empty
                ContainerItemContentsWriter.Builder builder = contents.getBuilder();

                if (builder.remove(originalSelectedStack) != originalSelectedStack.getCount()) {
                    InventoryExpansion.LOGGER.warn("ItemStack {} not fully removed.", originalSelectedStack);
                }
                builder.apply();
            }
            else if (ItemStack.isSameItemSameTags(originalSelectedStack, selectedStack)) {
                // If stacks match, add/remove difference
                int countDiff = selectedStack.getCount() - originalSelectedStack.getCount();
                if (countDiff > 0) {
                    // Add stacks for positive difference
                    ContainerItemContentsWriter.Builder builder = contents.getBuilder();
                    leftoverStack = selectedStack.copyWithCount(countDiff);

                    if (builder.add(leftoverStack, 0) > 0) {
                        builder.apply();
                    }
                } else if (countDiff < 0) {
                    // Remove stacks for negative difference
                    ContainerItemContentsWriter.Builder builder = contents.getBuilder();

                    if (builder.remove(originalSelectedStack, -countDiff) != -countDiff) {
                        InventoryExpansion.LOGGER.warn(
                                "Count difference ({}) for {} not fully removed",
                                -countDiff, originalSelectedStack
                        );
                    }
                    builder.apply();
                }
            } else {
                // Stack is fully replaced
                ContainerItemContentsWriter.Builder builder = contents.getBuilder();
                leftoverStack = selectedStack.copy();

                if (builder.replaceSelectedStack(leftoverStack, originalSelectedStack.getCount())) {
                    builder.apply();
                }
            }
        }
        // Make new selected stacks
        selectedStack = contents.copySelectedStack(selectedStack);
        originalSelectedStack = selectedStack.copy();

        if (!leftoverStack.isEmpty()) {
            leftoverStackConsumer.accept(leftoverStack);
        }
    }
}
