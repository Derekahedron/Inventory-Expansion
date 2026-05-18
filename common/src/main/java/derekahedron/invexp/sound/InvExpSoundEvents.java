package derekahedron.invexp.sound;

import derekahedron.invexp.platform.Services;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

/**
 * Holds all the sound events for Inventory Expansion.
 */
@SuppressWarnings("EmptyMethod")
public class InvExpSoundEvents {

    public static final Supplier<SoundEvent> ITEM_SACK_INSERT =
            register("item.sack.insert");
    public static final Supplier<SoundEvent> ITEM_SACK_REMOVE_ONE =
            register("item.sack.remove_one");
    public static final Supplier<SoundEvent> ITEM_QUIVER_INSERT =
            register("item.quiver.insert");
    public static final Supplier<SoundEvent> ITEM_QUIVER_REMOVE_ONE =
            register("item.quiver.remove_one");

    /**
     * Registers an Inventory Expansion sound event under a given path.
     *
     * @param path a {@link String} to register the sound event under
     * @return the {@link SoundEvent} that was created and registered
     */
    public static Supplier<SoundEvent> register(String path) {
        return Services.SOUND_EVENT_REGISTRAR.register(path, () ->
                SoundEvent.createVariableRangeEvent(InvExpUtil.location(path)));
    }

    /**
     * Initializes sound events.
     */
    public static void init() {
        // Do Nothing: Load Class
    }
}
