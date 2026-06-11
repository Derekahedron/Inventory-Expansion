package derekahedron.invexp.client.gui;

import derekahedron.invexp.client.util.ClientServices;
import derekahedron.invexp.client.util.InvExpClientUtil;
import derekahedron.invexp.client.util.Scroller;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.mixin.client.AbstractContainerScreenAccessor;
import derekahedron.invexp.mixin.client.ScreenAccessor;
import derekahedron.invexp.network.SetSelectedIndexPacket;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;

/**
 * Holds callbacks for scroll events for Inventory Expansion.
 */
@SuppressWarnings("resource")
public class InvExpScrollEvents {

    public static final Scroller scroller = new Scroller();

    /**
     * Registers all scroll events.
     */
    public static void init() {
        ClientServices.SCROLL_EVENT_REGISTRAR.register((screen, mouseX, mouseY, scrollDelta) -> {
            Minecraft minecraft = ((ScreenAccessor) screen).invexp$getMinecraft();

            // Ensure screen is a handled screen and the player is not null
            if (!(screen instanceof AbstractContainerScreen<?> containerScreen)
                    || minecraft == null
                    || minecraft.player == null) {
                return true;
            }

            Player player = minecraft.player;

            Slot slot = ((AbstractContainerScreenAccessor) containerScreen).invexp$getHoveredSlot();
            if (slot == null
                    || !slot.hasItem()
                    || !slot.allowModification(player)) {
                return true;
            }
            ItemStack stack = slot.getItem();

            // Fail if contents are invalid
            ContainerItemContentsWriter contents = ContainerItemBehaviors.getContents(stack)
                    .filter(ContainerItemContentsWriter::canScroll)
                    .orElse(null);

            if (contents == null || contents.isEmpty()) return true;

            // Get amount scrolled. Use horizontal scroll if vertical is empty
            Vector2i scrollVector = scroller.update(-scrollDelta, 0);
            int numScrolled = scrollVector.y == 0 ? -scrollVector.x : scrollVector.y;
            if (numScrolled == 0) return false;

            // Scroll to the new index
            int newSelectedIndex = Scroller.scrollCycling(
                    numScrolled,
                    contents.getSelectedIndex(),
                    contents.getStacks().size());
            if (newSelectedIndex == contents.getSelectedIndex()) return false;

            // Creative screens do not have slot ids that are synced, so we must find the corresponding slot
            // in the creative inventory
            slot = InvExpClientUtil.getTrueSlot(slot, player);
            if (slot == null) return true;

            // If you can scroll, set index and send packet to server
            if (ContainerItemBehaviors.getInsertableContents(stack).isPresent()) {

                // Animate equip progress when changing stack
                for (InteractionHand hand : InteractionHand.values()) {
                    if (player.getItemInHand(hand) == stack) {
                        minecraft.gameRenderer.itemInHandRenderer.itemUsed(hand);
                    }
                }
            }

            contents.setSelectedIndex(newSelectedIndex);
            Services.NETWORK_HANDLER.send(new SetSelectedIndexPacket(slot.index, newSelectedIndex));
            return false;
        });
    }
}
