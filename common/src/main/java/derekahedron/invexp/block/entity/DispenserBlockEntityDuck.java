package derekahedron.invexp.block.entity;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Adds setter method to Dispensers for setting a buffer that grabs inserted items and adds them back to
 * the container item.
 */
public interface DispenserBlockEntityDuck {

    /**
     * Sets the usage buffer on a dispenser.
     *
     * @param usageBuffer a list of {@linkplain ItemStack ItemStacks} that are to be added back to the container item.
     */
    void invexp$setUsageBuffer(@Nullable ArrayList<ItemStack> usageBuffer);
}
