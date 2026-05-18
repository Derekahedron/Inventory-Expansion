package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings("InjectLocalCaptureCanBeReplacedWithLocal")
@Mixin(MapItem.class)
public class MapItemMixin {

    /**
     * Update maps if they are inside container items in the offhand.
     */
    @Inject(
            method = "inventoryTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getOffhandItem()Lnet/minecraft/world/item/ItemStack;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void tickInOffhandSack(
            ItemStack stack,
            Level level,
            Entity entity,
            int itemSlot,
            boolean isSelected,
            CallbackInfo ci,
            MapItemSavedData mapState) {
        // Same logic for checking maps as vanilla
        if (!mapState.locked && !isSelected && entity instanceof Player player) {
            ContainerItemContentsReader contents = ContainerItemBehaviors.getUsableContents(
                    player.getOffhandItem())
                    .orElse(null);

            if (contents != null
                    && !contents.isEmpty()
                    && ItemStack.matches(stack, contents.getSelectedStack())) {
                ((MapItem) (Object) this).update(level, entity, mapState);
            }
        }
    }
}
