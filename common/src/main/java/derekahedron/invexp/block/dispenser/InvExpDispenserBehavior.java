package derekahedron.invexp.block.dispenser;

import derekahedron.invexp.item.InvExpItems;
import net.minecraft.world.level.block.DispenserBlock;

/**
 * Holds dispenser behaviors for Inventory Expansion.
 */
public class InvExpDispenserBehavior {

    /**
     * Initializes dispenser behaviors.
     */
    public static void init() {
        DispenserBlock.registerBehavior(InvExpItems.SACK::get, new SackDispenserBehavior());
        DispenserBlock.registerBehavior(InvExpItems.QUIVER::get, new QuiverDispenserBehavior());
    }
}
