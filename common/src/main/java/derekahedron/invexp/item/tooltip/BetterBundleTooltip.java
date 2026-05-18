package derekahedron.invexp.item.tooltip;

import derekahedron.invexp.item.bundle.BundleContentsWriter;
import net.minecraft.network.chat.Component;

import java.util.Optional;

/**
 * Holds bundle tooltip data.
 *
 * @param contents the contents of the bundle
 * @param description the optional extra text appended to the tooltip
 */
public record BetterBundleTooltip(BundleContentsWriter contents, Optional<Component> description) implements StickyTooltipComponent {
}
