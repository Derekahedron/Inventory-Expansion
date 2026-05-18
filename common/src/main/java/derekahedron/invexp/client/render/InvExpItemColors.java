package derekahedron.invexp.client.render;

import derekahedron.invexp.client.util.ClientServices;
import derekahedron.invexp.item.InvExpItems;
import net.minecraft.world.item.DyeableLeatherItem;

/**
 * Holds item colors (like sack color) for Inventory Expansion.
 */
public class InvExpItemColors {

    /**
     * Initializes item color behavior.
     */
    public static void init() {
        ClientServices.ITEM_COLORS_REGISTRAR.register(
                (stack, tintIndex) -> tintIndex > 0
                        ? -1
                        : ((DyeableLeatherItem) stack.getItem()).getColor(stack),
                InvExpItems.SACK);
    }
}
