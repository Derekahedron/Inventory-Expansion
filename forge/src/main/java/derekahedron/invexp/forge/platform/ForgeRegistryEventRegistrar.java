package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IRegistryEventRegistrar;
import net.minecraftforge.event.TagsUpdatedEvent;

import java.util.ArrayList;
import java.util.List;

public class ForgeRegistryEventRegistrar implements IRegistryEventRegistrar {
    private static final List<RegistryEventHandler> EVENT_HANDLERS = new ArrayList<>();

    @Override
    public void register(RegistryEventHandler handler) {
        EVENT_HANDLERS.add(handler);
    }

    public static void init(TagsUpdatedEvent event) {
        for (RegistryEventHandler handler : EVENT_HANDLERS) {
            handler.onRegistryLoad(event.getRegistryAccess(), event.shouldUpdateStaticData());
        }
    }
}
