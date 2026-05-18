package derekahedron.invexp.client.render;

import derekahedron.invexp.client.util.ClientServices;
import derekahedron.invexp.client.util.OpenItemTextures;

/**
 * Holds additional models for Inventory Expansion.
 */
public class InvExpAdditionalModels {

    /**
     * Initializes additional model registration.
     */
    public static void init() {
        ClientServices.ADDITIONAL_MODEL_REGISTRAR.register(OpenItemTextures::setupLocations);
    }
}
