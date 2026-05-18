package derekahedron.invexp.forge.platform;

import com.mojang.serialization.Codec;
import derekahedron.invexp.platform.services.IRegistryRegistrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DataPackRegistryEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForgeRegistryRegistrar implements IRegistryRegistrar {

    private static final List<RegistryRegistration<?>> REGISTRATIONS = new ArrayList<>();

    @Override
    public <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec) {
        REGISTRATIONS.add(new RegistryRegistration<>(key, codec, Optional.empty()));
    }

    @Override
    public <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> clientCodec) {
        REGISTRATIONS.add(new RegistryRegistration<>(key, codec, Optional.of(clientCodec)));
    }

    /**
     * Initializes registries.
     *
     * @param event the event to register under
     */
    public static void init(DataPackRegistryEvent.NewRegistry event) {
        for (RegistryRegistration<?> reg : REGISTRATIONS) {
            reg.register(event);
        }
    }

    public record RegistryRegistration<T>(ResourceKey<Registry<T>> key, Codec<T> codec, Optional<Codec<T>> clientCodec) {

        public void register(DataPackRegistryEvent.NewRegistry event) {
            event.dataPackRegistry(key, codec, clientCodec.orElse(null));
        }
    }
}
