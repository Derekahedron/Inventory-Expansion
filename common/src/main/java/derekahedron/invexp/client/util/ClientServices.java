package derekahedron.invexp.client.util;

import derekahedron.invexp.platform.Services;
import derekahedron.invexp.platform.services.*;

/**
 * Holds all client-specific services to provide compatibility between different platforms.
 */
public class ClientServices {

    public static final IModelLayerRegistrar MODEL_LAYER_REGISTRAR = Services.load(IModelLayerRegistrar.class);
    public static final IItemOverrideRegistrar ITEM_OVERRIDE_REGISTRAR = Services.load(IItemOverrideRegistrar.class);
    public static final IClientTooltipRegistrar CLIENT_TOOLTIP_REGISTRAR = Services.load(IClientTooltipRegistrar.class);
    public static final IItemColorRegistrar ITEM_COLORS_REGISTRAR = Services.load(IItemColorRegistrar.class);
    public static final IAdditionalModelRegistrar ADDITIONAL_MODEL_REGISTRAR = Services.load(IAdditionalModelRegistrar.class);
    public static final IScrollEventRegistrar SCROLL_EVENT_REGISTRAR = Services.load(IScrollEventRegistrar.class);
    public static final IKeyMappingRegistrar KEY_MAPPING_REGISTRAR = Services.load(IKeyMappingRegistrar.class);
    public static final IRenderEventRegistrar RENDER_EVENT_REGISTRAR = Services.load(IRenderEventRegistrar.class);
}
