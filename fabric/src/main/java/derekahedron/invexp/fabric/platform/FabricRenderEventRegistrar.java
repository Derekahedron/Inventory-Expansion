package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IRenderEventRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import java.util.ArrayList;

public class FabricRenderEventRegistrar implements IRenderEventRegistrar {

    private static final ArrayList<RenderEventHandler> HANDLERS = new ArrayList<>();

    @Override
    public void register(RenderEventHandler handler) {
        HANDLERS.add(handler);
    }

    /**
     * Initializes all registered render callbacks.
     */
    public static void init() {
        HudRenderCallback.EVENT.register((guiGraphics, partialTick) -> {
            for (RenderEventHandler handler : HANDLERS) {
                handler.handle(guiGraphics, partialTick);
            }
        });
    }
}
