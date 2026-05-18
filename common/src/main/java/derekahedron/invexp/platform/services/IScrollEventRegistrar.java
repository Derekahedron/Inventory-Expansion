package derekahedron.invexp.platform.services;

import net.minecraft.client.gui.screens.Screen;

/**
 * Service for setting up scroll events.
 */
public interface IScrollEventRegistrar {

    /**
     * Register a scroll event.
     *
     * @param handler the event handler to register
     */
    void register(ScrollEventHandler handler);

    interface ScrollEventHandler {
        boolean handleScroll(Screen screen, double mouseX, double mouseY, double scrollDelta);
    }
}
