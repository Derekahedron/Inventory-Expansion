package derekahedron.invexp.containeritem;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;

/**
 * Wraps a given item predicate inside another predicate that tests all the contents of a container item.
 */
public class ContainerItemItemPredicate extends ItemPredicate {

    public final ItemPredicate predicate;

    public ContainerItemItemPredicate(ItemPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean matches(ItemStack stack) {
        if (predicate.matches(stack)) return true;

        ContainerItemContentsReader contents = ContainerItemBehaviors.getInsertableContents(stack)
                .orElse(null);

        if (contents != null && !contents.isEmpty()) {
            for (ItemStack nestedStack : contents.getStacks()) {
                if (!nestedStack.isEmpty() && predicate.matches(nestedStack)) {
                    return true;
                }
            }
        }
        return false;
    }
}
