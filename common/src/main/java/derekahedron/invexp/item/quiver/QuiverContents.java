package derekahedron.invexp.item.quiver;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.containeritem.ContainerItemContents;
import derekahedron.invexp.item.ItemStackDuck;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Holds the raw quiver contents attached to a quiver via nbt. We rely on the actual quiver item to get the full
 * information, like max weight, so this only hold info related to the nbt itself.
 */
public class QuiverContents implements ContainerItemContents {

    public static final String COMPOUND_KEY = "InvExpQuiverContents";
    public static final String ITEMS_KEY = "Items";
    public static final String SELECTED_INDEX_KEY = "SelectedIndex";
    public static final String TOTAL_WEIGHT_KEY = "TotalWeight";
    public static final String NUMERATOR_KEY = "Numerator";
    public static final String DENOMINATOR_KEY = "Denominator";

    public final List<ItemStack> stacks;
    public final int selectedIndex;
    @Nullable
    private final Fraction totalWeight;

    /**
     * Creates a new QuiverContentsComponent from the given values.
     *
     * @param stacks list of stacks to give the component
     * @param selectedIndex selected index of the component
     * @param totalWeight total weight to store in the component
     */
    public QuiverContents(
            List<ItemStack> stacks,
            int selectedIndex,
            @Nullable Fraction totalWeight) {
        this.stacks = stacks;
        this.selectedIndex = selectedIndex;
        this.totalWeight = totalWeight;
    }

    /**
     * Creates a new empty QuiverContentsComponent.
     */
    public QuiverContents() {
        this(List.of(), -1, Fraction.ZERO);
    }

    @Override
    public List<ItemStack> getStacks() {
        return stacks;
    }

    @Override
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Gets the total weight of the contents if it has been calculated already.
     * The total weight should always be loaded unless there is bad data.
     *
     * @return the total weight of the contents; <code>empty</code> if it hasn't been calculated yet
     */
    public Optional<Fraction> getTotalWeight() {
        return Optional.ofNullable(totalWeight);
    }

    /**
     * Sets the quiver contents in the given stack to those of this component.
     *
     * @param stack ItemStack to set the contents for
     */
    public void setComponent(ItemStack stack) {
        if (isEmpty()) {
            stack.removeTagKey(COMPOUND_KEY);
            ((ItemStackDuck) (Object) stack).invexp$setCachedContents(null);
        } else {
            CompoundTag quiverContentsTag = new CompoundTag();

            // Write Quiver Items
            ListTag contentsTag = new ListTag();
            for (ItemStack nestedStack : getStacks()) {
                CompoundTag itemTag = new CompoundTag();
                nestedStack.save(itemTag);
                contentsTag.add(itemTag);
            }
            quiverContentsTag.put(ITEMS_KEY, contentsTag);

            // Write Selected Index
            quiverContentsTag.putInt(SELECTED_INDEX_KEY, getSelectedIndex());

            // Write Weight
            if (totalWeight != null) {
                CompoundTag totalWeightTag = new CompoundTag();
                totalWeightTag.putInt(NUMERATOR_KEY, totalWeight.getNumerator());
                totalWeightTag.putInt(DENOMINATOR_KEY, totalWeight.getDenominator());
                quiverContentsTag.put(TOTAL_WEIGHT_KEY, totalWeightTag);
            }

            CompoundTag tag = stack.getOrCreateTag();
            tag.put(COMPOUND_KEY, quiverContentsTag);
            ((ItemStackDuck) (Object) stack).invexp$setCachedContents(this);
        }
    }

    /**
     * Creates a QuiverContents component from the tags on a given stack.
     *
     * @param stack the quiver stack to get the component from
     * @return the quiver contents component attached to the stack
     */
    public static QuiverContents getComponent(ItemStack stack) {
        if (((ItemStackDuck) (Object) stack).invexp$getCachedContents() instanceof QuiverContents contents) {
            return contents;
        }

        // Ensure there is an existing Sack Tag
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(COMPOUND_KEY)) return new QuiverContents();

        CompoundTag quiverContentsTag = tag.getCompound(COMPOUND_KEY);

        // Read Quiver Items
        List<ItemStack> stacks = quiverContentsTag.getList(ITEMS_KEY, 10).stream()
                .map(CompoundTag.class::cast)
                .map(ItemStack::of)
                .toList();

        // Read Selected Index
        int selectedIndex = quiverContentsTag.getInt(SELECTED_INDEX_KEY);

        // Read Total Weight
        Fraction totalWeight = null;
        if (quiverContentsTag.contains(TOTAL_WEIGHT_KEY, 10)) {
            CompoundTag totalWeightTag = quiverContentsTag.getCompound(TOTAL_WEIGHT_KEY);
            try {
                totalWeight = Fraction.getFraction(
                        totalWeightTag.getInt(NUMERATOR_KEY),
                        totalWeightTag.getInt(DENOMINATOR_KEY));
            } catch (ArithmeticException e) {
                InventoryExpansion.LOGGER.error(
                        "Error creating Fraction: {} / {}",
                        totalWeightTag.getInt(NUMERATOR_KEY),
                        totalWeightTag.getInt(DENOMINATOR_KEY));
            }
        }

        QuiverContents contents = new QuiverContents(stacks, selectedIndex, totalWeight);
        //noinspection DataFlowIssue
        ((ItemStackDuck) (Object) stack).invexp$setCachedContents(contents);
        return contents;
    }

    /**
     * Checks if the item can have quiver contents attached.
     *
     * @param item the item to check
     * @return <code>true</code> if the item should have quiver contents; <code>false</code> otherwise
     */
    public static boolean hasQuiverContents(Item item) {
        return item instanceof QuiverItem;
    }
}
