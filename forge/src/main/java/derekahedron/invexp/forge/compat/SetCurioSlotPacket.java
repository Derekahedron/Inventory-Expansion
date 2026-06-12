package derekahedron.invexp.forge.compat;

import derekahedron.invexp.network.NetworkPacket;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Sets the curio slot for a given player.
 *
 * @param entityId the entity of the player to set the curio slot of
 * @param identifier the slot identifier of the curio slot to set
 * @param index the index of the curios slot
 * @param stack the stack to set to the slot
 */
public record SetCurioSlotPacket(int entityId, String identifier, int index, ItemStack stack) implements NetworkPacket {

    public static final ResourceLocation ID = InvExpUtil.location("set_curio_slot");

    /**
     * Creates a {@link SetCurioSlotPacket} from a byte buffer
     *
     * @param buffer the buffer containing the info to create a packet from
     */
    public SetCurioSlotPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readUtf(), buffer.readInt(), buffer.readItem());
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeUtf(identifier);
        buf.writeInt(index);
        buf.writeItem(stack);
    }
}
