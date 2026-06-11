package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ShootableContents;
import derekahedron.invexp.containeritem.ContainerItemContentsWriter;
import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemUsage;
import derekahedron.invexp.entity.PlayerEntityDuck;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.util.ExtraInventoryRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerEntityDuck {

    @Unique
    private boolean invexp$usingContainerItem = false;
    @Unique
    @Nullable
    private ContainerItemUsage invexp$mainHandContainerItemUsage;
    @Unique
    @Nullable
    private ContainerItemUsage invexp$offHandContainerItemUsage;

    /**
     * Returns the selected  item from a container item if the player is already using a container item.
     */
    @Inject(
            method = "getItemBySlot",
            at = @At("RETURN"),
            cancellable = true)
    private void getEquippedStackInSack(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (invexp$usingContainerItem && (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)) {
            ContainerItemUsage usage = invexp$getUsageForContainerStack(cir.getReturnValue());
            if (usage != null) {
                cir.setReturnValue(usage.selectedStack);
            }
        }
    }

    /**
     * Sets the selected item inside a container item usage if the user is using a container item.
     */
    @Inject(
            method = "setItemSlot",
            at = @At("HEAD"),
            cancellable = true)
    private void equipStackInContainerItem(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        if (invexp$usingContainerItem && (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND)) {
            Player self = (Player) (Object) this;
            // Get the stack that is being held
            ItemStack heldStack;
            switch (slot) {
                case MAINHAND -> heldStack = self.getInventory().getSelected();
                case OFFHAND -> heldStack = self.getInventory().offhand.get(0);
                default -> throw new IllegalArgumentException("Invalid slot " + slot);
            }
            // If the stack in the slot is a sack being used, replace there instead
            ContainerItemUsage usage = invexp$getUsageForContainerStack(heldStack);
            if (usage != null) {
                self.onEquipItem(slot, usage.selectedStack, stack);
                usage.selectedStack = stack;
                ci.cancel();
            }
        }
    }

    @Override
    public boolean invexp$isUsingContainerItem() {
        return invexp$usingContainerItem;
    }

    @Override
    public void invexp$startUsingContainerItem() {
        if (invexp$usingContainerItem) {
            invexp$stopUsingContainerItem();
        }

        Player self = (Player) (Object) this;
        ContainerItemUsage[] usages = new ContainerItemUsage[InteractionHand.values().length];

        for (int i = 0; i < InteractionHand.values().length; i++) {
            ItemStack heldStack = self.getItemInHand(InteractionHand.values()[i]);
            ContainerItemContentsWriter contents = ContainerItemBehaviors.getUsableContents(heldStack)
                    .orElse(null);

            if (contents != null && !contents.isEmpty()) {
                ContainerItemUsage usage = invexp$getUsageForContainerStack(heldStack);
                if (usage != null) {
                    usages[i] = new ContainerItemUsage(contents, usage.selectedStack);
                } else {
                    usages[i] = new ContainerItemUsage(contents);
                }
            }
        }

        for (int i = 0; i < InteractionHand.values().length; i++) {
            invexp$setUsageByHand(InteractionHand.values()[i], usages[i]);
        }

        invexp$usingContainerItem = true;
    }

    @Override
    public void invexp$stopUsingContainerItem() {
        if (!invexp$usingContainerItem) {
            return;
        }

        ArrayList<ItemStack> leftoverStacks = new ArrayList<>();
        Player self = (Player) (Object) this;
        for (InteractionHand hand : InteractionHand.values()) {
            ContainerItemUsage usage = invexp$getUsageByHand(hand);
            if (usage != null) {
                usage.update(leftoverStacks::add);
            }
        }

        invexp$usingContainerItem = false;
        for (ItemStack leftoverStack : leftoverStacks) {
            if (!leftoverStack.isEmpty() && !self.getInventory().add(leftoverStack)) {
                self.drop(leftoverStack, false);
            }
        }
    }

    @Override
    @Nullable
    public ContainerItemUsage invexp$getUsageForContainerStack(ItemStack containerStack) {
        for (InteractionHand hand : InteractionHand.values()) {
            ContainerItemUsage usage = invexp$getUsageByHand(hand);
            if (usage != null && usage.containerStack == containerStack) {
                return usage;
            }
        }
        return null;
    }


    @Override
    @Nullable
    public ContainerItemUsage invexp$getUsageForSelectedStack(ItemStack selectedStack) {
        for (InteractionHand hand : InteractionHand.values()) {
            ContainerItemUsage usage = invexp$getUsageByHand(hand);
            if (usage != null && usage.selectedStack == selectedStack) {
                return usage;
            }
        }
        return null;
    }

    /**
     * Sets usage for the given hand.
     *
     * @param hand the hand to set the usage for
     * @param usage the usage to set in the hand
     */
    @Unique
    private void invexp$setUsageByHand(InteractionHand hand, @Nullable ContainerItemUsage usage) {
        switch (hand) {
            case MAIN_HAND -> invexp$mainHandContainerItemUsage = usage;
            case OFF_HAND -> invexp$offHandContainerItemUsage = usage;
            default -> throw new IllegalArgumentException("Invalid hand " + hand);
        }
    }

    /**
     * Gets usage for the given hand.
     *
     * @param hand the hand to get the usage for
     * @return the usage in the given hand
     */
    @Unique
    @Nullable
    private ContainerItemUsage invexp$getUsageByHand(InteractionHand hand) {
        switch (hand) {
            case MAIN_HAND -> {
                return invexp$mainHandContainerItemUsage;
            }
            case OFF_HAND -> {
                return invexp$offHandContainerItemUsage;
            }
            default -> throw new IllegalArgumentException("Invalid hand " + hand);
        }
    }

    /**
     * Returns new ItemStack for a container item with a selected item that matches the
     * given predicate. Returned item stacks have a contents attached to them that is updated when the count changes.
     */
    @Inject(
            method = "getProjectile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ProjectileWeaponItem;getAllSupportedProjectiles()Ljava/util/function/Predicate;"),
            cancellable = true)
    private void getQuiveredProjectile(ItemStack shootable, CallbackInfoReturnable<ItemStack> cir) {
        Player self = (Player) (Object) this;
        Predicate<ItemStack> predicate = ((ProjectileWeaponItem) shootable.getItem()).getAllSupportedProjectiles();

        Stream<ItemStack> items = Stream.of(
                ExtraInventoryRegistry.getExtraInventory(self),
                self.getInventory().items.stream(),
                self.getInventory().armor.stream(),
                self.getInventory().offhand.stream()).flatMap(Function.identity());

        items.map(stack -> ShootableContents.getProjectileStack(stack, predicate))
                .filter(stack -> !stack.isEmpty())
                .findFirst()
                .ifPresent(projectile ->
                        cir.setReturnValue(Services.GAMEPLAY_HOOKS.getProjectile(self, shootable, projectile)));
    }
}
