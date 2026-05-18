package derekahedron.invexp.forge.client;

import derekahedron.invexp.client.InventoryExpansionClient;
import derekahedron.invexp.forge.platform.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Entrypoint for Inventory Expansion Client (Forge).
 */
public class ForgeInventoryExpansionClient {

    /**
     * Initializes the Inventory Expansion Client.
     */
    public ForgeInventoryExpansionClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ForgeItemOverrideRegistrar::init);
        modEventBus.addListener(ForgeClientTooltipsRegistrar::init);
        modEventBus.addListener(ForgeItemColorRegistrar::init);
        modEventBus.addListener(ForgeAdditionalModelRegistrar::init);
        MinecraftForge.EVENT_BUS.addListener(ForgeScrollEventRegistrar::init);

        InventoryExpansionClient.init();
    }
}
