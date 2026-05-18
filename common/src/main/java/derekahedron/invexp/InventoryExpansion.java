package derekahedron.invexp;

import derekahedron.invexp.containeritem.InvExpContainerItemBehaviors;
import derekahedron.invexp.item.InvExpCreativeItems;
import derekahedron.invexp.item.InvExpItems;
import derekahedron.invexp.network.InvExpNetworkEvents;
import derekahedron.invexp.recipe.InvExpRecipeSerializers;
import derekahedron.invexp.registry.InvExpRegistryEvents;
import derekahedron.invexp.registry.InvExpRegistryKeys;
import derekahedron.invexp.sound.InvExpSoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entrypoint for Inventory Expansion.
 */
public class InventoryExpansion {

    public static final String MOD_ID = "invexp";
    public static final String MOD_NAME = "Inventory Expansion";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    /**
     * Initializes Inventory Expansion.
     */
    public static void init() {
        InvExpItems.init();
        InvExpRecipeSerializers.init();
        InvExpSoundEvents.init();
        InvExpRegistryKeys.init();
        InvExpNetworkEvents.init();
        InvExpCreativeItems.init();
        InvExpRegistryEvents.init();
        InvExpContainerItemBehaviors.init();
    }
}