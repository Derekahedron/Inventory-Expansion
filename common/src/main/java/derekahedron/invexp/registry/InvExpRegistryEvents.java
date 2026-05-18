package derekahedron.invexp.registry;

import derekahedron.invexp.item.sack.SackRuleManager;
import derekahedron.invexp.platform.Services;

/**
 * Holds all the registry reload events for Inventory Expansion.
 */
public class InvExpRegistryEvents {

    /**
     * Initializes the registry reload events.
     */
    public static void init() {
        Services.REGISTRY_EVENT_REGISTRAR.register((registries, shouldUpdateStaticData) -> {
            if (shouldUpdateStaticData) {
                SackRuleManager.createNewInstance(registries);
            }
        });
    }
}
