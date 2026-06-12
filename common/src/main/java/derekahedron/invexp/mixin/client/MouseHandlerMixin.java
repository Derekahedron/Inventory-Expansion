package derekahedron.invexp.mixin.client;

import derekahedron.invexp.client.util.QuickSwapHandler;
import derekahedron.invexp.containeritem.ContainerItemContentsSelector;
import derekahedron.invexp.client.util.Scroller;
import derekahedron.invexp.network.SetSelectedIndexPacket;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.util.ModdedInventoriesEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.stream.Stream;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    /**
     * Injects on scroll and scrolls the selected container item instead.
     */
    @ModifyArg(
            method = "onScroll",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;swapPaint(D)V"),
            index = 0)
    private double onScrollQuickSwap(double direction) {
        if (!QuickSwapHandler.isActive(minecraft.player)) return direction;

        ContainerItemContentsSelector selector = ContainerItemContentsSelector.getSelector(minecraft.player)
                .orElse(null);

        if (selector == null) {
            // Block scrolling here so we don't get "caught" scrolling when scrolling onto a container item
            QuickSwapHandler.blockScrolling = true;
            return direction;
        }

        int numScrolled = (int) Math.signum(direction);
        if (numScrolled == 0) return 0;

        int newIndex = Scroller.scrollCycling(
                numScrolled,
                selector.selectedIndex,
                selector.compressedStacks.size());
        if (newIndex == selector.selectedIndex) return 0;

        int newSelectedIndex = selector.contents.indexOf(
                selector.compressedStacks.get(newIndex),
                selector.contents.getSelectedIndex());

        ModdedInventoriesEvent.SelectedIndexSlotHandler handler = Stream.concat(
                        ModdedInventoriesEvent.getHandlers(minecraft.player),
                        minecraft.player.containerMenu.slots.stream()
                                .map(slot -> new ModdedInventoriesEvent.SelectedIndexSlotHandler() {
                                    @Override
                                    public ItemStack getStack() {
                                        return slot.getItem();
                                    }

                                    @Override
                                    public void setSelectedIndex(int selectedIndex) {
                                        Services.NETWORK_HANDLER.sendC2S(new SetSelectedIndexPacket(slot.index, selectedIndex));
                                    }
                                }))
                .filter(h -> h.getStack() == selector.contents.getContainerStack())
                .findFirst()
                .orElse(null);

        if (handler == null) {
            return 0;
        }

        selector.contents.setSelectedIndex(newSelectedIndex);
        handler.setSelectedIndex(newSelectedIndex);
        return 0;
    }
}
