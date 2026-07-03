package derekahedron.invexp.item.bundle;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.containeritem.ContainerItemContents;
import derekahedron.invexp.item.ItemStackDuck;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Holds the raw bundle contents attached to a bundle via nbt. We rely on the actual bundle item to get the full
 * information, like max weight, so this only hold info related to the nbt itself.
 */
public class BundleContents implements ContainerItemContents {

    public static final String ITEMS_KEY = "Items";
    public static final String COMPOUND_KEY = "InvExpBundleContents";
    public static final String SELECTED_INDEX_KEY = "InvExpSelectedIndex";
    public static final String TOTAL_WEIGHT_KEY = "InvExpTotalWeight";
    public static final String NUMERATOR_KEY = "Numerator";
    public static final String DENOMINATOR_KEY = "Denominator";

    public final List<ItemStack> stacks;
    public final int selectedIndex;
    @Nullable
    public final Fraction totalWeight;

    public BundleContents(
            List<ItemStack> stacks,
            int selectedIndex,
            @Nullable Fraction totalWeight) {
        this.stacks = stacks;
        this.selectedIndex = selectedIndex;
        this.totalWeight = totalWeight;
    }

    public BundleContents() {
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
     * Sets the bundle contents in the given stack to those of this component.
     *
     * @param stack ItemStack to set the contents for
     */
    public void setComponent(ItemStack stack) {
        if (isEmpty()) {
            stack.removeTagKey(ITEMS_KEY);
            stack.removeTagKey(COMPOUND_KEY);
            ((ItemStackDuck) (Object) stack).invexp$setCachedContents(null);
        } else {
            CompoundTag tag = stack.getOrCreateTag();
            CompoundTag bundleContentsTag = new CompoundTag();

            // Write Bundle Items
            ListTag contentsTag = new ListTag();
            for (ItemStack nestedStack : getStacks()) {
                CompoundTag itemTag = new CompoundTag();
                nestedStack.save(itemTag);
                contentsTag.add(itemTag);
            }
            tag.put(ITEMS_KEY, contentsTag);

            // Write Selected Index
            if (getSelectedIndex() != -1) {
                bundleContentsTag.putInt(SELECTED_INDEX_KEY, getSelectedIndex());
            }

            // Write Total Weight
            if (totalWeight != null) {
                CompoundTag totalWeightTag = new CompoundTag();
                totalWeightTag.putInt(NUMERATOR_KEY, totalWeight.getNumerator());
                totalWeightTag.putInt(DENOMINATOR_KEY, totalWeight.getDenominator());
                bundleContentsTag.put(TOTAL_WEIGHT_KEY, totalWeightTag);
            }

            tag.put(COMPOUND_KEY, bundleContentsTag);
            ((ItemStackDuck) (Object) stack).invexp$setCachedContents(this);
        }
    }

    /**
     * Creates a BundleContents component from the tags on a given stack.
     *
     * @param stack the bundle stack to get the component from
     * @return the bundle contents component attached to the stack
     */
    public static BundleContents getComponent(ItemStack stack) {
        if (((ItemStackDuck) (Object) stack).invexp$getCachedContents() instanceof BundleContents contents) {
            return contents;
        }

        CompoundTag tag = stack.getTag();

        // Ensure there is an existing Bundle Tag
        if (tag == null || !tag.contains(ITEMS_KEY)) return new BundleContents();

        // Read Bundle Items
        List<ItemStack> stacks = tag.getList(ITEMS_KEY, 10).stream()
                .map(CompoundTag.class::cast)
                .map(ItemStack::of)
                .toList();

        int selectedIndex = -1;
        Fraction totalWeight = null;

        if (tag.contains(COMPOUND_KEY)) {
            CompoundTag bundleContentsTag = tag.getCompound(COMPOUND_KEY);

            // Read Selected Index
            if (bundleContentsTag.contains(SELECTED_INDEX_KEY)) {
                selectedIndex = bundleContentsTag.getInt(SELECTED_INDEX_KEY);
            }

            // Read Total Weight
            if (bundleContentsTag.contains(TOTAL_WEIGHT_KEY, 10)) {
                CompoundTag totalWeightTag = bundleContentsTag.getCompound(TOTAL_WEIGHT_KEY);
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
        }

        BundleContents contents = new BundleContents(stacks, selectedIndex, totalWeight);
        //noinspection DataFlowIssue
        ((ItemStackDuck) (Object) stack).invexp$setCachedContents(contents);
        return contents;
    }

    /**
     * Checks if the item can have bundle contents attached.
     *
     * @param item the item to check
     * @return <code>true</code> if the item should have bundle contents; <code>false</code> otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean hasBundleContents(Item item) {
        return item == Items.BUNDLE || item instanceof BetterBundleItem;
    }
}
