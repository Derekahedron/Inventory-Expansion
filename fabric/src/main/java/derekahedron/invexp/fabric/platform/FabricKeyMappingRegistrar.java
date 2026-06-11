package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IKeyMappingRegistrar;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

public class FabricKeyMappingRegistrar implements IKeyMappingRegistrar {

    @Override
    public void register(KeyMapping mapping) {
        KeyBindingHelper.registerKeyBinding(mapping);
    }
}
