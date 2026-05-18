package derekahedron.invexp.mixin;

import derekahedron.invexp.entity.PlayerEntityDuck;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    /**
     * Starts player using container item before they release hold on an item.
     */
    @Inject(
            method = "handlePlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V",
                    shift = At.Shift.AFTER))
    private void beforeReleaseUseItem(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        if (packet.getAction() == ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM) {
            ((PlayerEntityDuck) ((ServerGamePacketListenerImpl) (Object) this).player).invexp$startUsingContainerItem();
        }
    }

    /**
     * Stops player using container item after they release hold on an item.
     */
    @Inject(
            method = "handlePlayerAction",
            at = @At("RETURN"))
    private void afterReleaseUseItem(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        if (packet.getAction() == ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM) {
            ((PlayerEntityDuck) ((ServerGamePacketListenerImpl) (Object) this).player).invexp$stopUsingContainerItem();
        }
    }

    /**
     * Starts player using container item before they interact with an item.
     */
    @Inject(
            method = "handleUseItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V",
                    shift = At.Shift.AFTER))
    private void beforeInteractItem(ServerboundUseItemPacket packet, CallbackInfo ci) {
        ((PlayerEntityDuck) ((ServerGamePacketListenerImpl) (Object) this).player).invexp$startUsingContainerItem();
    }

    /**
     * Stops player using container item after they interact with an item.
     */
    @Inject(
            method = "handleUseItem",
            at = @At("RETURN"))
    private void afterInteractItem(ServerboundUseItemPacket packet, CallbackInfo ci) {
        ((PlayerEntityDuck) ((ServerGamePacketListenerImpl) (Object) this).player).invexp$stopUsingContainerItem();
    }

    /**
     * Starts player using container item before they interact with a block.
     */
    @Inject(
            method = "handleUseItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V",
                    shift = At.Shift.AFTER))
    private void beforeInteractBlock(ServerboundUseItemOnPacket packet, CallbackInfo ci) {
        ((PlayerEntityDuck) ((ServerGamePacketListenerImpl) (Object) this).player).invexp$startUsingContainerItem();
    }

    /**
     * Stops player using container item after they interact with a block.
     */
    @Inject(
            method = "handleUseItemOn",
            at = @At("RETURN"))
    private void afterInteractBlock(ServerboundUseItemOnPacket packet, CallbackInfo ci) {
        ((PlayerEntityDuck) ((ServerGamePacketListenerImpl) (Object) this).player).invexp$stopUsingContainerItem();
    }

    /**
     * Starts player using container item before they interact with an entity.
     */
    @Inject(
            method = "handleInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/server/level/ServerLevel;)V",
                    shift = At.Shift.AFTER))
    private void beforeInteractEntity(ServerboundInteractPacket packet, CallbackInfo ci) {
        ((PlayerEntityDuck) ((ServerGamePacketListenerImpl) (Object) this).player).invexp$startUsingContainerItem();
    }

    /**
     * Stops player using container item after they interact with an entity.
     */
    @Inject(
            method = "handleInteract",
            at = @At("RETURN"))
    private void afterInteractEntity(ServerboundInteractPacket packet, CallbackInfo ci) {
        ((PlayerEntityDuck) ((ServerGamePacketListenerImpl) (Object) this).player).invexp$stopUsingContainerItem();
    }
}
