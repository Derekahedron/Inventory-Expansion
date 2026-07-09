package derekahedron.invexp.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import derekahedron.invexp.client.util.InvExpClientUtil;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.entity.PlayerEntityDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@SuppressWarnings({"MixinExtrasUnnecessaryMutableLocal", "LocalMayUseName"})
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;

    /**
     * Starts player using container item before they use an item.
     */
    @Inject(
            method = "startUseItem",
            at = @At("HEAD"))
    private void beforeItemUse(CallbackInfo ci) {
        Minecraft self = (Minecraft) (Object) this;

        if (self.player != null) {
            ((PlayerEntityDuck) self.player).invexp$startUsingContainerItem();
        }
    }

    /**
     * Stops player using container item after they use an item.
     */
    @Inject(
            method = "startUseItem",
            at = @At("RETURN"))
    private void afterItemUse(CallbackInfo ci) {
        Minecraft self = (Minecraft) (Object) this;

        if (self.player != null) {
            ((PlayerEntityDuck) self.player).invexp$stopUsingContainerItem();
        }
    }

    /**
     * Injects into the pick block to handle picking a container item that contains the desired item.
     */
    @Inject(
            method = "pickBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;findSlotMatchingItem(Lnet/minecraft/world/item/ItemStack;)I"),
            cancellable = true)
    private void afterPickedStack(CallbackInfo ci, @Local LocalRef<ItemStack> stackRef) {
        ItemStack stack = stackRef.get();
        if (player == null || stack == null || gameMode == null) return;
        Inventory inventory = player.getInventory();
        int newSelectedIndex = -1;
        int i = -1;

        // If the selected item is a sack with the selected stack matching the given stack, use that index always
        ContainerItemContentsWriter contents = ContainerItemBehaviors.getUsableContents(inventory.items.get(inventory.selected))
                .orElse(null);
        if (contents != null) {
            newSelectedIndex = contents.indexOf(stack, contents.getSelectedIndex());

            if (newSelectedIndex == contents.getSelectedIndex()) {
                i = inventory.selected;
            } else if (newSelectedIndex != -1
                    && InvExpClientUtil.sendSetSelectedIndexPacket(player, contents.getContainerStack(), newSelectedIndex)) {
                contents.setSelectedIndex(newSelectedIndex);
                i = inventory.selected;
            }
        }

        // From here, only continue if nothing was found
        if (i == -1) {
            i = inventory.findSlotMatchingItem(stack);

            if (i != -1) return;

            int backupSlot = -1;

            for (int slot = 0; slot < inventory.items.size(); slot++) {
                contents = ContainerItemBehaviors.getUsableContents(inventory.items.get(slot))
                        .orElse(null);

                if (contents != null && !contents.isEmpty()) {
                    if (ItemStack.isSameItemSameTags(stack, contents.getSelectedStack())) {
                        // Find first sack that has the item selected already
                        i = slot;
                        break;
                    } else if (backupSlot == -1) {
                        // Otherwise, if a backup hasn't been found, test if the item is in the stack
                        newSelectedIndex = contents.indexOf(stack, contents.getSelectedIndex());
                        if (newSelectedIndex != -1) {
                            backupSlot = slot;
                        }
                    }
                }
            }

            // If there is a backup, set the selected index of that backup and return the slot.
            if (i != -1 && backupSlot != -1) {
                contents = ContainerItemBehaviors.getUsableContents(inventory.items.get(backupSlot))
                        .orElse(null);

                if (contents != null
                        && InvExpClientUtil.sendSetSelectedIndexPacket(player, contents.getContainerStack(), newSelectedIndex)) {
                    contents.setSelectedIndex(newSelectedIndex);
                    i = backupSlot;
                }
            }
        }

        if (i == -1) return;

        if (player.getAbilities().instabuild) {
            inventory.setPickedItem(stack);
            gameMode.handleCreativeModeItemAdd(player.getItemInHand(InteractionHand.MAIN_HAND), 36 + inventory.selected);
        } else {
            if (Inventory.isHotbarSlot(i)) {
                inventory.selected = i;
            } else {
                gameMode.handlePickItem(i);
            }
        }
        ci.cancel();
    }
}
