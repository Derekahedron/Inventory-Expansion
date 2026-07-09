package derekahedron.invexp.mixin.client;

import derekahedron.invexp.client.util.*;
import derekahedron.invexp.containeritem.ContainerItemContentsSelector;
import derekahedron.invexp.containeritem.UsableContents;
import derekahedron.invexp.item.sack.SackContentsWriter;
import derekahedron.invexp.item.sack.SackContentsReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@SuppressWarnings("ModifyVariableMayUseName")
@Mixin(Gui.class)
public class GuiMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    @Nullable
    private SackContentsReader invexp$openContents;

    /**
     * Draws a scissor area around a sack if it is open and selected. Also, stores the contents in a variable for
     * repeated accesses.
     */
    @Inject(
            method = "renderSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;getPopTime()I"))
    private void calculateSackContentsComponent(
            GuiGraphics guiGraphics,
            int x,
            int y,
            float partialTick,
            Player player,
            ItemStack stack,
            int seed,
            CallbackInfo ci) {
        invexp$openContents = null;
        if (stack != player.getMainHandItem() && stack != player.getOffhandItem()) return;

        SackContentsWriter contents = SackContentsWriter.of(stack);
        if (contents != null && !contents.isEmpty()) {
            invexp$openContents = contents;
            guiGraphics.enableScissor(x, y - 16, x + 16, y + 16);
        }
    }

    /**
     * Render the selected stack if there is a non-null contents component.
     * We do this instead of rendering the component via
     * the model as that would make the sack overlay affected by the
     * bobbing animation, which looks odd.
     */
    @ModifyArg(
            method = "renderSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;III)V"))
    private ItemStack renderSelectedStack(ItemStack stack) {
        return invexp$openContents == null
                ? stack
                : UsableContents.selectedStackOf(stack, minecraft.player);
    }

    /**
     * Renders the sack overlay and the count of the matching items in the sacks.
     */
    @Inject(
            method = "renderSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V"))
    private void renderSackCount(
            GuiGraphics guiGraphics,
            int x,
            int y,
            float partialTick,
            Player player,
            ItemStack stack,
            int seed,
            CallbackInfo ci) {
        if (invexp$openContents == null) return;

        // Close scissor area
        guiGraphics.disableScissor();

        OpenItemTextures.renderOpenItem(guiGraphics, stack, x, y, player.level(), player, seed);

        // Gather total count of nested items that match the selected stack
        ItemStack selectedStack = invexp$openContents.getSelectedStack();
        int maxCount = selectedStack.getMaxStackSize();
        int count = 0;
        for (ItemStack nestedStack : invexp$openContents.getStacks()) {
            if (ItemStack.isSameItemSameTags(nestedStack, selectedStack)) {
                count += nestedStack.getCount();

                if (count > maxCount) {
                    // If max count is surpassed, return early
                    break;
                }
            }
        }

        // Do not render for counts 1 and below
        if (count <= 1) return;

        // Render count
        String countLabel = InvExpClientUtil.getCountLabel(count, maxCount);
        Font font = minecraft.font;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0D, 0.0D, 200.0F);
        guiGraphics.drawString(
                font,
                countLabel,
                x + 19 - 2 - font.width(countLabel),
                y,
                0xFFFFFF,
                true);
        guiGraphics.pose().popPose();
    }

    /**
     * Updates the tracked variables to determine when a container item is being scrolled.
     */
    @Inject(
            method = "tick()V",
            at = @At("HEAD"))
    private void updateVars(CallbackInfo ci) {
        QuickSwapHandler.buttonPressed = InvExpKeyMappings.QUICK_SWAP.isDown();

        if (!QuickSwapHandler.buttonPressed) {
            QuickSwapHandler.blockScrolling = false;
        } else {
            InvExpKeyMappings.QUICK_SWAP.consumeClick();
        }
    }

    /**
     * Updates the hover stack to that of the stack being swapped in the container item.
     */
    @ModifyVariable(
            method = "tick()V",
            at = @At("STORE"),
            ordinal = 0)
    private ItemStack getSwappedHoverStack(ItemStack itemstack) {
        if (!QuickSwapHandler.isActive(minecraft.player)) return itemstack;

        return ContainerItemContentsSelector.getSelector(minecraft.player)
                .map(ContainerItemContentsSelector::getSelectedStack)
                .orElse(itemstack);
    }
}
