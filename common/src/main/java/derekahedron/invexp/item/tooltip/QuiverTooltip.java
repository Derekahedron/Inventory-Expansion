package derekahedron.invexp.item.tooltip;

import derekahedron.invexp.item.quiver.QuiverContentsWriter;
import net.minecraft.network.chat.Component;

import java.util.Optional;

/**
 * Holds quiver tooltip data.
 *
 * @param contents the contents of the quiver
 * @param description the optional extra text appended to the tooltip
 */
public record QuiverTooltip(QuiverContentsWriter contents, Optional<Component> description) implements StickyTooltipComponent {
}
