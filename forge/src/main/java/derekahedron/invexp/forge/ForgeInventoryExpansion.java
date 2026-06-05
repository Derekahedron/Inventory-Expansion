package derekahedron.invexp.forge;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.block.cauldron.InvExpCauldronBehavior;
import derekahedron.invexp.block.dispenser.InvExpDispenserBehavior;
import derekahedron.invexp.forge.client.ForgeInventoryExpansionClient;
import derekahedron.invexp.forge.compat.CuriosCompat;
import derekahedron.invexp.forge.platform.*;
import derekahedron.invexp.platform.Services;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Entrypoint for Inventory Expansion (Forge).
 */
@Mod(InventoryExpansion.MOD_ID)
public class ForgeInventoryExpansion {

    /**
     * Initializes the Inventory Expansion.
     */
    public ForgeInventoryExpansion() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgeItemRegistrar.ITEMS.register(modEventBus);
        ForgeRecipeSerializerRegistrar.RECIPE_SERIALIZERS.register(modEventBus);
        ForgeSoundEventRegistrar.SOUND_EVENTS.register(modEventBus);

        modEventBus.addListener((FMLLoadCompleteEvent event) -> event.enqueueWork(InvExpCauldronBehavior::init));
        modEventBus.addListener((FMLLoadCompleteEvent event) -> event.enqueueWork(InvExpDispenserBehavior::init));

        modEventBus.addListener(ForgeRegistryRegistrar::init);
        modEventBus.addListener(ForgePacketRegistrar::init);
        MinecraftForge.EVENT_BUS.addListener(ForgeRegistryEventRegistrar::init);
        modEventBus.addListener(ForgeCreativeItemsRegistrar::init);

        InventoryExpansion.init();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> new ForgeInventoryExpansionClient(modEventBus));

        Services.COMPATIBILITY_HELPER.runIfPresent("curios", CuriosCompat::init);
    }
}