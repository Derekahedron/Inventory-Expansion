package derekahedron.invexp.mixin.client;

import derekahedron.invexp.client.util.ContainerItemSlotDragger;
import derekahedron.invexp.client.util.InvExpClientUtil;
import derekahedron.invexp.client.util.OpenItemTextures;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.item.bundle.BundleContentsWriter;
import derekahedron.invexp.item.sack.SackContentsWriter;
import derekahedron.invexp.item.sack.SackContentsReader;
import derekahedron.invexp.item.tooltip.StickyTooltipComponent;
import derekahedron.invexp.network.SetSelectedIndexPacket;
import derekahedron.invexp.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("resource")
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Shadow @Final protected Set<Slot> quickCraftSlots;

    @Shadow protected boolean isQuickCrafting;

    @Shadow @Nullable private Slot clickedSlot;

    @Shadow protected abstract List<Component> getTooltipFromContainerItem(ItemStack stack);

    @Shadow protected int imageWidth;

    @Shadow protected abstract boolean isHovering(Slot slot, double mouseX, double mouseY);

    @Shadow @Nullable protected Slot hoveredSlot;

    @Shadow
    private int quickCraftingType;
    @Unique
    @Nullable
    private Slot invexp$currentSlot;
    @Unique
    private boolean invexp$hasMoved;
    @Unique
    @Nullable
    private SackContentsReader invexp$openContents;
    @Unique
    private int invexp$mouseX;
    @Unique
    private int invexp$mouseY;
    @Unique
    @Nullable
    private Slot invexp$hoveredBundleSlot;


    /**
     * Starts dragging a container item.
     */
    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"))
    private void startDraggingContainer(
            double mouseX,
            double mouseY,
            int button,
            CallbackInfoReturnable<Boolean> cir) {
        AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;

        if (invexp$getDragger(self.getMenu().getCarried()).isPresent()) {
            invexp$currentSlot = hoveredSlot;
            invexp$hasMoved = false;
        }
    }

    /**
     * Handles hovering when dragging a container item.
     */
    @Inject(
            method = "mouseDragged",
            at = @At("HEAD"),
            cancellable = true)
    private void dragContainer(
            double mouseX,
            double mouseY,
            int button,
            double dragX,
            double dragY,
            CallbackInfoReturnable<Boolean> cir) {
        AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;
        Minecraft minecraft = ((ScreenAccessor) self).invexp$getMinecraft();

        // Vanilla checks for dragging
        if (quickCraftingType == 2 ||
                !isQuickCrafting ||
                clickedSlot != null ||
                minecraft == null ||
                minecraft.options.touchscreen().get()) {
            return;
        }

        // Make sure there is a valid slot
        if (hoveredSlot == null) return;

        // Check that there is a valid dragger
        ItemStack cursorStack = self.getMenu().getCarried();

        ContainerItemSlotDragger dragger = invexp$getDragger(cursorStack).orElse(null);
        if (dragger == null) return;

        // If current slot is not set, update current slot
        if (invexp$currentSlot == null) {
            invexp$currentSlot = hoveredSlot;
        } else if (invexp$currentSlot != hoveredSlot) {
            if (!invexp$hasMoved) {
                invexp$hasMoved = true;
                dragger.onHover(invexp$currentSlot, self);
            }
            invexp$currentSlot = hoveredSlot;
            dragger.onHover(invexp$currentSlot, self);
            quickCraftSlots.clear();
        }
        cir.setReturnValue(true);
        cir.cancel();
    }

    /**
     * Resets values when stopping dragging the container item.
     */
    @Inject(
            method = "mouseReleased",
            at = @At("HEAD"),
            cancellable = true)
    private void finishDraggingContainer(
            double mouseX,
            double mouseY,
            int button,
            CallbackInfoReturnable<Boolean> cir) {
        AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;
        if (invexp$hasMoved && invexp$getDragger(self.getMenu().getCarried()).isPresent()) {
            invexp$hasMoved = false;
            invexp$currentSlot = null;
            isQuickCrafting = false;
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    /**
     * Displays the tooltip of the container item that an item is being hovered over.
     */
    @Inject(
            method = "renderTooltip",
            at = @At("HEAD"),
            cancellable = true)
    private void renderContainerTooltip(GuiGraphics guiGraphics, int x, int y, CallbackInfo ci) {
        AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;

        if (!self.getMenu().getCarried().isEmpty()
                && hoveredSlot != null
                && hoveredSlot.hasItem()) {
            ItemStack hoverStack = hoveredSlot.getItem();

            hoverStack.getTooltipImage()
                    .filter(component -> component instanceof StickyTooltipComponent)
                    .ifPresent(component -> {
                        guiGraphics.renderTooltip(
                                ((ScreenAccessor) self).invexp$getFont(),
                                getTooltipFromContainerItem(hoverStack),
                                Optional.of(component),
                                x,
                                y);
                        ci.cancel();
                    });
        }
    }

    /**
     * Tracks the latest mouse position so we can tell when items are being hovered over.
     */
    @Inject(
            method = "render",
            at = @At("HEAD"))
    private void saveMousePos(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci) {
        invexp$mouseX = mouseX;
        invexp$mouseY = mouseY;
    }

    /**
     * Draws the open Sack item when hovered
     */
    @Inject(
            method = "renderSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V"))
    private void drawOpenSack(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;
        Minecraft minecraft = ((ScreenAccessor) self).invexp$getMinecraft();
        invexp$openContents = null;

        if (!slot.hasItem()
                || !isHovering(slot, invexp$mouseX, invexp$mouseY)
                || minecraft == null
                || minecraft.player == null
                || !slot.allowModification(minecraft.player)
                || !self.getMenu().canDragTo(slot)) {
            return;
        }

        ItemStack cursorStack = self.getMenu().getCarried();
        SackContentsReader contents = SackContentsWriter.of(slot.getItem());

        if (contents == null
                || contents.isEmpty()
                || (!cursorStack.isEmpty()
                && !contents.canTryInsert(cursorStack))) {
            return;
        }

        invexp$openContents = contents;
        Player player = minecraft.player;
        Level level = player != null ? player.level() : null;
        OpenItemTextures.renderOpenItem(
                guiGraphics,
                slot.getItem(),
                slot.x,
                slot.y,
                level,
                player,
                slot.x + slot.y * imageWidth);
    }

    /**
     * Renders the selected stack when there are open contents. Used when hovering over a sack.
     */
    @ModifyArg(
            method = "renderSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;III)V"))
    private ItemStack renderSelectedItem(ItemStack stack) {
        if (invexp$openContents != null) {
            ItemStack selectedStack = invexp$openContents.getSelectedStack();
            invexp$openContents = null;
            return selectedStack;
        } else {
            return stack;
        }
    }

    /**
     * Tracks the
     */
    @Inject(
            method = "render",
            at = @At("RETURN"))
    private void trackBundleSlot(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        invexp$setHoveredSlot(hoveredSlot);
    }

    /**
     * Clears the hovered bundle slot when the screen closes.
     */
    @Inject(
            method = "onClose",
            at = @At("HEAD"))
    private void closeBundle(CallbackInfo ci) {
        invexp$setHoveredSlot(null);
    }

    @Unique
    private void invexp$setHoveredSlot(@Nullable Slot newHoveredSlot) {
        if (newHoveredSlot == invexp$hoveredBundleSlot) return;

        AbstractContainerScreen<?> self = (AbstractContainerScreen<?>) (Object) this;
        Minecraft minecraft = ((ScreenAccessor) self).invexp$getMinecraft();

        if (minecraft == null) return;

        if (invexp$hoveredBundleSlot != null) {
            BundleContentsWriter contents = BundleContentsWriter.of(invexp$hoveredBundleSlot.getItem());

            if (contents != null && !contents.isEmpty() && contents.getSelectedIndex() != -1) {
                contents.setSelectedIndex(-1);
                Slot trueSlot = InvExpClientUtil.getTrueSlot(
                        invexp$hoveredBundleSlot,
                        minecraft.player);

                if (trueSlot != null) {
                    Services.NETWORK_HANDLER.send(new SetSelectedIndexPacket(trueSlot.index, -1));
                }
            }
        }

        invexp$hoveredBundleSlot = newHoveredSlot != null && newHoveredSlot.getItem().getItem() instanceof BundleItem
                ? newHoveredSlot
                : null;
    }

    @Unique
    private static Optional<ContainerItemSlotDragger> invexp$getDragger(ItemStack stack) {
        return ContainerItemBehaviors.getContents(stack).map(contents ->
                new ContainerItemSlotDragger() {

                @Override
                public boolean isEmpty() {
                    return contents.isEmpty();
                }

                @Override
                public boolean canTryInsert(ItemStack stack) {
                    return contents.canTryInsert(stack);
                }
            });
    }
}

