package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.fabric.client.FabricInventoryExpansionClient;
import derekahedron.invexp.platform.services.IRegistryEventRegistrar;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.List;

public class FabricRegistryEventRegistrar implements IRegistryEventRegistrar {

    private static final List<RegistryEventHandler> EVENT_HANDLERS = new ArrayList<>();

    @Override
    public void register(RegistryEventHandler handler) {
        EVENT_HANDLERS.add(handler);
    }

    /**
     * Initializes registry loaded events.
     */
    public static void init() {
        CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> {
            boolean shouldUpdateStaticData = FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT
                    || !FabricInventoryExpansionClient.isRunningIntegratedServer();

            for (RegistryEventHandler handler : EVENT_HANDLERS) {
                handler.onRegistryLoad(registries, shouldUpdateStaticData);
            }
        });
    }
}
