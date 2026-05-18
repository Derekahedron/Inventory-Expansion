package derekahedron.invexp.platform.services;

import derekahedron.invexp.network.NetworkPacket;

/**
 * Service for handling network events.
 */
public interface INetworkHandler {

    /**
     * Sends a network packet.
     *
     * @param packet the network packet to send
     */
    void send(NetworkPacket packet);
}
