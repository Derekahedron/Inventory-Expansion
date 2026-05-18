package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IPacketRegistrar;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ForgePacketRegistrar implements IPacketRegistrar {

    private static final List<PacketRegistration<?>> C2S_REGISTRATIONS = new ArrayList<>();

    @Override
    public <MSG> void registerC2S(
            ResourceLocation id,
            Class<MSG> messageType,
            BiConsumer<MSG, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, MSG> decoder,
            BiConsumer<MSG, C2SPacketContext> handler) {
        C2S_REGISTRATIONS.add(new PacketRegistration<>(messageType, encoder, decoder, handler));
    }

    /**
     * Initializes all packet registrations.
     *
     * @param event the event to register under
     */
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            for (PacketRegistration<?> registration : C2S_REGISTRATIONS) {
                registration.register();
            }
        });
    }

    public record PacketRegistration<MSG>(
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
                            ));
        }
    }
}
