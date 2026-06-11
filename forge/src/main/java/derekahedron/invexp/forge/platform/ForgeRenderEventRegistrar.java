package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IRenderEventRegistrar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.event.RenderGuiEvent;

import java.util.ArrayList;

public class ForgeRenderEventRegistrar implements IRenderEventRegistrar {

    private static final ArrayList<RenderEventHandler> HANDLERS = new ArrayList<>();

    @Override
    public void register(RenderEventHandler handler) {
        HANDLERS.add(handler);
    }

    /**
     * Initializes all registered render callbacks.
     *
     * @param event the event to register under
     */
    public static void init(RenderGuiEvent.Post event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        float partialTick = event.getPartialTick();

        for (RenderEventHandler handler : HANDLERS) {
            handler.handle(guiGraphics, partialTick);
        }
    }
}
