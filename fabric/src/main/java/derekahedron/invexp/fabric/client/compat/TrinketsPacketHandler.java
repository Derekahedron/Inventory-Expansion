package derekahedron.invexp.fabric.client.compat;

import derekahedron.invexp.fabric.compat.SetTrinketSelectedIndexPacket;
import derekahedron.invexp.fabric.compat.SetTrinketSlotPacket;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class TrinketsPacketHandler {

    /**
     * Handles a {@link SetTrinketSlotPacket} by updating the trinket slot for the given player.
     *
     * @param packet a packet that contains information for setting a trinket slot
     */
    public static void handle(SetTrinketSlotPacket packet) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        if (minecraft.level == null) {
            return;
        }

        Entity entity = minecraft.level.getEntity(packet.entityId());
        if (!(entity instanceof Player player)) {
            return;
        }

        TrinketComponent component = TrinketsApi.getTrinketComponent(player).orElse(null);
        if (component == null) {
            return;
        }

        TrinketInventory inventory = Optional.ofNullable(component.getInventory().get(packet.group()))
                .flatMap(group -> Optional.ofNullable(group.get(packet.name())))
                .orElse(null);
        if (inventory == null) {
            return;
        }

        if (packet.index() < 0 || packet.index() >= inventory.getContainerSize()) {
            return;
        }

        inventory.setItem(packet.index(), packet.stack());
        SetTrinketSelectedIndexPacket.getPreviousTrinkets(player)
                .put(packet.group() + "/" + packet.name() + "/" + packet.index(), packet.stack().copy());
    }
}
