package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.ISoundEventRegistrar;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class FabricSoundEventRegistrar implements ISoundEventRegistrar {

    @Override
    public Supplier<SoundEvent> register(String path, Supplier<SoundEvent> supplier) {
        ResourceLocation location = InvExpUtil.location(path);
        SoundEvent soundEvent = Registry.register(BuiltInRegistries.SOUND_EVENT, location, supplier.get());
        return () -> soundEvent;
    }
}
