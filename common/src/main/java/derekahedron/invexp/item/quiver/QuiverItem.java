package derekahedron.invexp.item.quiver;

import derekahedron.invexp.item.tooltip.QuiverTooltip;
import derekahedron.invexp.sound.InvExpSoundEvents;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

/**
 * Quiver Item. Stores stacks of arrows in a QuiverContentsComponent.
 * Arrows can be added and removed by clicking the stack, and are also inserted
 * automatically on pickup. Arrows are used directly from the quiver's selected stack.
 */
public class QuiverItem extends Item {

    public static final int FULL_ITEM_BAR_COLOR = Mth.color(1.0F, 0.33F, 0.33F);
    public static final int ITEM_BAR_COLOR = Mth.color(0.44F, 0.53F, 1.0F);

    public QuiverItem(Properties properties) {
        super(properties);
    }

    /**
     * Gets if the given stack can be inserted into the stack based on the properties of the stack.
     *
     * @param contents the {@link QuiverContentsReader} of the sack
     * @param stack the {@link ItemStack} to test
     * @return if a stack can be tried to be inserted
     */
    @SuppressWarnings("unused")
    public boolean canTryInsert(QuiverContentsReader contents, ItemStack stack) {
        return stack.is(ItemTags.ARROWS);
    }

    /**
     * Gets how much the given stack weighs.
     *
     * @param contents the {@link QuiverContentsReader} of the quiver
     * @param stack the ItemStack to get the weight of
     * @return what fraction of a stack the given stack takes up
     */
    @SuppressWarnings("unused")
    public Fraction getWeight(QuiverContentsReader contents, ItemStack stack) {
        return Fraction.getFraction(1, stack.getMaxStackSize());
    }

    /**
     * Gets the maximum number of mixed stacks of arrows this quiver can hold.
     *
     * @param contents the {@link QuiverContentsReader} of the quiver
     * @return the number of mixed stacks of arrows this quiver can hold
     */
    @SuppressWarnings("unused")
    public Fraction getMaxWeight(QuiverContentsReader contents) {
        return Fraction.getFraction(8);
    }

    /**
     * Gets the maximum number of total separate stacks allowed in this quiver.
     *
     * @param contents the {@link QuiverContentsReader} of the quiver
     * @return the number of total stacks this quiver can hold
     */
    @SuppressWarnings("unused")
    public int getMaxStacks(QuiverContentsReader contents) {
        return 64;
    }

    @Override
    public boolean overrideStackedOnOther(
            ItemStack stack,
            Slot slot,
            ClickAction clickAction,
            Player player) {
        QuiverContentsWriter contents = QuiverContentsWriter.of(stack);
        ItemStack otherStack;

        // Make sure this is actually a valid quiver
        if (contents == null) return false;

        otherStack = slot.getItem();

        if (clickAction == ClickAction.PRIMARY && !otherStack.isEmpty()) {
            if (!contents.canTryInsert(otherStack) || !slot.allowModification(player)) {
                // Don't do anything if the other stack is not an arrow
                return false;
            } else if (contents.add(slot, player) > 0) {
                // If added, play sound and update screen handler
                playInsertSound(player);
                InvExpUtil.onContentChanged(player);
            }
            return true;
        } else if (clickAction == ClickAction.SECONDARY && otherStack.isEmpty()) {
            if (contents.popSelectedStack(slot)) {
                // If removed, play sound and update handler
                playRemoveSound(player);
                InvExpUtil.onContentChanged(player);
            }
            // Always return true so quiver stays in cursor slot
            return true;
        }
        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack stack,
            ItemStack otherStack,
            Slot slot,
            ClickAction clickAction,
            Player player,
            SlotAccess slotAccess) {
        QuiverContentsWriter contents = QuiverContentsWriter.of(stack);

        // Make sure this is actually a valid quiver
        if (contents == null) return false;

        if (clickAction == ClickAction.PRIMARY && !otherStack.isEmpty()) {
            if (!contents.canTryInsert(otherStack) || !slot.allowModification(player)) {
                // Don't do anything if the other stack is not an arrow
                return false;
            } else if (contents.add(otherStack) > 0) {
                // If added, play sound and update screen handler
                playInsertSound(player);
                InvExpUtil.onContentChanged(player);
            }
            return true;
        } else if (clickAction == ClickAction.SECONDARY && otherStack.isEmpty()) {
            if (slot.allowModification(player)) {
                ItemStack poppedStack = contents.popSelectedStack();

                if (!poppedStack.isEmpty()) {
                    // If removed, play sound and update handler
                    slotAccess.set(poppedStack);
                    playRemoveSound(player);
                    InvExpUtil.onContentChanged(player);
                }
                // Always return true so quiver stays in cursor slot
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        QuiverContentsWriter contents = QuiverContentsWriter.of(stack);
        return contents != null && !contents.isEmpty();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        QuiverContentsWriter contents = QuiverContentsWriter.of(stack);
        if (contents == null) {
            return 0;
        }
        Fraction fillFraction = contents.getFillFraction();
        return Math.min(
                13,
                1 + (fillFraction.getNumerator() * 12 / fillFraction.getDenominator())
        );
    }

    @Override
    public int getBarColor(ItemStack stack) {
        QuiverContentsWriter contents = QuiverContentsWriter.of(stack);
        if (contents == null || contents.getTotalWeight().compareTo(contents.getMaxWeight()) < 0) {
            return ITEM_BAR_COLOR;
        }
        else {
            return FULL_ITEM_BAR_COLOR;
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        QuiverContentsWriter contents = QuiverContentsWriter.of(stack);
        if (contents != null) {
            return Optional.of(new QuiverTooltip(contents, getTooltipDescription(stack)));
        }
        return Optional.empty();
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

    @Override
    public void onDestroyed(ItemEntity entity) {
        QuiverContentsWriter contents = QuiverContentsWriter.of(entity.getItem());

        if (contents == null || contents.isEmpty()) return;

        ItemUtils.onContainerDestroyed(entity, contents.popAllStacks().stream());
    }

    /**
     * Plays the quiver insert sound.
     *
     * @param entity the entity to play the insert sound from
     */
    public void playInsertSound(Entity entity) {
        entity.playSound(
                InvExpSoundEvents.ITEM_QUIVER_INSERT.get(),
                0.8F,
                Mth.lerp(entity.level().getRandom().nextFloat(), 0.8F, 1.2F));
    }

    /**
     * Plays the quiver remove sound.
     *
     * @param entity the entity to play the remove sound from
     */
    public void playRemoveSound(Entity entity) {
        entity.playSound(
                InvExpSoundEvents.ITEM_QUIVER_REMOVE_ONE.get(),
                0.8F,
                Mth.lerp(entity.level().getRandom().nextFloat(), 0.8F, 1.2F));
    }
}
