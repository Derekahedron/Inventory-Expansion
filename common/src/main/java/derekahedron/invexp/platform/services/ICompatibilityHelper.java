package derekahedron.invexp.platform.services;

import derekahedron.invexp.InventoryExpansion;

/**
 * Service for checking if mods are present and running logic if they are.
 */
public interface ICompatibilityHelper {

    /**
     * Runs the given code if the given mod is present.
     *
     * @param modId the mod id of the mod to check for
     * @param runnable the code to run if the mod is present
     */
    default void runIfPresent(String modId, Runnable runnable) {
        if (isModLoaded(modId)) {
            try {
                runnable.run();
            } catch (NoClassDefFoundError e) {
                InventoryExpansion.LOGGER.error("Error loading mod with id: {}. {}", modId, e.getMessage());
            }
        }
    }

    /**
     * Checks if a given mod is present.
     *
     * @param modId the mod id of the mod to check for
     * @return <code>true</code> if the mod is present; <code>false</code> otherwise
     */
    boolean isModLoaded(String modId);
}
