package derekahedron.invexp.platform.services;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Service for registering packets.
 */
public interface IPacketRegistrar {

    /**
     * Registers client to server packet.
     *
     * @param id the id for the packet type
     * @param messageType the class of the packet
     * @param encoder the encoder from packet class to buffer
     * @param decoder the decoder from buffer to packet class
     * @param handler the packet handler
     * @param <MSG> the type of packet
     */
    <MSG> void registerC2S(
            ResourceLocation id,
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, C2SPacketContext> handler);

    record C2SPacketContext(@Nullable ServerPlayer player) {
    }
}
