package derekahedron.invexp.fabric.mixin.client;

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
    private Minecraft minecraft;

    /**
     * Adds an offset to the selected item text so it doesn't overlap with the quick swap GUI.
     */
    @ModifyVariable(
            method = "renderSelectedItemName",
            at = @At("STORE"),
            ordinal = 2)
    private int moveSelectedItemTextUp(int k) {
        return k + QuickSwapHandler.getSelectedItemNameOffset(minecraft);
    }
}
