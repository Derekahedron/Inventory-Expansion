package derekahedron.invexp.fabric.compat;

import derekahedron.invexp.network.NetworkPacket;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Sets the trinket slot for a given player.
 *
 * @param entityId the entity of the player to set the trinket slot of
 * @param group the group identifier of the trinket slot to set
 * @param name the name of the trinket slot
 * @param index the index of the trinket slot
 * @param stack the stack to set to the slot
 */
public record SetTrinketSlotPacket(int entityId, String group, String name, int index, ItemStack stack) implements NetworkPacket {

    public static final ResourceLocation ID = InvExpUtil.location("set_trinket_slot");

    /**
     * Creates a {@link SetTrinketSlotPacket} from a byte buffer
     *
     * @param buffer the buffer containing the info to create a packet from
     */
    public SetTrinketSlotPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readUtf(), buffer.readUtf(), buffer.readInt(), buffer.readItem());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeUtf(group);
        buf.writeUtf(name);
        buf.writeInt(index);
        buf.writeItem(stack);
    }
}
