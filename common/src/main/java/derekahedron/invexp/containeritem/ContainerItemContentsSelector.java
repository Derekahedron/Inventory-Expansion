package derekahedron.invexp.containeritem;

import derekahedron.invexp.util.ModdedInventoriesEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Contains information about selecting a new selected item for quick swapping the contents of
 * a container item.
 */
public class ContainerItemContentsSelector {

    public final ItemStack heldStack;
    public final ContainerItemContentsWriter contents;
    public final List<ItemStack> compressedStacks;
    public final int selectedIndex;

    /**
     * Creates a selector using the held stack, container item contents, and a predicate to test
     * which stacks can be used.
     *
     * @param heldStack the stack being held
     * @param contents the contents of the container item
     * @param predicate predicate that tests which items can go in the compressed stacks
     */
    public ContainerItemContentsSelector(
            ItemStack heldStack,
            ContainerItemContentsWriter contents,
            Predicate<ItemStack> predicate) {
        this.heldStack = heldStack;
        this.contents = contents;

        // Compress the stacks into a list to avoid repeats
        ArrayList<ItemStack> compressedStacks = new ArrayList<>(contents.getStacks().size());
        int selectedIndex = -1;
        ItemStack selectedStack = contents.getSelectedStack(predicate);
        // This is a cache of item to the list of ItemStacks so we don't have to iterate through
        // the entire list every time we add an item.
        Map<Item, ArrayList<ItemStack>> stackCache = new HashMap<>();

        for (ItemStack nestedStack : contents.getStacks()) {
            if (!predicate.test(nestedStack)) continue;

            ArrayList<ItemStack> existingStacks = stackCache.computeIfAbsent(
                    nestedStack.getItem(),
                    (i) -> new ArrayList<>());

            boolean foundStack = false;
            for (ItemStack stack : existingStacks) {
                if (ItemStack.isSameItemSameTags(nestedStack, stack)) {
                    stack.grow(nestedStack.getCount());
                    foundStack = true;
                    break;
                }
            }

            if (!foundStack) {
                ItemStack stack = nestedStack.copy();
                if (selectedIndex == -1 && ItemStack.isSameItemSameTags(stack, selectedStack)) {
                    selectedIndex = compressedStacks.size();
                }
                existingStacks.add(stack);
                compressedStacks.add(stack);
            }
        }

        this.compressedStacks = List.copyOf(compressedStacks);
        this.selectedIndex = selectedIndex;
    }

    /**
     * Gets the currently selected stack
     *
     * @return the currently selected stack; <code>ItemStack.EMPTY</code> if there isn't one
     */
    public ItemStack getSelectedStack() {
        if (selectedIndex == -1) return ItemStack.EMPTY;
        return compressedStacks.get(selectedIndex);
    }

    /**
     * Gets the current container item selector for the player. Uses items in the players hands to
     * see if there are any relevant selectors, starting with the main hand.
     *
     * @param player the player to search for a selector for
     * @return a valid selector if it is present
     */
    public static Optional<ContainerItemContentsSelector> getSelector(Player player) {
        Optional<ContainerItemContentsSelector> selector = Optional.empty();

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack heldStack = player.getItemInHand(hand);

            // First check if the item is shootable like a bow
            if (heldStack.getItem() instanceof ProjectileWeaponItem weaponItem) {
                // Check for mainhand/offhand first as they have a different predicate
                Predicate<ItemStack> heldPredicate = weaponItem.getSupportedHeldProjectiles();
                selector = Stream.of(InteractionHand.OFF_HAND, InteractionHand.MAIN_HAND)
                        .map(player::getItemInHand)
                        .map(ContainerItemBehaviors::getShootableContents)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(contents -> new ContainerItemContentsSelector(
                                heldStack,
                                contents,
                                heldPredicate))
                        .filter(s -> !s.compressedStacks.isEmpty())
                        .findFirst();

                if (selector.isPresent()) return selector;

                // Then check for the rest of the inventory
                Stream<ItemStack> items = Stream.of(
                        ModdedInventoriesEvent.getItemStacks(player),
                        player.getInventory().items.stream(),
                        player.getInventory().armor.stream(),
                        player.getInventory().offhand.stream()).flatMap(Function.identity());

                Predicate<ItemStack> allPredicate = weaponItem.getAllSupportedProjectiles();
                selector = items
                        .map(ContainerItemBehaviors::getShootableContents)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(contents -> new ContainerItemContentsSelector(
                                heldStack,
                                contents,
                                allPredicate))
                        .filter(s -> !s.compressedStacks.isEmpty())
                        .findFirst();

                if (selector.isPresent()) return selector;
            }

            // Then check if the item itself is a usable container item like a sack
            ContainerItemContentsWriter contents = ContainerItemBehaviors.getUsableContents(heldStack).orElse(null);
            if (contents != null) {
                selector = Optional.of(new ContainerItemContentsSelector(heldStack, contents, (stack) -> true))
                        .filter(s -> !s.compressedStacks.isEmpty());

                if (selector.isPresent()) return selector;
            }
        }

        return selector;
    }
}
