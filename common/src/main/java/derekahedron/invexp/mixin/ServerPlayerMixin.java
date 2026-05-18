package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    /**
     * Sends packets for all the items inside a container item per tick for complex items like maps.
     */
    @Inject(
            method = "doTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;getContainerSize()I"))
    private void sendContainerItemPackets(CallbackInfo ci) {
        ServerPlayer self = (ServerPlayer) (Object) this;

        for (int i = 0; i < self.getInventory().getContainerSize(); i++) {
            ContainerItemContentsReader contents = ContainerItemBehaviors.getUsableContents(self.getInventory().getItem(i))
                    .orElse(null);

            if (contents != null && !contents.isEmpty()) {
                for (ItemStack nestedStack : contents.getStacks()) {
                    if (nestedStack.getItem() instanceof ComplexItem complexItem) {
                        Packet<?> packet = complexItem.getUpdatePacket(nestedStack, self.level(), self);
                        if (packet != null) {
                            self.connection.send(packet);
                        }
                    }
                }
            }
        }
    }
}
