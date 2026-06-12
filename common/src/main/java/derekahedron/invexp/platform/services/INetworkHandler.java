package derekahedron.invexp.platform.services;

import derekahedron.invexp.network.NetworkPacket;
import net.minecraft.world.entity.Entity;

/**
 * Service for handling network events.
 */
public interface INetworkHandler {

    /**
     * Sends a C2S network packet.
     *
     * @param packet the network packet to send
     */
    void sendC2S(NetworkPacket packet);

    /**
     * Sends a S2C network packet to all players tracking a given entity.
     *
     * @param packet the network packet to send
     * @param entity the entity to send to all players that track it
     */
    void sendS2CToTracking(NetworkPacket packet, Entity entity);
}
