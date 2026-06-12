package derekahedron.invexp.network;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.platform.services.IPacketRegistrar;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record SetSelectedIndexPacket(int slotId, int selectedIndex) implements NetworkPacket {

    public static final ResourceLocation ID = InvExpUtil.location("set_selected_index");

    public SetSelectedIndexPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readInt());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(slotId);
        buf.writeInt(selectedIndex);
    }

    /**
     * Handles the selected index packet that sets the selected index for a given players
     * container item.
     *
     * @param context the packet context containing the sending player
     */
    public void handle(IPacketRegistrar.C2SPacketContext context) {
        ServerPlayer sender = context.player();

        if (sender == null) {
            InventoryExpansion.LOGGER.debug(
                    "Received Set Selected Index packet with no sender!");
            return;
        }

        if (slotId < 0 || slotId >= sender.containerMenu.slots.size()) {
            InventoryExpansion.LOGGER.debug(
                    "Player {} set selected index of invalid slot id {}", sender, slotId);
            return;
        }

        // Makes sure the container is valid
        ItemStack stack = sender.containerMenu.slots.get(slotId).getItem();
        ContainerItemContentsWriter contents = ContainerItemBehaviors.getContents(stack)
                .filter(ContainerItemContentsWriter::canScroll)
                .orElse(null);

        if (contents == null) {
            InventoryExpansion.LOGGER.debug(
                    "Player {} set selected index of invalid stack {}", sender, stack);
            return;
        }

        if (selectedIndex >= contents.getStacks().size() || selectedIndex < -1) {
            InventoryExpansion.LOGGER.debug(
                    "Player {} set an invalid selected index of {}", sender, selectedIndex);
            return;
        }

        contents.setSelectedIndex(selectedIndex);
        sender.containerMenu.setRemoteSlot(slotId, stack);
    }
}
