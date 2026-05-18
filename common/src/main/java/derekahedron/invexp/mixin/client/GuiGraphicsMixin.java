package derekahedron.invexp.mixin.client;

import derekahedron.invexp.client.util.OpenItemTextures;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.item.bundle.BetterBundleItem;
import derekahedron.invexp.item.bundle.BundleContentsWriter;
import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@SuppressWarnings("ModifyVariableMayUseName")
@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    /**
     * Draws cooldown progress for the selected stack if it exists, otherwise draw for the
     * original stack.
     */
    @ModifyVariable(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("STORE"),
            ordinal = 0)
    private float drawCooldownProgressForSelectedStack(
            float f,
            Font font,
            ItemStack stack,
            int x,
            int y,
            @Nullable String text) {
        if (minecraft.player == null) return f;

        ContainerItemContentsReader contents = ContainerItemBehaviors.getUsableContents(stack)
                .orElse(null);

        if (contents == null || contents.isEmpty()) return f;

        return minecraft.player.getCooldowns()
                .getCooldownPercent(contents.getSelectedStack().getItem(), minecraft.getFrameTime());
    }


    /**
     * Renders the bunder as open when there is a selected index.
     */
    @ModifyVariable(
            method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
            at = @At("HEAD"),
            argsOnly = true)
    private ItemStack renderOpenBundle(
            ItemStack bundleStack,
            @Nullable LivingEntity entity,
            @Nullable Level level,
            ItemStack stack,
            int x,
            int y,
            int seed,
            int guiOffset) {
        if (!stack.is(Items.BUNDLE) && !(stack.getItem() instanceof BetterBundleItem)) {
            return bundleStack;
        }
        BundleContentsWriter contents = BundleContentsWriter.of(bundleStack);
        if (contents == null || contents.isEmpty() || contents.getSelectedIndex() == -1) {
            return bundleStack;
        }

        GuiGraphics self = (GuiGraphics) (Object) this;
        OpenItemTextures.renderOpenItem(self, bundleStack, x, y, level, entity, seed);
        return contents.getSelectedStack();
    }
}
