package derekahedron.invexp.block.cauldron;

import derekahedron.invexp.item.InvExpItems;
import net.minecraft.core.cauldron.CauldronInteraction;

/**
 * Holds cauldron behaviors for Inventory Expansion.
 */
public class InvExpCauldronBehavior {

    /**
     * Initializes cauldron behaviors.
     */
    public static void init() {
        CauldronInteraction.WATER.put(InvExpItems.SACK.get(), CauldronInteraction.DYED_ITEM);
    }
}
