package derekahedron.invexp.platform.services;

import net.minecraft.client.KeyMapping;

/**
 * Service for registering client key mappings (keybinds).
 */
public interface IKeyMappingRegistrar {

    /**
     * Registers a key mapping so it shows up in the controls screen and receives input.
     *
     * @param mapping the key mapping to register
     */
    void register(KeyMapping mapping);
}
