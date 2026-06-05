package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.ICompatibilityHelper;
import net.minecraftforge.fml.ModList;

public class ForgeCompatibilityHelper implements ICompatibilityHelper {

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
