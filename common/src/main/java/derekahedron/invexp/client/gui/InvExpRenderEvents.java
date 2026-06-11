package derekahedron.invexp.client.gui;

import derekahedron.invexp.client.util.ClientServices;
import derekahedron.invexp.client.util.QuickSwapHandler;

/**
 * Holds callbacks for render events for Inventory Expansion
 */
public class InvExpRenderEvents {

    /**
     * Registers all render events.
     */
    public static void init() {
        ClientServices.RENDER_EVENT_REGISTRAR.register(((guiGraphics, partialTick) ->
                QuickSwapHandler.render(guiGraphics)));
    }
}
