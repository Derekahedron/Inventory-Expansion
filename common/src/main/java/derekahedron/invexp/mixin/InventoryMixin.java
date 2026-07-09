package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.containeritem.InsertableContents;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.entity.PlayerEntityDuck;
import derekahedron.invexp.containeritem.ContainerItemUsage;
import derekahedron.invexp.util.ModdedInventoriesEvent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    @Shadow
    @Final
    public NonNullList<ItemStack> items;

    @Shadow
    @Final
    public Player player;

    @Shadow
    public int selected;

    @Shadow
    public abstract ItemStack getSelected();

    /**
     * Inserts an item into the container items in a players inventory with priority to the container items.
     */
    @Inject(
            method = "add(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true)
    private void insertIntoContainerItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Inventory self = (Inventory) (Object) this;

        // Gathers list of all container item contents in the inventory, starting with main hand/offhand, then
        // the rest of the inventory. Also sort by priority so quivers get inserted into first.
        Stream<ItemStack> items = Stream.of(
                Stream.of(self.getItem(selected), self.getItem(40)),
                ModdedInventoriesEvent.getItemStacks(self.player),
                self.items.stream()
                        .filter(itemStack -> itemStack != self.getItem(selected))
        ).flatMap(Function.identity());

        List<ContainerItemContentsWriter> contentsList = items
                .map(ContainerItemBehaviors::getInsertableContents)
                .filter(Optional::isPresent).map(Optional::get)
                .filter(contents -> ((InsertableContents) contents).canInsert(stack))
                .sorted(Comparator.comparing(contents -> -((InsertableContents) contents).getPickupPriority()))
                .toList();

        for (ContainerItemContentsWriter contents : contentsList) {
            contents.add(stack);

            if (stack.isEmpty()) {
                cir.setReturnValue(true);
                return;
            }
        }
    }

    /**
     * When removing an item from the player inventory, if the player is currently using
     * the sack, remove that item if it is in one of the players usages.
     */
    @Inject(
            method = "removeItem(Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true)
    private void removeFromContainerItem(ItemStack stack, CallbackInfo ci) {
        Inventory self = (Inventory) (Object) this;

        if (((PlayerEntityDuck) self.player).invexp$isUsingContainerItem()) {
            ContainerItemUsage usage = ((PlayerEntityDuck) self.player).invexp$getUsageForSelectedStack(stack);

            if (usage != null) {
                usage.selectedStack = ItemStack.EMPTY;
                ci.cancel();
            }
        }
    }

    /**
     * Drops a selected item from a container item instead of dropping the entire container item.
     * If the entire stack should be dropped, drop it anyway.
     */
    @Inject(
            method = "removeFromSelected",
            at = @At("HEAD"),
            cancellable = true)
    private void dropSelectedItemFromContainerItem(boolean removeStack, CallbackInfoReturnable<ItemStack> info) {
        // Dropping the entire stack should drop the container item
        if (removeStack) return;

        Inventory self = (Inventory) (Object) this;
        ItemStack sackStack = self.getSelected();
        ContainerItemContentsWriter contents = ContainerItemBehaviors.getUsableContents(sackStack)
                .orElse(null);

        if (contents != null && !contents.isEmpty()) {
            info.setReturnValue(contents.popSelectedItem());
        }
    }

    /**
     * Gets an item slot with the container item containing a picked item in creative mode.
     */
    @ModifyVariable(
            method = "setPickedItem",
            at = @At("STORE"),
            ordinal = 0)
    private int getAlternateSlot(int i, ItemStack stack) {
        if (ContainerItemBehaviors.getUsableContents(items.get(selected))
                .filter(contents -> ItemStack.isSameItemSameTags(stack, contents.getSelectedStack()))
                .isPresent()) {
            return selected;
        }

        if (i != -1) return i;

        for (int slot = 0; slot < items.size(); slot++) {
            ContainerItemContentsReader contents = ContainerItemBehaviors.getUsableContents(items.get(slot))
                    .orElse(null);

            if (contents != null && !contents.isEmpty()) {
                if (ItemStack.isSameItemSameTags(stack, contents.getSelectedStack())) {
                    // Find first sack that has the item selected already
                    return slot;
                }
            }
        }

        return i;
    }
}
