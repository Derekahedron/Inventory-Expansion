package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IPacketRegistrar;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FabricPacketRegistrar implements IPacketRegistrar {

    private static final List<PacketRegistration<?>> C2S_REGISTRATIONS = new ArrayList<>();

    @Override
    public <MSG> void registerC2S(
            ResourceLocation id,
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, C2SPacketContext> handler) {
        C2S_REGISTRATIONS.add(new PacketRegistration<>(id, messageType, encoder, decoder, handler));
    }

    /**
     * Initializes all packet registrations.
     */
    public static void init() {
        for (PacketRegistration<?> registration : C2S_REGISTRATIONS) {
            registration.register();
        }
    }

    public record PacketRegistration<MSG>(
            ResourceLocation id,
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, C2SPacketContext> handler) {

        void register() {
            ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handler, buf, responseSender) -> {
                MSG packet = decoder.apply(buf);
                server.execute(() ->
                        this.handler.accept(packet, new C2SPacketContext(player)));
            });
        }
    }
}
