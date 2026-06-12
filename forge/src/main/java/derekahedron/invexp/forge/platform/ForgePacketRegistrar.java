package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IPacketRegistrar;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ForgePacketRegistrar implements IPacketRegistrar {

    private static final List<C2SPacketRegistration<?>> C2S_REGISTRATIONS = new ArrayList<>();
    private static final List<S2CPacketRegistration<?>> S2C_REGISTRATIONS = new ArrayList<>();

    @Override
    public <MSG> void registerC2S(
            ResourceLocation id,
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, C2SPacketContext> handler) {
        C2S_REGISTRATIONS.add(new C2SPacketRegistration<>(messageType, encoder, decoder, handler));
    }

    @Override
    public <MSG> void registerS2C(
            ResourceLocation id,
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            Consumer<MSG> handler) {
        S2C_REGISTRATIONS.add(new S2CPacketRegistration<>(messageType, encoder, decoder, handler));
    }

    /**
     * Initializes all packet registrations.
     *
     * @param event the event to register under
     */
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (C2SPacketRegistration<?> registration : C2S_REGISTRATIONS) {
                registration.register();
            }
            for (S2CPacketRegistration<?> registration : S2C_REGISTRATIONS) {
                registration.register();
            }
        });
    }

    public record C2SPacketRegistration<MSG>(
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, C2SPacketContext> handler) {

        void register() {
            ForgeNetworkHandler.INSTANCE.registerMessage(
                    ForgeNetworkHandler.getId(),
                    messageType,
                    encoder,
                    decoder,
                    (msg, context) ->
                            context.get().enqueueWork(() ->
                                    handler.accept(msg, new C2SPacketContext(
                                            context.get().getSender()))
                            ),
                    Optional.of(NetworkDirection.PLAY_TO_SERVER));
        }
    }

    public record S2CPacketRegistration<MSG>(
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            Consumer<MSG> handler) {

        void register() {
            ForgeNetworkHandler.INSTANCE.registerMessage(
                    ForgeNetworkHandler.getId(),
                    messageType,
                    encoder,
                    decoder,
                    (msg, context) ->
                            context.get().enqueueWork(() ->
                                    handler.accept(msg)
                            ),
                    Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        }
    }
}
