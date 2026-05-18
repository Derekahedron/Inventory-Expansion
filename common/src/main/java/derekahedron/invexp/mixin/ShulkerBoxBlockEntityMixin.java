package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin {

    /**
     * Prevents items with container items inside them from being put into shulker boxes.
     * This doesn't really have a use in vanilla, but can possibly prevent infinite stacking for mods.
     */
    @Inject(
            method = "canPlaceItemThroughFace",
            at = @At("RETURN"),
            cancellable = true)
    private void canPlaceContainerItemThroughFace(
            int index,
            ItemStack itemStack,
            Direction direction,
            CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        ContainerItemContentsWriter contents = ContainerItemBehaviors.getContents(itemStack)
                .orElse(null);

        if (contents != null && !contents.isEmpty()) {
            ShulkerBoxBlockEntity self = (ShulkerBoxBlockEntity) (Object) this;

            for (ItemStack nestedStack : contents.getStacks()) {
                if (!self.canPlaceItemThroughFace(index, nestedStack, direction)) {
                    cir.setReturnValue(false);
                    return;
                }
            }
        }
    }
}
