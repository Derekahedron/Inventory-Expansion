package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IClientTooltipRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FabricClientTooltipRegistrar implements IClientTooltipRegistrar {

    private static final List<ClientTooltipRegistration<? extends TooltipComponent>> REGISTRATIONS = new ArrayList<>();

    @Override
    public <T extends TooltipComponent> void register(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory) {
        REGISTRATIONS.add(new ClientTooltipRegistration<>(type, factory));
    }

    /**
     * Initializes client tooltips.
     */
    public static void init() {
        TooltipComponentCallback.EVENT.register(data -> {
            ClientTooltipComponent clientTooltip;

            for (ClientTooltipRegistration<? extends TooltipComponent> reg : REGISTRATIONS) {
                clientTooltip = reg.getClientTooltip(data);
                if (clientTooltip != null) return clientTooltip;
            }
            return null;
        });
    }

    public record ClientTooltipRegistration<T extends TooltipComponent>(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory) {

        @Nullable
        public ClientTooltipComponent getClientTooltip(TooltipComponent data) {
            if (type.isInstance(data)) {
                return factory.apply(type.cast(data));
            } else {
                return null;
            }
        }
    }
}
