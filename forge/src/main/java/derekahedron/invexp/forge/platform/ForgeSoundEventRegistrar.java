package derekahedron.invexp.forge.platform;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.platform.services.ISoundEventRegistrar;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeSoundEventRegistrar implements ISoundEventRegistrar {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, InventoryExpansion.MOD_ID);

    @Override
    public Supplier<SoundEvent> register(String path, Supplier<SoundEvent> supplier) {
        return SOUND_EVENTS.register(path, supplier);
    }
}
