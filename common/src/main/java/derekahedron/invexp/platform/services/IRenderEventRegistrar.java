package derekahedron.invexp.platform.services;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Service for registering render events. These events can render a gui on the screen.
 */
public interface IRenderEventRegistrar {

    /**
     * Registers a render event.
     *
     * @param handler the event handler to register
     */
    void register(RenderEventHandler handler);

    /**
     * Event handler for rendering a gui on the screen.
     */
    interface RenderEventHandler {

        /**
         * Renders a gui on the screen.
         *
         * @param guiGraphics the graphics renderer
         * @param partialTick how long has passed since the previous tick
         */
        void handle(GuiGraphics guiGraphics, float partialTick);
    }
}
