package derekahedron.invexp.platform.services;

import derekahedron.invexp.mixin.BucketItemAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

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

    /**
     * Hook for getting the fluid from a bucket.
     *
     * @param item the bucket to get fluid for
     * @return the fluid in the bucket
     */
    default Fluid getFluid(BucketItem item) {
        return ((BucketItemAccessor) item).invexp$getContent();
    }
}
