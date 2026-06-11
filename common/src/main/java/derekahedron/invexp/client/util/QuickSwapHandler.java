package derekahedron.invexp.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import derekahedron.invexp.containeritem.ContainerItemContentsSelector;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Client-side logic for the in-game arrow swap feature. While the swap arrow key is held and the
 * player holds a launcher with an available quiver, a small preview of the selected arrow is shown
 * above the selected hotbar slot and scrolling cycles the selected arrow.
 */
public class QuickSwapHandler {

    public static final ResourceLocation QUICK_SWAP_SLOT_TEXTURE =
            InvExpUtil.location("textures/gui/sprites/hud/quick_swap_slot.png");
    public static final ResourceLocation QUICK_SWAP_SLOTS_TEXTURE =
            InvExpUtil.location("textures/gui/sprites/hud/quick_swap_slots.png");
    public static final ResourceLocation QUICK_SWAP_SELECTOR_TEXTURE =
            InvExpUtil.location("textures/gui/sprites/hud/quick_swap_selector.png");

    public static boolean blockScrolling = false;
    public static boolean buttonPressed = false;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isActive(@Nullable LocalPlayer player) {
        return player != null
                && Minecraft.getInstance().screen == null
                && QuickSwapHandler.buttonPressed
                && !QuickSwapHandler.blockScrolling;
    }

    public static int getSelectedItemNameOffset(Minecraft minecraft) {
        if (!isActive(minecraft.player)) return 0;

        return ContainerItemContentsSelector.getSelector(minecraft.player)
                .filter(selector ->
                        selector.heldStack != selector.contents.getContainerStack()
                                && selector.heldStack != minecraft.player.getOffhandItem())
                .map(selector -> {
                    int offset = -2 - 3 - 16 - 3;
                    if (minecraft.gameMode != null && minecraft.gameMode.canHurtPlayer()) {
                        offset += 14;
                    }
                    return offset;
                })
                .orElse(0);
    }

    public static void render(GuiGraphics guiGraphics) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (!isActive(player)) return;

        ContainerItemContentsSelector.getSelector(player).ifPresent(selector -> {
            if (selector.heldStack == selector.contents.getContainerStack()) return;

            RenderSystem.enableBlend();
            ItemStack selectedStack = selector.getSelectedStack();
            int size = selector.compressedStacks.size();
            int centerX = guiGraphics.guiWidth() / 2;
            int bottom = guiGraphics.guiHeight();
            int slotX = selector.heldStack == player.getOffhandItem()
                    ? player.getMainArm() == HumanoidArm.RIGHT
                      ? centerX - 91 - 7 - 3 - 16 - 3
                      : centerX + 91 + 7
                    : centerX - 91 + player.getInventory().selected * 20;
            int slotY = bottom - 3 - 16 - 3 - 2 - 3 - 16 - 3;

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 60.0F);
            if (size <= 1) {
                guiGraphics.blit(QUICK_SWAP_SLOT_TEXTURE, slotX, slotY, 0, 0, 22, 22, 22, 22);
            } else {
                guiGraphics.blit(QUICK_SWAP_SLOTS_TEXTURE, slotX - 20, slotY, 0, 0, 62, 22, 62, 22);
            }
            guiGraphics.blit(QUICK_SWAP_SELECTOR_TEXTURE, slotX - 1, slotY - 1, 0, 0, 24, 24, 24, 24);
            guiGraphics.pose().popPose();

            // Render the selected arrow icon.
            Font font = Minecraft.getInstance().font;
            guiGraphics.renderItem(player, selectedStack, slotX + 3, slotY + 3, selector.selectedIndex);
            guiGraphics.renderItemDecorations(font, selectedStack, slotX + 3, slotY + 3, InvExpClientUtil.getCountLabel(selectedStack.getCount(), 999));

            if (size > 1) {
                int prevIndex = (selector.selectedIndex - 1 + size) % size;
                ItemStack prevStack = selector.compressedStacks.get(prevIndex);
                guiGraphics.renderItem(player, prevStack, slotX - 20 + 3, slotY + 3, prevIndex);
                guiGraphics.renderItemDecorations(font, prevStack, slotX - 20 + 3, slotY + 3, InvExpClientUtil.getCountLabel(prevStack.getCount(), 999));

                int nextIndex = (selector.selectedIndex + 1) % size;
                ItemStack nextStack = selector.compressedStacks.get(nextIndex);
                guiGraphics.renderItem(player, nextStack, slotX + 20 + 3, slotY + 3, nextIndex);
                guiGraphics.renderItemDecorations(font, nextStack, slotX + 20 + 3, slotY + 3, InvExpClientUtil.getCountLabel(nextStack.getCount(), 999));
            }
            RenderSystem.disableBlend();
        });
    }
}
