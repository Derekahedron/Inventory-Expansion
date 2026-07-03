package derekahedron.invexp.item.bundle;

import derekahedron.invexp.mixin.BundleItemInvoker;
import derekahedron.invexp.util.OpenItemTexturesRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

public class BetterBundleItem extends BundleItem {

    public static final int FULL_ITEM_BAR_COLOR = Mth.color(1.0F, 0.33F, 0.33F);

    public BetterBundleItem(Properties properties) {
        super(properties);
        OpenItemTexturesRegistry.addItem(this);
    }

    /**
     * Gets if the given stack can be inserted into the stack based on the properties of the stack.
     *
     * @param contents the {@link BundleContentsReader} of the sack
     * @param stack the {@link ItemStack} to test
     * @return if a stack can be tried to be inserted
     */
    @SuppressWarnings("unused")
    public boolean canTryInsert(BundleContentsReader contents, ItemStack stack) {
        return true;
    }

    /**
     * Gets how much the given stack weighs.
     *
     * @param contents the {@link BundleContentsReader} of the bundle
     * @param stack the ItemStack to get the weight of
     * @return what fraction of a stack the given stack takes up
     */
    @SuppressWarnings("unused")
    public Fraction getWeight(BundleContentsReader contents, ItemStack stack) {
        return Fraction.getFraction(BundleItemInvoker.invexp$callGetWeight(stack), 64);
    }

    /**
     * Gets the maximum number of mixed stacks this bundle can hold.
     *
     * @param contents the {@link BundleContentsReader} of the bundle
     * @return the number of mixed stacks this bundle can hold
     */
    @SuppressWarnings("unused")
    public Fraction getMaxWeight(BundleContentsReader contents) {
        return Fraction.ONE;
    }

    /**
     * Gets the maximum number of total separate stacks allowed in this bundle.
     *
     * @param contents the {@link BundleContentsReader} of the bundle
     * @return the number of total stacks this bundle can hold
     */
    @SuppressWarnings("unused")
    public int getMaxStacks(BundleContentsReader contents) {
        return 64;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        BundleContentsWriter contents = BundleContentsWriter.of(stack);
        if (contents == null) return 0;

        Fraction fillFraction = contents.getFillFraction();
        return Math.min(
                13,
                1 + (fillFraction.getNumerator() * 12 / fillFraction.getDenominator()));
    }

    /**
     * Gets an additional tooltip text to be appended to the tooltip.
     *
     * @param stack the stack being viewed
     * @return an optional tooltip text to be added
     */
    @SuppressWarnings("unused")
    public Optional<Component> getTooltipDescription(ItemStack stack) {
        return Optional.empty();
    }
}
