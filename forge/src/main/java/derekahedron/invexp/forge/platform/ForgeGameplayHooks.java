package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IGameplayHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeHooks;

public class ForgeGameplayHooks implements IGameplayHooks {

    @Override
    public ItemStack getProjectile(LivingEntity entity, ItemStack weapon, ItemStack projectile) {
        return ForgeHooks.getProjectile(entity, weapon, projectile);
    }

    @Override
    public Fluid getFluid(BucketItem item) {
        return item.getFluid();
    }
}
