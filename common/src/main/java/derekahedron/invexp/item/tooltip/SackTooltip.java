package derekahedron.invexp.item.tooltip;

import derekahedron.invexp.item.sack.SackContentsReader;
import net.minecraft.network.chat.Component;

import java.util.Optional;

/**
 * Holds sack tooltip data.
 *
 * @param contents the contents of the sack
 * @param description the optional extra text appended to the tooltip
 */
public record SackTooltip(SackContentsReader contents, Optional<Component> description) implements StickyTooltipComponent {
}
