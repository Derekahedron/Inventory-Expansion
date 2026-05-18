package derekahedron.invexp.mixin.client;

import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    /**
     * Displays a "popping" animation when an item is picked up into a container item. We do so by comparing the counts
     * of all the items before and after the change.
     */
    @Inject(
            method = "handleContainerSetSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z",
                    ordinal = 1))
    private void changeCountBeforeComparison (
            ClientboundContainerSetSlotPacket packet,
            CallbackInfo ci) {
        if (minecraft.player == null) return;

        ItemStack newStack = packet.getItem();
        ItemStack oldStack = minecraft.player.containerMenu.getSlot(packet.getSlot()).getItem();

        // Only allow pop animation when the items are the same
        if (newStack.getItem() != oldStack.getItem()) return;

        ContainerItemContentsReader oldContents = ContainerItemBehaviors.getInsertableContents(oldStack)
                .orElse(null);
        ContainerItemContentsReader newContents = ContainerItemBehaviors.getInsertableContents(newStack)
                .orElse(null);

        if (oldContents == null || newContents == null) return;

        // Compare contents count. If there is an increase, display animation
        int oldCount = 0;
        int newCount = 0;
        for (ItemStack stack : oldContents.getStacks()) {
            oldCount += stack.getCount();
        }
        for (ItemStack stack : newContents.getStacks()) {
            newCount += stack.getCount();
        }
        if (newCount > oldCount) {
            newStack.setPopTime(5);
        }
    }
}
