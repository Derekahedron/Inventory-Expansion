package derekahedron.invexp.forge.platform;

import derekahedron.invexp.platform.services.IGameplayHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ForgeGameplayHooks implements IGameplayHooks {

    public ItemStack getProjectile(LivingEntity entity, ItemStack weapon, ItemStack projectile) {
        return net.minecraftforge.common.ForgeHooks.getProjectile(entity, weapon, projectile);
    }
}
