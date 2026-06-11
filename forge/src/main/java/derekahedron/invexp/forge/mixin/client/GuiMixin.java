package derekahedron.invexp.forge.mixin.client;

import derekahedron.invexp.client.util.QuickSwapHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow
    @Final
    protected Minecraft minecraft;

    /**
     * Adds an offset to the selected item text so it doesn't overlap with the quick swap GUI.
     */
    @ModifyVariable(
            method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;I)V",
            at = @At("STORE"),
            remap = false,
            name = "k")
    private int moveSelectedItemTextUp(int k) {
        return k + QuickSwapHandler.getSelectedItemNameOffset(minecraft);
    }
}
