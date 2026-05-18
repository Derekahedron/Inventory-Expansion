package derekahedron.invexp.item.bundle;

import derekahedron.invexp.mixin.BundleItemInvoker;
import derekahedron.invexp.util.OpenItemTexturesRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

public class BetterBundleItem extends BundleItem {

    public static final int FULL_ITEM_BAR_COLOR = Mth.color(1.0F, 0.33F, 0.33F);

    public BetterBundleItem(Properties properties) {
        super(properties);
        OpenItemTexturesRegistry.addItem(this);
    }

    /**
     * Gets how much the given stack weighs.
     *
     * @param self the ItemStack that contains the bundle
     * @param stack the ItemStack to get the weight of
     * @return what fraction of a stack the given stack takes up
     */
    @SuppressWarnings("unused")
    public Fraction getWeight(ItemStack self, ItemStack stack) {
        return Fraction.getFraction(BundleItemInvoker.invexp$callGetWeight(stack), 64);
    }

    /**
     * Gets the maximum number of mixed stacks this bundle can hold.
     *
     * @param self the ItemStack that contains the bundle
     * @return the number of mixed stacks this bundle can hold
     */
    @SuppressWarnings("unused")
    public Fraction getMaxWeight(ItemStack self) {
        return Fraction.ONE;
    }

    /**
     * Gets the maximum number of total separate stacks allowed in this bundle.
     *
     * @param self the ItemStack that contains the bundle
     * @return the number of total stacks this bundle can hold
     */
    @SuppressWarnings("unused")
    public int getMaxStacks(ItemStack self) {
        return 64;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player) {
            BundleContentsWriter contents = BundleContentsWriter.of(stack);
            if (contents == null) return;
            contents.validate(player);
        }
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
