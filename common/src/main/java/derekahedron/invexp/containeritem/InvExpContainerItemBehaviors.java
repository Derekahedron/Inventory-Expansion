package derekahedron.invexp.containeritem;

import derekahedron.invexp.item.bundle.BundleContentsWriter;
import derekahedron.invexp.item.quiver.QuiverContentsWriter;
import derekahedron.invexp.item.sack.SackContentsWriter;

import java.util.Optional;

/**
 * Holds all container item behaviors for Inventory Expansion.
 */
public class InvExpContainerItemBehaviors {

    /**
     * Initializes the container item behaviors.
     */
    public static void init() {
        ContainerItemBehaviors.register(stack -> Optional.ofNullable(SackContentsWriter.of(stack)));
        ContainerItemBehaviors.register(stack -> Optional.ofNullable(QuiverContentsWriter.of(stack)));
        ContainerItemBehaviors.register(stack -> Optional.ofNullable(BundleContentsWriter.of(stack)));
    }
}
