package derekahedron.invexp.fabric.compat;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.network.NetworkPacket;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.platform.services.IPacketRegistrar;
import derekahedron.invexp.util.InvExpUtil;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Sets the selected index for a trinket slot on the server.
 *
 * @param group the group identifier of the trinket slot to set
 * @param name the name of the trinket slot
 * @param index the index of the trinket slot
 * @param selectedIndex the new selected index to set
 */
public record SetTrinketSelectedIndexPacket(
        String group,
        String name,
        int index,
        int selectedIndex) implements NetworkPacket {

    public static final ResourceLocation ID = InvExpUtil.location("set_trinket_selected_index");

    /**
     * Creates a {@link SetTrinketSelectedIndexPacket} from a byte buffer
     *
     * @param buffer the buffer containing the info to create a packet from
     */
    public SetTrinketSelectedIndexPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(), buffer.readUtf(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(group);
        buf.writeUtf(name);
        buf.writeInt(index);
        buf.writeInt(selectedIndex);
    }

    /**
     * Handles the selected index packet that sets the selected index for a given players
     * container item in a trinket slot.
     *
     * @param context the packet context containing the sending player
     */
    public void handle(IPacketRegistrar.C2SPacketContext context) {
        ServerPlayer sender = context.player();

        if (sender == null) {
            InventoryExpansion.LOGGER.debug(
                    "Received Set Trinket Selected Index packet with no sender!");
            return;
        }

        TrinketComponent component = TrinketsApi.getTrinketComponent(sender).orElse(null);
        if (component == null) {
            InventoryExpansion.LOGGER.debug(
                    "Player {} tried to change the Trinkets inventory but doesn't have one!", sender);
            return;
        }

        TrinketInventory inventory = Optional.ofNullable(component.getInventory().get(group))
                .flatMap(group -> Optional.ofNullable(group.get(name)))
                .orElse(null);
        if (inventory == null) {
            InventoryExpansion.LOGGER.debug(
                    "Player {} set selected index of invalid trinkets slot {}/{}!", sender, group, name);
            return;
        }

        if (index < 0 || index >= inventory.getContainerSize()) {
            InventoryExpansion.LOGGER.debug(
                    "Player {} set selected index of invalid trinkets slot id {}/{}:{}", sender, group, name, index);
            return;
        }

        // Makes sure the container is valid
        ItemStack stack = inventory.getItem(index);
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
        // Update the previous trinkets as to not trigger a re-sync with the sending player
        getPreviousTrinkets(context.player())
                .put(group + "/" + name + "/" + index, contents.getContainerStack().copy());

        // Sometimes, the container can also contain the trinket slot, so we want to make sure that doesn't trigger
        // a re-sync either.
        sender.containerMenu.slots.stream()
                .filter(slot -> slot.getItem() == contents.getContainerStack())
                .findFirst()
                .ifPresent(slot -> sender.containerMenu.setRemoteSlot(slot.index, contents.getContainerStack()));

        // Update the inventory change to all tracking players
        Services.NETWORK_HANDLER.sendS2CToTracking(
                new SetTrinketSlotPacket(
                        sender.getId(),
                        group,
                        name,
                        index,
                        contents.getContainerStack()),
                sender);
    }

    /**
     * Gets a map containing the previous trinkets tracked for a given player. Uses reflection as there is no
     * API for this, and we have to do this to avoid an even hackier approach.
     *
     * @param entity the entity to get the tracked slots for
     * @return a map containing all trinkets that were in the entities inventory in the previous tick
     */
    public static Map<String, ItemStack> getPreviousTrinkets(LivingEntity entity) {
        try {
            @SuppressWarnings("JavaReflectionMemberAccess")
            final Field equippedTrinketsField = LivingEntity.class.getDeclaredField("lastEquippedTrinkets");
            equippedTrinketsField.setAccessible(true);
            Object o = equippedTrinketsField.get(entity);
            //noinspection unchecked
            return (Map<String, ItemStack>) o;
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
            return new HashMap<>();
        }
    }
}
