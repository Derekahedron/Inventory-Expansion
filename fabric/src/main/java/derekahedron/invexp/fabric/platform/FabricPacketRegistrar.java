package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IPacketRegistrar;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class FabricPacketRegistrar implements IPacketRegistrar {

    private static final List<C2SPacketRegistration<?>> C2S_REGISTRATIONS = new ArrayList<>();
    private static final List<S2CPacketRegistration<?>> S2C_REGISTRATIONS = new ArrayList<>();

    @Override
    public <MSG> void registerC2S(
            ResourceLocation id,
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, C2SPacketContext> handler) {
        C2S_REGISTRATIONS.add(new C2SPacketRegistration<>(id, messageType, encoder, decoder, handler));
    }

    @Override
    public <MSG> void registerS2C(
            ResourceLocation id,
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            Consumer<MSG> handler) {
        S2C_REGISTRATIONS.add(new S2CPacketRegistration<>(id, messageType, encoder, decoder, handler));
    }

    /**
     * Initializes all C2S packet registrations.
     */
    public static void init() {
        for (C2SPacketRegistration<?> registration : C2S_REGISTRATIONS) {
            registration.register();
        }
    }

    /**
     * Initializes all C2S packet registrations. Must be called from the client side.
     */
    public static void clientInit() {
        for (S2CPacketRegistration<?> registration : S2C_REGISTRATIONS) {
            registration.register();
        }
    }

    public record C2SPacketRegistration<MSG>(
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

    public record S2CPacketRegistration<MSG>(
            ResourceLocation id,
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            Consumer<MSG> handler) {

        void register() {
            ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, packetSender) -> {
                MSG packet = decoder.apply(buf);
                client.execute(() ->
                        this.handler.accept(packet));
            });
        }
    }
}
