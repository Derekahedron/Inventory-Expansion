package derekahedron.invexp.network;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.containeritem.ContainerItemContentsSelector;
import derekahedron.invexp.platform.services.IPacketRegistrar;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet for sending a quick swap to the server.
 * Note that this doesn't send what slot and assumes that the server and client are in-sync.
 *
 * @param selectedIndex the new selected index to set
 */
public record QuickSwapSelectedIndexPacket(int selectedIndex) implements NetworkPacket {

    public static final ResourceLocation ID = InvExpUtil.location("quick_swap_selected_index");

    /**
     * Reads a new packet from a given byte buffer
     *
     * @param buffer the byte buffer to reach the packet from
     */
    public QuickSwapSelectedIndexPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(selectedIndex);
    }

    /**
     * Handles the packet by setting the selected index to the given index
     *
     * @param context the packet context
     */
    public void handle(IPacketRegistrar.C2SPacketContext context) {
        ServerPlayer sender = context.player();

        if (sender == null) {
            InventoryExpansion.LOGGER.debug(
                    "Received Quick Swap Selected Index packet with no sender!");
            return;
        }

        ContainerItemContentsSelector.getSelector(sender).ifPresent(selector -> {
            if (selectedIndex >= selector.contents.getStacks().size() || selectedIndex < -1) {
                InventoryExpansion.LOGGER.debug(
                        "Player {} set an invalid selected index of {}", sender, selectedIndex);
                return;
            }

            selector.contents.setSelectedIndex(selectedIndex);
        });
    }
}
