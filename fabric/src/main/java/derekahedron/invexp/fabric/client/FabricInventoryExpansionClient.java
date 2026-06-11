package derekahedron.invexp.fabric.client;

import derekahedron.invexp.client.InventoryExpansionClient;
import derekahedron.invexp.fabric.client.compat.TrinketsCompatClient;
import derekahedron.invexp.fabric.platform.*;
import derekahedron.invexp.platform.Services;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;

/**
 * Entrypoint for the Inventory Expansion Client (Fabric).
 */
public class FabricInventoryExpansionClient implements ClientModInitializer {

    /**
     * Initializes the Inventory Expansion Client.
     */
    @Override
    public void onInitializeClient() {
        InventoryExpansionClient.init();

        FabricAdditionalModelRegistrar.init();
        FabricClientTooltipRegistrar.init();
        FabricItemColorRegistrar.init();
        FabricItemOverrideRegistrar.init();
        FabricRenderEventRegistrar.init();
        FabricScrollEventRegistrar.init();

        Services.COMPATIBILITY_HELPER.runIfPresent("trinkets", TrinketsCompatClient::init);
    }

    /**
     * Checks if an integrated server is being run. Useful for determining things like
     * loading static data.
     *
     * @return <code>true</code> if the server is local; <code>false</code> otherwise
     */
    public static boolean isRunningIntegratedServer() {
        return Minecraft.getInstance().isLocalServer();
    }
}
