package derekahedron.invexp.mixin;

import derekahedron.invexp.item.ItemStackDuck;
import derekahedron.invexp.item.bundle.BetterBundleItem;
import derekahedron.invexp.item.bundle.BundleContents;
import derekahedron.invexp.item.bundle.BundleContentsWriter;
import derekahedron.invexp.item.tooltip.BetterBundleTooltip;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(BundleItem.class)
public abstract class BundleItemMixin {

    @Shadow protected abstract void playInsertSound(Entity entity);

    @Shadow protected abstract void playRemoveOneSound(Entity entity);

    /**
     * Uses bundle contents to manage inserting/removing from a bundle.
     */
    @Inject(
            method = "overrideStackedOnOther",
            at = @At("HEAD"),
            cancellable = true)
    private void betterOverrideStackedOnOther(
            ItemStack stack,
            Slot slot,
            ClickAction action,
            Player player,
            CallbackInfoReturnable<Boolean> cir) {
        if (!BundleContents.hasBundleContents((BundleItem) (Object) this)) return;

        // Make sure this is actually a valid bundle
        BundleContentsWriter contents = BundleContentsWriter.of(stack);
        if (contents == null) {
            cir.setReturnValue(false);
            return;
        }

        ItemStack otherStack = slot.getItem();
        if (action == ClickAction.PRIMARY && !otherStack.isEmpty()) {
            if (!contents.canTryInsert(otherStack) || !slot.allowModification(player)) {
                // Don't do anything if the other stack cannot be inserted
                cir.setReturnValue(false);
                return;
            } else if (contents.add(slot, player) > 0) {
                // If added, play sound and update screen handler
                playInsertSound(player);
                InvExpUtil.onContentChanged(player);
            }
            cir.setReturnValue(true);
            return;
        } else if (action == ClickAction.SECONDARY && otherStack.isEmpty()) {

            if (contents.popSelectedStack(slot)) {
                // If removed, play sound and update handler
                playRemoveOneSound(player);
                InvExpUtil.onContentChanged(player);
            }
            // Always return true so bundle stays in cursor slot
            cir.setReturnValue(true);
            return;
        }

        cir.setReturnValue(false);
    }

    /**
     * Uses bundle contents to manage inserting/removing from a bundle.
     */
    @Inject(
            method = "overrideOtherStackedOnMe",
            at = @At("HEAD"),
            cancellable = true)
    private void betterOtherStackedOnMe(
            ItemStack stack,
            ItemStack other,
            Slot slot,
            ClickAction action,
            Player player,
            SlotAccess access,
            CallbackInfoReturnable<Boolean> cir) {
        if (!BundleContents.hasBundleContents((BundleItem) (Object) this)) return;

        // Make sure this is actually a valid bundle
        BundleContentsWriter contents = BundleContentsWriter.of(stack);
        if (contents == null) {
            cir.setReturnValue(false);
            return;
        }

        if (action == ClickAction.PRIMARY && !other.isEmpty()) {
            if (!contents.canTryInsert(other) || !slot.allowModification(player)) {
                // Don't do anything if the other stack cannot be inserted
                cir.setReturnValue(false);
                return;
            } else if (contents.add(other) > 0) {
                // If added, play sound and update screen handler
                playInsertSound(player);
                InvExpUtil.onContentChanged(player);
            }

            cir.setReturnValue(true);
            return;
        } else if (action == ClickAction.SECONDARY && other.isEmpty()) {
            if (slot.allowModification(player)) {
                ItemStack poppedStack = contents.popSelectedStack();

                if (!poppedStack.isEmpty()) {
                    // If removed, play sound and update handler
                    contents.setSelectedIndex(-1);
                    access.set(poppedStack);
                    playRemoveOneSound(player);
                    InvExpUtil.onContentChanged(player);
                }
                // Always return true so bundle stays in cursor slot
                cir.setReturnValue(true);
                return;
            }
        } else {
            contents.setSelectedIndex(-1);
        }

        cir.setReturnValue(false);
    }

    /**
     * Gets the proper bar color for the bundle.
     */
    @Inject(
            method = "getBarColor",
            at = @At("HEAD"),
            cancellable = true)
    private void betterGetBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (!BundleContents.hasBundleContents((BundleItem) (Object) this)) return;

        BundleContentsWriter contents = BundleContentsWriter.of(stack);

        if (contents != null && contents.getTotalWeight().compareTo(contents.getMaxWeight()) >= 0) {
            cir.setReturnValue(BetterBundleItem.FULL_ITEM_BAR_COLOR);
        }
    }

    /**
     * Gets the better tooltip for the bundle.
     */
    @SuppressWarnings("DataFlowIssue")
    @Inject(
            method = "getTooltipImage",
            at = @At("HEAD"),
            cancellable = true)
    private void betterGetTooltipImage(
            ItemStack stack,
            CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        if (!BundleContents.hasBundleContents((BundleItem) (Object) this)) return;

        BundleContentsWriter contents = BundleContentsWriter.of(stack);

        if (contents != null) {
            BundleItem self = (BundleItem) (Object) this;

            if (self instanceof BetterBundleItem betterBundleItem) {
                cir.setReturnValue(Optional.of(new BetterBundleTooltip(
                        contents,
                        betterBundleItem.getTooltipDescription(stack))));
            } else {
                cir.setReturnValue(Optional.of(new BetterBundleTooltip(contents, Optional.empty())));
            }
        } else {
            cir.setReturnValue(Optional.empty());
        }
    }

    /**
     * Clear cached contents when dropping.
     */
    @Inject(
            method = "dropContents",
            at = @At("RETURN"))
    private static void clearContents(ItemStack stack, Player player, CallbackInfoReturnable<Boolean> cir) {
        ((ItemStackDuck) (Object) stack).invexp$setCachedContents(null);
    }

    @Inject(
            method = "appendHoverText",
            at = @At("HEAD"),
            cancellable = true)
    private void betterAppendHoverText(
            ItemStack stack,
            Level level,
            List<Component> tooltipComponents,
            TooltipFlag isAdvanced,
            CallbackInfo ci) {
        if (!BundleContents.hasBundleContents((BundleItem) (Object) this)) return;
        ci.cancel();
    }

    @Inject(
            method = "getWeight",
            at = @At("HEAD"),
            cancellable = true)
    private static void getBetterWeight(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() instanceof BetterBundleItem) {
            cir.setReturnValue(BundleItemInvoker.invexp$callGetContentWeight(stack) + 4);
        }
    }
}
