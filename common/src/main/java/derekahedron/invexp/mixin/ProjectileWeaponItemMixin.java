package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ShootableContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {

    /**
     * Gets the held projectile from a container item.
     */
    @Inject(
            method = "getHeldProjectile",
            at = @At("HEAD"),
            cancellable = true)
    private static void getHeldProjectileFromContainerItem(
            LivingEntity shooter,
            Predicate<ItemStack> isAmmo,
            CallbackInfoReturnable<ItemStack> cir) {
        // Check hands in reverse (Offhand first)
        for (int i = InteractionHand.values().length - 1; i >= 0; i--) {
            InteractionHand hand = InteractionHand.values()[i];
            ItemStack projectile = ShootableContents.getProjectileStack(
                    shooter.getItemInHand(hand), isAmmo);

            if (!projectile.isEmpty()) {
                cir.setReturnValue(projectile);
                return;
            }
        }
    }
}
