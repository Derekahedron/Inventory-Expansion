package derekahedron.invexp.fabric.platform;

import com.mojang.serialization.Codec;
import derekahedron.invexp.platform.services.IRegistryRegistrar;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class FabricRegistryRegistrar implements IRegistryRegistrar {

    @Override
    public <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec) {
        DynamicRegistries.register(key, codec);
    }

    @Override
    public <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> clientCodec) {
        DynamicRegistries.registerSynced(key, codec, clientCodec);
    }
}
