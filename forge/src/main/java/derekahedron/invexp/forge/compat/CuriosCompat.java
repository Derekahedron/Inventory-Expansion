package derekahedron.invexp.forge.compat;

import derekahedron.invexp.forge.client.compat.CuriosPacketHandler;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.util.ModdedInventoriesEvent;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Collection;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Entrypoint for general Curios compatibility.
 */
public class CuriosCompat {

    /**
     * Initializes Curios.
     */
    public static void init() {
        // Registers the curios inventory for inserting items into container items
        ModdedInventoriesEvent.register(player -> CuriosApi.getCuriosInventory(player).resolve()
                .stream()
                .map(ICuriosItemHandler::getCurios)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .flatMap(slotEntry -> IntStream.range(0, slotEntry.getValue().getSlots())
                        .mapToObj(index -> new ModdedInventoriesEvent.SelectedIndexSlotHandler() {
                            @Override
                            public ItemStack getStack() {
                                return slotEntry.getValue().getStacks().getStackInSlot(index);
                            }

                            @Override
                            public void setSelectedIndex(int selectedIndex) {
                                Services.NETWORK_HANDLER.sendC2S(new SetCurioSelectedIndexPacket(
                                        slotEntry.getKey(),
                                        index,
                                        selectedIndex));
                            }
                        })));

        Services.PACKET_REGISTRAR.registerC2S(
                SetCurioSelectedIndexPacket.ID,
                SetCurioSelectedIndexPacket.class,
                SetCurioSelectedIndexPacket::encode,
                SetCurioSelectedIndexPacket::new,
                SetCurioSelectedIndexPacket::handle);
        Services.PACKET_REGISTRAR.registerS2C(
                SetCurioSlotPacket.ID,
                SetCurioSlotPacket.class,
                SetCurioSlotPacket::encode,
                SetCurioSlotPacket::new,
                CuriosPacketHandler::handle);
    }
}
