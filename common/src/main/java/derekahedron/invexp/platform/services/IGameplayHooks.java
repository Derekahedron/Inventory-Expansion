package derekahedron.invexp.platform.services;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Provides general gameplay hooks.
 */
public interface IGameplayHooks {

    /**
     * Hook for when a projectile is gotten from an inventory.
     *
     * @param entity the entity the projectile is from
     * @param weapon the weapon shooting the projectile
     * @param projectile the projectile sent
     * @return the final projectile after modifications
     */
    default ItemStack getProjectile(LivingEntity entity, ItemStack weapon, ItemStack projectile) {
        return projectile;
    }
}
