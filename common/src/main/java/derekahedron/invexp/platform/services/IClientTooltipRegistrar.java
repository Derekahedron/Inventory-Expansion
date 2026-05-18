package derekahedron.invexp.platform.services;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.function.Function;

/**
 * Registers behavior for additional client tooltips.
 */
public interface IClientTooltipRegistrar {

    /**
     * Registers special client tooltips.
     *
     * @param type the class of tooltip to register
     * @param factory the factory creating a client tooltip from the given tooltip
     * @param <T> the tooltip type
     */
    <T extends TooltipComponent> void register(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory);
}
