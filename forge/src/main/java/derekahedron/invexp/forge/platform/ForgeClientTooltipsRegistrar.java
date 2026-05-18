package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IClientTooltipRegistrar;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ForgeClientTooltipsRegistrar implements IClientTooltipRegistrar {

    private static final List<ClientTooltipRegistration<? extends TooltipComponent>> REGISTRATIONS = new ArrayList<>();

    @Override
    public <T extends TooltipComponent> void register(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory) {
        REGISTRATIONS.add(new ClientTooltipRegistration<>(type, factory));
    }

    /**
     * Initializes client tooltips.
     *
     * @param event the event to register tooltips under
     */
    public static void init(RegisterClientTooltipComponentFactoriesEvent event) {
        for (ClientTooltipRegistration<? extends TooltipComponent> reg : REGISTRATIONS) {
            reg.register(event);
        }
    }

    public record ClientTooltipRegistration<T extends TooltipComponent>(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory) {

        public void register(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(type, factory);
        }
    }
}
