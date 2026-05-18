package derekahedron.invexp.platform.services;

import net.minecraft.core.RegistryAccess;

public interface IRegistryEventRegistrar {

    void register(RegistryEventHandler handler);

    interface RegistryEventHandler {
        void onRegistryLoad(RegistryAccess registries, boolean shouldUpdateStaticData);
    }
}
