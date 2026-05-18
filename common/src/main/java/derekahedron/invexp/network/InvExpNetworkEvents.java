package derekahedron.invexp.network;

import derekahedron.invexp.platform.Services;

/**
 * Holds network events for Inventory Expansion.
 */
public class InvExpNetworkEvents {

    /**
     * Initializes network events.
     */
    public static void init() {
        Services.PACKET_REGISTRAR.registerC2S(
                SetSelectedIndexPacket.ID,
                SetSelectedIndexPacket.class,
                SetSelectedIndexPacket::encode,
                SetSelectedIndexPacket::new,
                SetSelectedIndexPacket::handle);
    }
}
