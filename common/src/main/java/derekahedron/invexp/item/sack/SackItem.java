package derekahedron.invexp.item.sack;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.item.tooltip.SackTooltip;
import derekahedron.invexp.sound.InvExpSoundEvents;
import derekahedron.invexp.util.InvExpUtil;
import derekahedron.invexp.util.OpenItemTexturesRegistry;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Stores stacks of items in a SackContentsComponent.
 * Items can be used directly from the sack and are automatically inserted into the sack
 * on pickup. Items must be of the same sack type in order to be added.
 */
public class SackItem extends Item {

    public static final int FULL_ITEM_BAR_COLOR = Mth.color(1.0F, 0.33F, 0.33F);
    public static final int ITEM_BAR_COLOR = Mth.color(0.44F, 0.53F, 1.0F);

    public SackItem(Properties properties) {
        super(properties);
        OpenItemTexturesRegistry.addItem(this);
    }

    /**
     * Gets the max sack types that this sack can hold.
     *
     * @param self the ItemStack that contains the sack
     * @return the number of types this sack can hold
     */
    @SuppressWarnings("unused")
    public int getMaxSackTypes(ItemStack self) {
        return 1;
    }

    /**
     * Gets the max weight that this sack can hold.
     *
     * @param self the ItemStack that contains the sack
     * @return the max weight this sack can hold
     */
    @SuppressWarnings("unused")
    public Fraction getMaxWeight(ItemStack self) {
        return Fraction.getFraction(4);
    }

    /**
     * Gets the type that the given ItemStack should have when in the sack.
     *
     * @param self the ItemStack that contains the sack
     * @param stack the stack to get the type for
     * @return the SackType that the given stack should have; <code>null</code> if there is none
     */
    @Nullable
    @SuppressWarnings("unused")
    public String getSackType(ItemStack self, ItemStack stack) {
        // Fail if manager is not created before running this
        if (SackRuleManager.getInstance() == null) {
            InventoryExpansion.LOGGER.error(
                    "Tried to access type before the Sack Rule Manager has been initialized! Please report this to the developers.");
            return null;
        }

        return SackRuleManager.getInstance().getType(stack)
                .map(sackType -> sackType.location().toString())
                .orElse(null);
    }

    /**
     * Gets the weight that the given ItemStack should use when in the sack.
     *
     * @param self the ItemStack that contains the sack
     * @param stack the stack to get the weight for
     * @return the weight that the given stack should have
     */
    @SuppressWarnings("unused")
    public Fraction getWeight(ItemStack self, ItemStack stack) {
        // Fail if manager is not created before running this
        if (SackRuleManager.getInstance() == null) {
            InventoryExpansion.LOGGER.error(
                    "Tried to access weight before the Sack Rule Manager has been initialized! Please report this to the developers.");
            return Fraction.ONE;
        }

        return SackRuleManager.getInstance().getWeight(stack)
                .divideBy(Fraction.getFraction(stack.getMaxStackSize()));
    }

    /**
     * Gets the maximum number of total separate stacks allowed in this sack.
     *
     * @param self the ItemStack that contains the sack
     * @return the number of total stacks this sack can hold
     */
    @SuppressWarnings("unused")
    public int getMaxStacks(ItemStack self) {
        return 64;
    }

    @Override
    public boolean overrideStackedOnOther(
            ItemStack stack,
            Slot slot,
            ClickAction clickAction,
            Player player) {
        SackContentsWriter contents = SackContentsWriter.of(stack);
        ItemStack otherStack;

        // Make sure this is actually a valid sack
        if (contents == null) return false;
        otherStack = slot.getItem();

        if (clickAction == ClickAction.PRIMARY && !otherStack.isEmpty()) {
            if (!contents.canTryInsert(otherStack) || !slot.allowModification(player)) {
                // Don't do anything if the other stack does not match the types
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
            // Always return true so sack stays in cursor slot
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
        SackContentsWriter contents = SackContentsWriter.of(stack);

        // Make sure this is actually a valid sack
        if (contents == null) return false;

        if (clickAction == ClickAction.PRIMARY && !otherStack.isEmpty()) {
            if (!contents.canTryInsert(otherStack) || !slot.allowModification(player)) {
                // Don't do anything if the other stack does not match the types
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
                // Always return true so sack stays in cursor slot
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        SackContentsReader contents = SackContentsWriter.of(stack);
        return contents != null && !contents.isEmpty();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        SackContentsReader contents = SackContentsWriter.of(stack);
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
    public int getBarColor(ItemStack sackStack) {
        SackContentsReader contents = SackContentsWriter.of(sackStack);
        if (contents == null || contents.getTotalWeight().compareTo(contents.getMaxWeight()) < 0) {
            return ITEM_BAR_COLOR;
        } else {
            return FULL_ITEM_BAR_COLOR;
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack sackStack) {
        SackContentsReader contents = SackContentsWriter.of(sackStack);
        if (contents != null) {
            return Optional.of(new SackTooltip(contents, getTooltipDescription(sackStack)));
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
        SackContentsWriter contents = SackContentsWriter.of(entity.getItem());

        if (contents == null || contents.isEmpty()) return;

        ItemUtils.onContainerDestroyed(entity, contents.popAllStacks().stream());
    }

    @Override
    public void inventoryTick(ItemStack sackStack, Level level, Entity entity, int slot, boolean selected) {
        if (entity instanceof Player player) {
            SackContentsWriter contents = SackContentsWriter.of(sackStack);
            if (contents == null) return;
            contents.validate(player);

            // After contents are validated, try ticking the selected stack
            if (contents.isEmpty()) return;

            ItemStack selectedStack = contents.copySelectedStack();
            selectedStack.getItem().inventoryTick(selectedStack, level, entity, slot, selected);
            contents.updateSelectedStack(selectedStack, leftoverStack -> {
                if (!player.getInventory().add(leftoverStack)) {
                    player.drop(leftoverStack, false);
                }
            });
        }
    }

    /**
     * Plays the sack insert sound.
     *
     * @param entity the entity to play the insert sound from
     */
    public void playInsertSound(Entity entity) {
        entity.playSound(
                InvExpSoundEvents.ITEM_SACK_INSERT.get(),
                0.8F,
                Mth.lerp(entity.level().getRandom().nextFloat(), 0.8F, 1.2F));
    }

    /**
     * Plays the sack remove sound.
     *
     * @param entity the entity to play the remove sound from
     */
    public void playRemoveSound(Entity entity) {
        entity.playSound(
                InvExpSoundEvents.ITEM_SACK_REMOVE_ONE.get(),
                0.8F,
                Mth.lerp(entity.level().getRandom().nextFloat(), 0.8F, 1.2F));
    }
}
