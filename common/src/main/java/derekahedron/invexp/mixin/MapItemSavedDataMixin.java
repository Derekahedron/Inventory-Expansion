package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(MapItemSavedData.class)
public class MapItemSavedDataMixin {

    @Unique
    @Nullable
    Player invexp$trackedPlayer;

    /**
     *
     */
    @ModifyArg(
            method = "tickCarriedBy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;contains(Lnet/minecraft/world/item/ItemStack;)Z"))
    private ItemStack containsMap(ItemStack mapStack) {
        if (invexp$trackedPlayer == null) {
            return mapStack;
        }

        for (List<ItemStack> stacks : ((InventoryAccessor) invexp$trackedPlayer.getInventory()).invexp$getCompartments()) {
            for (ItemStack stack : stacks) {
                ContainerItemContentsReader contents = ContainerItemBehaviors.getUsableContents(stack)
                        .orElse(null);

                if (contents != null) {
                    for (ItemStack nestedStack : contents.getStacks()) {
                        if (!nestedStack.isEmpty() && ItemStack.isSameItemSameTags(nestedStack, mapStack)) {
                            return stack;
                        }
                    }
                }
            }
        }

        return mapStack;
    }

    /**
     *
     */
    @Inject(
            method = "tickCarriedBy",
            at = @At("HEAD"))
    private void setTrackedPlayer(Player player, ItemStack mapStack, CallbackInfo ci) {
        invexp$trackedPlayer = player;
    }

    /**
     * Tracks player holding map so we can
     */
    @ModifyVariable(
            method = "tickCarriedBy",
            at = @At("STORE"))
    private MapItemSavedData.HoldingPlayer setTrackedPlayer(MapItemSavedData.HoldingPlayer mapitemsaveddata$holdingplayer) {
        invexp$trackedPlayer = mapitemsaveddata$holdingplayer.player;
        return mapitemsaveddata$holdingplayer;
    }
}
