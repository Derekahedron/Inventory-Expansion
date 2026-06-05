package derekahedron.invexp.client;

import derekahedron.invexp.client.gui.tooltip.InvExpClientTooltips;
import derekahedron.invexp.client.gui.InvExpScrollEvents;
import derekahedron.invexp.client.model.InvExpModelLayers;
import derekahedron.invexp.client.render.InvExpAdditionalModels;
import derekahedron.invexp.client.render.InvExpItemColors;
import derekahedron.invexp.client.render.InvExpItemOverrides;

/**
 * Entrypoint for the Inventory Expansion Client.
 */
public class InventoryExpansionClient {

    /**
     * Initializes the Inventory Expansion Client.
     */
    public static void init() {
        InvExpItemColors.init();
        InvExpItemOverrides.init();
        InvExpClientTooltips.init();
        InvExpAdditionalModels.init();
        InvExpModelLayers.init();
        InvExpScrollEvents.init();
    }
}
