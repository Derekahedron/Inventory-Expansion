package derekahedron.invexp.forge.client;

import derekahedron.invexp.client.InventoryExpansionClient;
import derekahedron.invexp.forge.client.compat.CuriosCompatClient;
import derekahedron.invexp.forge.platform.*;
import derekahedron.invexp.platform.Services;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Entrypoint for Inventory Expansion Client (Forge).
 */
public class ForgeInventoryExpansionClient {

    /**
     * Initializes the Inventory Expansion Client.
     *
     * @param modEventBus the mod bus to register events under
     */
    public ForgeInventoryExpansionClient(IEventBus modEventBus) {
        modEventBus.addListener(ForgeItemOverrideRegistrar::init);
        modEventBus.addListener(ForgeClientTooltipsRegistrar::init);
        modEventBus.addListener(ForgeItemColorRegistrar::init);
        modEventBus.addListener(ForgeAdditionalModelRegistrar::init);
        modEventBus.addListener(ForgeModelLayerRegistrar::init);
        MinecraftForge.EVENT_BUS.addListener(ForgeScrollEventRegistrar::init);

        InventoryExpansionClient.init();

        Services.COMPATIBILITY_HELPER.runIfPresent("curios", () ->
                CuriosCompatClient.init(modEventBus));
    }
}
