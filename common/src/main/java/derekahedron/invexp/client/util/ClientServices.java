package derekahedron.invexp.client.util;

import derekahedron.invexp.platform.Services;
import derekahedron.invexp.platform.services.*;

public class ClientServices {

    public static final IItemOverrideRegistrar ITEM_OVERRIDE_REGISTRAR = Services.load(IItemOverrideRegistrar.class);
    public static final IClientTooltipRegistrar CLIENT_TOOLTIP_REGISTRAR = Services.load(IClientTooltipRegistrar.class);
    public static final IItemColorRegistrar ITEM_COLORS_REGISTRAR = Services.load(IItemColorRegistrar.class);
    public static final IAdditionalModelRegistrar ADDITIONAL_MODEL_REGISTRAR = Services.load(IAdditionalModelRegistrar.class);
    public static final IScrollEventRegistrar SCROLL_EVENT_REGISTRAR = Services.load(IScrollEventRegistrar.class);
}
