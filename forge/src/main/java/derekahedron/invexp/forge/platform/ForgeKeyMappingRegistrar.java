package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IKeyMappingRegistrar;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

import java.util.ArrayList;
import java.util.List;

public class ForgeKeyMappingRegistrar implements IKeyMappingRegistrar {

    private static final List<KeyMapping> KEY_MAPPINGS = new ArrayList<>();

    @Override
    public void register(KeyMapping mapping) {
        KEY_MAPPINGS.add(mapping);
    }

    /**
     * Registers all collected key mappings.
     *
     * @param event the event to register under
     */
    public static void init(RegisterKeyMappingsEvent event) {
        for (KeyMapping mapping : KEY_MAPPINGS) {
            event.register(mapping);
        }
    }
}
