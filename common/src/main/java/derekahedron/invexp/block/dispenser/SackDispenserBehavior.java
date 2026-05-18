package derekahedron.invexp.block.dispenser;

import derekahedron.invexp.block.entity.DispenserBlockEntityDuck;
import derekahedron.invexp.item.sack.SackContentsWriter;
import derekahedron.invexp.mixin.DispenserBlockAccessor;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;

import java.util.ArrayList;

/**
 * Dispenser Behavior for sacks to dispense their selected item.
 */
public class SackDispenserBehavior extends ContainerItemDispenseItemBehavior {
    public static final DefaultDispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

    /**
     * Dispenser behavior for sacks.
     *
     * @param pointer the {@link BlockSource} pointer to the dispenser
     * @param stack the Sack stack that is being dispensed
     * @return the Sack stack after dispense
     */
    @Override
    public ItemStack dispense(BlockSource pointer, ItemStack stack) {
        SackContentsWriter contents = SackContentsWriter.of(stack);
        // Fail dispense if invalid or empty
        if (contents == null || contents.isEmpty()) {
            setSuccess(false);
            playSound(pointer);
            playAnimation(pointer, pointer.getBlockState().getValue(DispenserBlock.FACING));
            return stack;
        }

        // Set buffer to catch all inserted stacks
        ArrayList<ItemStack> usageBuffer = new ArrayList<>();
        ((DispenserBlockEntityDuck) pointer.getEntity()).invexp$setUsageBuffer(usageBuffer);

        ItemStack selectedStack = contents.copySelectedStack();
        DispenseItemBehavior behavior = DispenserBlockAccessor.invexp$getDispenserRegistry().getOrDefault(selectedStack.getItem(), null);
        if (behavior != null) {
            // Use dispenser behavior of selected stack if it exists
            selectedStack = behavior.dispense(pointer, selectedStack);
        }
        else {
            // Otherwise, default to regular dispensing
            selectedStack = super.dispense(pointer, selectedStack);
        }
        // Remove buffer
        ((DispenserBlockEntityDuck) pointer.getEntity()).invexp$setUsageBuffer(null);

        // Update selected stack and try to add remainder back into sack
        contents.updateSelectedStack(selectedStack, (leftoverStack -> {
            if (((DispenserBlockEntity) pointer.getEntity()).addItem(leftoverStack) < 0) {
                defaultBehavior.dispense(pointer, leftoverStack);
            }
        }));

        // Try to add inserted stacks into the sack contents.
        for (ItemStack insertedStack : usageBuffer) {
            contents.add(insertedStack);
            if (!insertedStack.isEmpty()) {
                if (((DispenserBlockEntity) pointer.getEntity()).addItem(insertedStack) < 0) {
                    defaultBehavior.dispense(pointer, insertedStack);
                }
            }
        }
        return stack;
    }
}
