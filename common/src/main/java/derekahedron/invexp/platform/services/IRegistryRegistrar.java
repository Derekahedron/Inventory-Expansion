package derekahedron.invexp.platform.services;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * Service for registering dynamic registries.
 */
public interface IRegistryRegistrar {

    /**
     * Registers a dynamic registry.
     *
     * @param key the registry key of the registry to register
     * @param codec the codec for entries of the registry
     * @param <T> the type of registry
     */
    <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec);

    /**
     * Registers a dynamic registry.
     *
     * @param key the registry key of the registry to register
     * @param codec the codec for entries of the registry
     * @param clientCodec the codec for sending entries to the client
     * @param <T> the type of registry
     */
    <T> void register(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> clientCodec);
}
