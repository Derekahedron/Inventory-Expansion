package derekahedron.invexp.forge.client.compat;

import derekahedron.invexp.item.InvExpItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

/**
 * Entrypoint for compatibility with Curios on the client side.
 */
public class CuriosCompatClient {

    /**
     * Initializes Curios on the client.
     *
     * @param modEventBus the mod bus to register events under
     */
    public static void init(IEventBus modEventBus) {
        // Register the quiver renderer
        modEventBus.addListener((FMLClientSetupEvent event) ->
                event.enqueueWork(() ->
                        CuriosRendererRegistry.register(InvExpItems.QUIVER.get(), QuiverCurioRenderer::new)));
    }
}
