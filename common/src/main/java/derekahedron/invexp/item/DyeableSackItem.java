package derekahedron.invexp.item;

import derekahedron.invexp.item.sack.SackItem;
import net.minecraft.world.item.DyeableLeatherItem;

/**
 * A Sack that can be dyed.
 */
public class DyeableSackItem extends SackItem implements DyeableLeatherItem {

    public DyeableSackItem(Properties properties) {
        super(properties);
    }
}
