package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.entity.PlayerEntityDuck;
import derekahedron.invexp.containeritem.ContainerItemUsage;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemUtils.class)
public class ItemUtilsMixin {

    /**
     * Exchanges the ItemStack being swapped out with the ItemStack being used by the container item.
     * This allows for things like filling a bottle with water or filling a bucket to happen eloquently without the
     * new item being inserted into the player's inventory.
     */
    @Inject(
            method = "createFilledResult(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"),
            cancellable = true)
    private static void exchange(
            ItemStack emptyStack,
            Player player,
            ItemStack filledStack,
            boolean preventDuplicates,
            CallbackInfoReturnable<ItemStack> cir) {
        if (player.isCreative()) {
            if (preventDuplicates) {
                for (List<ItemStack> stacks : ((InventoryAccessor) player.getInventory()).invexp$getCompartments()) {
                    for (ItemStack stack : stacks) {
                        ContainerItemContentsReader contents = ContainerItemBehaviors.getUsableContents(stack)
                                .orElse(null);

                        if (contents != null && !contents.isEmpty()) {
                            for (ItemStack nestedStack : contents.getStacks()) {
                                if (!nestedStack.isEmpty() && ItemStack.isSameItem(nestedStack, filledStack)) {
                                    cir.setReturnValue(emptyStack);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            return;
        }

        if (emptyStack.getCount() <= 1) return;

        ContainerItemUsage usage = ((PlayerEntityDuck) player).invexp$getUsageForSelectedStack(emptyStack);

        if (usage == null
                || !ItemStack.isSameItemSameTags(usage.originalSelectedStack, usage.selectedStack)
                || usage.originalSelectedStack.getCount() <= 1) {
            return;
        }

        ContainerItemContentsWriter contents = ContainerItemBehaviors.getUsableContents(usage.containerStack)
                .orElse(null);
        if (contents == null || contents.isEmpty()) return;

        if (contents.remove(usage.selectedStack.copyWithCount(1)) == 0) return;

        usage.originalSelectedStack.shrink(1);
        emptyStack.shrink(1);
        contents.add(filledStack);

        if (!filledStack.isEmpty() && !player.getInventory().add(filledStack)) {
            player.drop(filledStack, false);
        }
        cir.setReturnValue(emptyStack);
    }
}
