package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IScrollEventRegistrar;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class FabricScrollEventRegistrar implements IScrollEventRegistrar {

    private static final List<ScrollEventHandler> EVENT_HANDLERS = new ArrayList<>();

    @Override
    public void register(ScrollEventHandler handler) {
        EVENT_HANDLERS.add(handler);
    }

    /**
     * Initializes scroll events.
     */
    public static void init() {
        for (ScrollEventHandler handler : EVENT_HANDLERS) {
            ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) ->
                    ScreenMouseEvents.allowMouseScroll(screen).register((
                            Screen screen1, double mouseX, double mouseY, double horizontal, double vertical) -> handler.handleScroll(screen1, mouseX, mouseY, vertical)));
        }
    }
}
