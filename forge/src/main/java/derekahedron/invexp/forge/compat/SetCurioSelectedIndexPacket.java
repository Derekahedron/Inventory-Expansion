package derekahedron.invexp.forge.compat;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.network.NetworkPacket;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.platform.services.IPacketRegistrar;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

/**
 * Sets the selected index for a curio slot on the server.
 *
 * @param identifier the slot identifier of the curio slot to set
 * @param index the index of the curio slot
 * @param selectedIndex the new selected index to set
 */
public record SetCurioSelectedIndexPacket(String identifier, int index, int selectedIndex) implements NetworkPacket {

    public static final ResourceLocation ID = InvExpUtil.location("set_curio_selected_index");

    /**
     * Creates a {@link SetCurioSelectedIndexPacket} from a byte buffer
     *
     * @param buffer the buffer containing the info to create a packet from
     */
    public SetCurioSelectedIndexPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(identifier);
        buf.writeInt(index);
        buf.writeInt(selectedIndex);
    }

    /**
     * Handles the selected index packet that sets the selected index for a given players
     * container item in a curio slot.
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

        ICuriosItemHandler curios = CuriosApi.getCuriosInventory(sender).resolve().orElse(null);
        if (curios == null) {
            InventoryExpansion.LOGGER.debug(
                    "Player {} tried to change the Curios inventory but doesn't have one!", sender);
            return;
        }

        ICurioStacksHandler handler = curios.getCurios().get(identifier);
        if (handler == null) {
            InventoryExpansion.LOGGER.debug(
                    "Player {} set selected index of invalid curio slot {}!", sender, identifier);
            return;
        }

        if (index < 0 || index >= handler.getStacks().getSlots()) {
            InventoryExpansion.LOGGER.debug(
                    "Player {} set selected index of invalid curios slot id {}:{}", sender, identifier, index);
            return;
        }

        // Makes sure the container is valid
        ItemStack stack = handler.getStacks().getStackInSlot(index);
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
        // Update the previous index as to not trigger a re-sync with the sending player
        handler.getStacks().setPreviousStackInSlot(index, contents.getContainerStack().copy());

        // Sometimes, the container can also contain the curio slot, so we want to make sure that doesn't trigger
        // a re-sync either.
        sender.containerMenu.slots.stream()
                .filter(slot -> slot.getItem() == contents.getContainerStack())
                .findFirst()
                .ifPresent(slot -> sender.containerMenu.setRemoteSlot(slot.index, contents.getContainerStack()));

        // Update the inventory change to all tracking players
        Services.NETWORK_HANDLER.sendS2CToTracking(
                new SetCurioSlotPacket(
                        sender.getId(),
                        identifier,
                        index,
                        contents.getContainerStack()),
                sender);
    }
}