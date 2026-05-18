package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IScrollEventRegistrar;
import net.minecraftforge.client.event.ScreenEvent;

import java.util.ArrayList;
import java.util.List;

public class ForgeScrollEventRegistrar implements IScrollEventRegistrar {

    private static final List<ScrollEventHandler> EVENT_HANDLERS = new ArrayList<>();

    @Override
    public void register(ScrollEventHandler handler) {
        EVENT_HANDLERS.add(handler);
    }

    /**
     * Initializes scroll events.
     *
     * @param event the event to register under
     */
    public static void init(ScreenEvent.MouseScrolled event) {
        for (ScrollEventHandler handler : EVENT_HANDLERS) {
            if (!handler.handleScroll(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getScrollDelta())) {
                event.setCanceled(true);
                return;
            }
        }
    }
}
