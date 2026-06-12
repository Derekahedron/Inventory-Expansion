package derekahedron.invexp.fabric.client.compat;

import derekahedron.invexp.item.InvExpItems;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.world.entity.EntityType;

/**
 * Entrypoint for compatibility with Trinkets on the client side.
 */
public class TrinketsCompatClient {

    /**
     * Initializes Trinkets on the client.
     */
    public static void init() {
        // Register the quiver renderer
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, registrationHelper, context) -> {
                    if (entityType == EntityType.PLAYER) {
                        TrinketRendererRegistry.registerRenderer(
                                InvExpItems.QUIVER.get(),
                                new QuiverTrinketRenderer());
                    }
                });
    }
}