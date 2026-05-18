package derekahedron.invexp.client.gui.tooltip;

import derekahedron.invexp.client.util.ClientServices;
import derekahedron.invexp.item.tooltip.BetterBundleTooltip;
import derekahedron.invexp.item.tooltip.QuiverTooltip;
import derekahedron.invexp.item.tooltip.SackTooltip;

/**
 * Registers Client Tooltips for Inventory Expansion
 */
public class InvExpClientTooltips {

    /**
     * Initializes client tooltips.
     */
    public static void init() {
        ClientServices.CLIENT_TOOLTIP_REGISTRAR.register(SackTooltip.class, ClientSackTooltip::new);
        ClientServices.CLIENT_TOOLTIP_REGISTRAR.register(QuiverTooltip.class, ClientQuiverTooltip::new);
        ClientServices.CLIENT_TOOLTIP_REGISTRAR.register(BetterBundleTooltip.class, BetterClientBundleTooltip::new);
    }
}
