package derekahedron.invexp.forge.platform;

import derekahedron.invexp.network.NetworkPacket;
import derekahedron.invexp.platform.services.INetworkHandler;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgeNetworkHandler implements INetworkHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            InvExpUtil.location("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int id = 0;

    public static int getId() {
        return id++;
    }

    @Override
    public void sendC2S(NetworkPacket packet) {
        INSTANCE.sendToServer(packet);
    }

    @Override
    public void sendS2CToTracking(NetworkPacket packet, Entity entity) {
        INSTANCE.send(
                PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                packet);
    }
}
