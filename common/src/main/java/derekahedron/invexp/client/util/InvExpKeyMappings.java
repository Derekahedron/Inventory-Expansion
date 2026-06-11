package derekahedron.invexp.client.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

/**
 * Holds the key mappings added by Inventory Expansion.
 */
public class InvExpKeyMappings {

    public static final String INVENTORY_EXPANSION_CATEGORY = "key.categories.invexp";

    public static final KeyMapping QUICK_SWAP = register(new KeyMapping(
            "key.invexp.quick_swap",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            INVENTORY_EXPANSION_CATEGORY));

    /**
     * Registers a key mapping.
     *
     * @param keyMapping the key mapping to be registered
     * @return the key mapping registered
     */
    public static KeyMapping register(KeyMapping keyMapping) {
        ClientServices.KEY_MAPPING_REGISTRAR.register(keyMapping);
        return keyMapping;
    }

    /**
     * Initializes key mappings.
     */
    public static void init() {
        // Do nothing: load class
    }
}
