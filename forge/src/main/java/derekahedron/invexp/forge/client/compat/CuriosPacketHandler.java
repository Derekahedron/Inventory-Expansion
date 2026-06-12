package derekahedron.invexp.forge.client.compat;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.forge.compat.SetCurioSlotPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Optional;

/**
 * Handles Server to Client Packets for Curios.
 */
public class CuriosPacketHandler {

    /**
     * Handles a {@link SetCurioSlotPacket} by updating the curio slot for the given player.
     *
     * @param packet a packet that contains information for setting a curio slot
     */
    public static void handle(SetCurioSlotPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            InventoryExpansion.LOGGER.debug("Received a SetCurioSlotPacket for no player!");
            return;
        }

        if (minecraft.level == null) {
            InventoryExpansion.LOGGER.debug("Received a SetCurioSlotPacket for no level!");
            return;
        }

        Entity entity = minecraft.level.getEntity(packet.entityId());
        if (!(entity instanceof Player player)) {
            InventoryExpansion.LOGGER.debug(
                    "Cannot set curio slot for non-player entity {}", entity);
            return;
        }

        ICuriosItemHandler curios = CuriosApi.getCuriosInventory(player).resolve().orElse(null);
        if (curios == null) {
            InventoryExpansion.LOGGER.debug(
                    "Cannot set curio slot for player {} without curios inventory", player);
            return;
        }

        IDynamicStackHandler handler = Optional.ofNullable(curios.getCurios().get(packet.identifier()))
                .map(ICurioStacksHandler::getStacks)
                .orElse(null);
        if (handler == null) {
            InventoryExpansion.LOGGER.debug(
                    "No curio slot found with identifier {}", packet.identifier());
            return;
        }

        if (packet.index() < 0 || packet.index() >= handler.getSlots()) {
            InventoryExpansion.LOGGER.debug(
                    "Index {} out of bounds for curios inventory of size {}", packet.index(), handler.getSlots());
            return;
        }

        handler.setStackInSlot(packet.index(), packet.stack());
        handler.setPreviousStackInSlot(packet.index(), packet.stack().copy());
    }
}
