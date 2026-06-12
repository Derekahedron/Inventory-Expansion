package derekahedron.invexp.fabric.compat;

import derekahedron.invexp.fabric.client.compat.TrinketsPacketHandler;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.util.ModdedInventoriesEvent;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Entrypoint for general Trinkets compatibility.
 */
public class TrinketsCompat {

    /**
     * Initializes Trinkets.
     */
    public static void init() {
        // Registers the trinkets inventory for inserting items into container items
        ModdedInventoriesEvent.register(player -> TrinketsApi.getTrinketComponent(player)
                .stream()
                .map(TrinketComponent::getInventory)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .flatMap(groupEntry -> groupEntry.getValue().entrySet().stream()
                        .flatMap(slotEntry -> IntStream.range(0, slotEntry.getValue().getContainerSize())
                                .mapToObj(index -> new ModdedInventoriesEvent.SelectedIndexSlotHandler() {
                                    @Override
                                    public ItemStack getStack() {
                                        return slotEntry.getValue().getItem(index);
                                    }

                                    @Override
                                    public void setSelectedIndex(int selectedIndex) {
                                        Services.NETWORK_HANDLER.sendC2S(new SetTrinketSelectedIndexPacket(
                                                groupEntry.getKey(),
                                                slotEntry.getKey(),
                                                index,
                                                selectedIndex));
                                    }
                                }))));

        Services.PACKET_REGISTRAR.registerC2S(
                SetTrinketSelectedIndexPacket.ID,
                SetTrinketSelectedIndexPacket.class,
                SetTrinketSelectedIndexPacket::encode,
                SetTrinketSelectedIndexPacket::new,
                SetTrinketSelectedIndexPacket::handle);
        Services.PACKET_REGISTRAR.registerS2C(
                SetTrinketSlotPacket.ID,
                SetTrinketSlotPacket.class,
                SetTrinketSlotPacket::encode,
                SetTrinketSlotPacket::new,
                TrinketsPacketHandler::handle);
    }
}