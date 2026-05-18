package derekahedron.invexp.item.sack;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.containeritem.ContainerItemContents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Holds the raw sack contents attached to a sack via nbt. We rely on the actual sack item to get the full
 * information, like max weight, so this only hold info related to the nbt itself.
 */
public class SackContents implements ContainerItemContents {

    public static final String COMPOUND_KEY = "InvExpSackContents";
    public static final String TYPES_KEY = "Types";
    public static final String ITEMS_KEY = "Items";
    public static final String SELECTED_INDEX_KEY = "SelectedIndex";
    public static final String TOTAL_WEIGHT_KEY = "TotalWeight";
    public static final String NUMERATOR_KEY = "Numerator";
    public static final String DENOMINATOR_KEY = "Denominator";

    public final List<String> sackTypes;
    public final List<ItemStack> stacks;
    public final int selectedIndex;
    @Nullable
    public final Fraction totalWeight;

    public SackContents(
            List<String> sackTypes,
            List<ItemStack> stacks,
            int selectedIndex,
            @Nullable Fraction totalWeight) {
        this.sackTypes = sackTypes;
        this.stacks = stacks;
        this.selectedIndex = selectedIndex;
        this.totalWeight = totalWeight;
    }

    public SackContents() {
        this(List.of(), List.of(), -1, Fraction.ZERO);
    }

    /**
     * Gets all sack types used in the sack.
     *
     * @return a list of sack types in the sack
     */
    public List<String> getSackTypes() {
        return sackTypes;
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
     * Sets the sack contents in the given stack to those of this component.
     *
     * @param stack ItemStack to set the contents for
     */
    public void setComponent(ItemStack stack) {
        if (isEmpty()) {
            stack.removeTagKey(COMPOUND_KEY);
        } else {
            CompoundTag sackContentsTag = new CompoundTag();

            // Write Sack Types
            ListTag typesTag = new ListTag();
            for (String sackType : getSackTypes()) {
                typesTag.add(StringTag.valueOf(sackType));
            }
            sackContentsTag.put(TYPES_KEY, typesTag);

            // Write Sack Items
            ListTag contentsTag = new ListTag();
            for (ItemStack nestedStack : getStacks()) {
                CompoundTag itemTag = new CompoundTag();
                nestedStack.save(itemTag);
                contentsTag.add(itemTag);
            }
            sackContentsTag.put(ITEMS_KEY, contentsTag);

            // Write Selected Index
            sackContentsTag.putInt(SELECTED_INDEX_KEY, getSelectedIndex());

            // Write Weight
            if (totalWeight != null) {
                CompoundTag totalWeightTag = new CompoundTag();
                totalWeightTag.putInt(NUMERATOR_KEY, totalWeight.getNumerator());
                totalWeightTag.putInt(DENOMINATOR_KEY, totalWeight.getDenominator());
                sackContentsTag.put(TOTAL_WEIGHT_KEY, totalWeightTag);
            }

            CompoundTag tag = stack.getOrCreateTag();
            tag.put(COMPOUND_KEY, sackContentsTag);
        }
    }

    /**
     * Creates a SackContents component from the tags on a given stack.
     *
     * @param stack the sack stack to get the component from
     * @return the sack contents component attached to the stack
     */
    public static SackContents getComponent(ItemStack stack) {
        // Ensure there is an existing Sack Tag
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(COMPOUND_KEY)) return new SackContents();

        CompoundTag sackContentsTag = tag.getCompound(COMPOUND_KEY);

        // Read Sack Types
        List<String> sackTypes = sackContentsTag.getList(TYPES_KEY, 8).stream()
                .map(Tag::getAsString)
                .toList();

        // Read Sack Items
        List<ItemStack> stacks = sackContentsTag.getList(ITEMS_KEY, 10).stream()
                .map(CompoundTag.class::cast)
                .map(ItemStack::of)
                .toList();

        // Read Selected Index
        int selectedIndex = sackContentsTag.getInt(SELECTED_INDEX_KEY);

        // Read Total Weight
        Fraction totalWeight = null;
        if (sackContentsTag.contains(TOTAL_WEIGHT_KEY, 10)) {
            CompoundTag totalWeightTag = sackContentsTag.getCompound(TOTAL_WEIGHT_KEY);
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

        return new SackContents(sackTypes, stacks, selectedIndex, totalWeight);
    }

    /**
     * Checks if the item can have sack contents attached.
     *
     * @param item the item to check
     * @return <code>true</code> if the item should have sack contents; <code>false</code> otherwise
     */
    public static boolean hasSackContents(Item item) {
        return item instanceof SackItem;
    }
}
