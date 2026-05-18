package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin {

    /**
     * Prevents items with container items inside them from being put into shulker boxes.
     * This doesn't really have a use in vanilla, but can possibly prevent infinite stacking for mods.
     */
    @Inject(
            method = "mayPlace",
            at = @At("RETURN"),
            cancellable = true)
    private void mayPlaceContainerItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        ContainerItemContentsWriter contents = ContainerItemBehaviors.getContents(stack)
                .orElse(null);

        if (contents != null && !contents.isEmpty()) {
            ShulkerBoxSlot self = (ShulkerBoxSlot) (Object) this;

            for (ItemStack nestedStack : contents.getStacks()) {
                if (!self.mayPlace(nestedStack)) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }
}
