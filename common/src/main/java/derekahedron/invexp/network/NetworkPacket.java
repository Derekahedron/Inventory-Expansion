package derekahedron.invexp.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Contains methods needed for defining a Network Packet.
 */
public interface NetworkPacket {

    /**
     * Gets the id of the network packet.
     *
     * @return a unique identifier for the packet
     */
    ResourceLocation getId();

    /**
     * Encodes the network packet to a buffer.
     *
     * @param buf the buffer to encode the packet on
     */
    void encode(FriendlyByteBuf buf);
}
