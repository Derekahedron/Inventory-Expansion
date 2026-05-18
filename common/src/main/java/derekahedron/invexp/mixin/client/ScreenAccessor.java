package derekahedron.invexp.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import javax.annotation.Nullable;

@Mixin(Screen.class)
public interface ScreenAccessor {

    @Accessor(value = "minecraft")
    @Nullable
    Minecraft invexp$getMinecraft();

    @Accessor(value = "font")
    Font invexp$getFont();
}
