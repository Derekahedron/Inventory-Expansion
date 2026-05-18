package derekahedron.invexp.platform.services;

import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

/**
 * Service for registering sound events.
 */
public interface ISoundEventRegistrar {

    /**
     * Registers a sound event.
     *
     * @param path the path to register the sound event under
     * @param supplier the supplier for creating the sound event
     * @return the supplier for the registered sound event.
     */
    Supplier<SoundEvent> register(String path, Supplier<SoundEvent> supplier);
}
