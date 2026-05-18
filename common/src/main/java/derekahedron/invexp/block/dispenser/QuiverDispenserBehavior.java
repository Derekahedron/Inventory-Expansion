package derekahedron.invexp.block.dispenser;

import derekahedron.invexp.item.quiver.QuiverContentsWriter;
import derekahedron.invexp.mixin.DispenserBlockAccessor;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;

/**
 * Dispenser Behavior for quivers to dispense their selected arrow.
 */
public class QuiverDispenserBehavior extends ContainerItemDispenseItemBehavior {
    public static final DefaultDispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

    /**
     * Dispenser behavior for quivers.
     *
     * @param pointer the {@link BlockSource} pointer to the dispenser
     * @param stack the Quiver stack that is being dispensed
     * @return the Quiver stack after dispense
     */
    @Override
    public final ItemStack dispense(BlockSource pointer, ItemStack stack) {
        QuiverContentsWriter contents = QuiverContentsWriter.of(stack);
        // Fail dispense if invalid or empty
        if (contents == null || contents.isEmpty()) {
            setSuccess(false);
            playSound(pointer);
            playAnimation(pointer, pointer.getBlockState().getValue(DispenserBlock.FACING));
            return stack;
        }

        ItemStack selectedStack = contents.getSelectedStack().copy();
        DispenseItemBehavior behavior = DispenserBlockAccessor.invexp$getDispenserRegistry()
                .getOrDefault(selectedStack.getItem(), null);

        if (behavior instanceof AbstractProjectileDispenseBehavior projectileBehavior) {
            // Use projectile behavior if it exists
            selectedStack = projectileBehavior.dispense(pointer, selectedStack);
        } else {
            // If, somehow, the quiver has an item without a projectile behavior, dispense
            // regularly
            selectedStack = super.dispense(pointer, selectedStack);
        }

        // Update selected stack and try to add remainder back into quiver
        contents.updateSelectedStack(selectedStack, (itemStack -> {
            contents.add(itemStack);
            if (!itemStack.isEmpty()) {
                if (((DispenserBlockEntity) pointer.getEntity()).addItem(itemStack) < 0) {
                    defaultBehavior.dispense(pointer, itemStack);
                }
            }
        }));
        return stack;
    }
}
