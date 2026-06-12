package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.network.NetworkPacket;
import derekahedron.invexp.platform.services.INetworkHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class FabricNetworkHandler implements INetworkHandler {

    @Override
    public void sendC2S(NetworkPacket packet) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);
        ClientPlayNetworking.send(packet.getId(), buf);
    }

    @Override
    public void sendS2CToTracking(NetworkPacket packet, Entity entity) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        packet.encode(buf);

        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, packet.getId(), buf);
        }
    }
}
