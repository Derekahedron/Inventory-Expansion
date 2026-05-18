package derekahedron.invexp.mixin;

import derekahedron.invexp.block.dispenser.ContainerItemDispenseItemBehavior;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@SuppressWarnings("ModifyVariableMayUseName")
@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {

    /**
     * Avoids empty container items that have dispenser behavior when selecting a stack to dispense.
     * This is done to prevent these empty items from doing nothing when there are other items that could be dispensed.
     * We do not want dispensers to dispense empty sacks and quivers, so if one is selected,
     * we re-roll except this time avoiding empty containers. This is done after the initial roll
     * to avoid modifying the vanilla code unless necessary.
     */
    @ModifyVariable(
            method = "dispenseFrom",
            at = @At("STORE"),
            ordinal = 0)
    private int avoidEmptyContainers(int i, ServerLevel level, BlockPos pos) {
        if (i == -1) return i;

        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(level, pos);
        DispenserBlockEntity dispenser = blocksourceimpl.getEntity();
        ContainerItemContentsWriter contents = invexp$contentsOf(dispenser.getItem(i));

        // Only recalculate if the selected item has contents but is not empty
        if (contents != null && contents.isEmpty()) {
            i = -1;
            int tries = 0;

            for (int slotId = 0; slotId < ((DispenserBlockEntityAccessor) dispenser).invexp$getItems().size(); slotId++) {
                ItemStack stack = dispenser.getItem(slotId);
                contents = invexp$contentsOf(stack);

                // Use vanilla logic for getting random slot, only this time avoiding empty contents
                if (!stack.isEmpty() && (contents == null || !contents.isEmpty())) {
                    tries++;

                    if (level.random.nextInt(tries++) == 0) {
                        i = slotId;
                    }
                }
            }
        }
        return i;
    }

    /**
     * Gets the contents of a stack if it is a container item that has dispenser behavior.
     *
     * @param stack the ItemStack to check the contents of
     * @return the contents of the stack if there are any; <code>null</code> otherwise
     */
    @Unique
    @Nullable
    ContainerItemContentsWriter invexp$contentsOf(ItemStack stack) {
        //noinspection ConstantValue
        return ContainerItemBehaviors.getContents(stack)
                .filter(contents ->
                        DispenserBlockAccessor.invexp$getDispenserRegistry().getOrDefault(
                                contents.getContainerStack().getItem(), null
                        ) instanceof ContainerItemDispenseItemBehavior)
                .orElse(null);
    }
}
