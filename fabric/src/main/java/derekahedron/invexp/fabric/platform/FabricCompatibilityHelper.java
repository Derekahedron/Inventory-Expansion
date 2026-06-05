package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.ICompatibilityHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricCompatibilityHelper implements ICompatibilityHelper {

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
