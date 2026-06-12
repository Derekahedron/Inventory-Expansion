package derekahedron.invexp.fabric;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.block.cauldron.InvExpCauldronBehavior;
import derekahedron.invexp.block.dispenser.InvExpDispenserBehavior;
import derekahedron.invexp.fabric.compat.TrinketsCompat;
import derekahedron.invexp.fabric.platform.FabricCreativeItemsRegistrar;
import derekahedron.invexp.fabric.platform.FabricPacketRegistrar;
import derekahedron.invexp.fabric.platform.FabricRegistryEventRegistrar;
import derekahedron.invexp.platform.Services;
import net.fabricmc.api.ModInitializer;

/**
 * Entrypoint for Inventory Expansion (Fabric).
 */
public class FabricInventoryExpansion implements ModInitializer {

    /**
     * Initializes Inventory Expansion.
     */
    @Override
    public void onInitialize() {
        InventoryExpansion.init();
        Services.COMPATIBILITY_HELPER.runIfPresent("trinkets", TrinketsCompat::init);

        InvExpCauldronBehavior.init();
        InvExpDispenserBehavior.init();
        FabricPacketRegistrar.init();
        FabricRegistryEventRegistrar.init();
        FabricCreativeItemsRegistrar.init();
    }
}
