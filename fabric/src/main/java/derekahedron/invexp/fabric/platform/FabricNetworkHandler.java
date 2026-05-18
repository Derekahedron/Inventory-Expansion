package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.network.NetworkPacket;
import derekahedron.invexp.platform.services.INetworkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

public class FabricNetworkHandler implements INetworkHandler {

    @Override
    public void send(NetworkPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ClientPlayNetworking.send(packet.getId(), buf);
    }
}
