package derekahedron.invexp.mixin;

import derekahedron.invexp.containeritem.ContainerItemBehaviors;
import derekahedron.invexp.containeritem.ContainerItemItemPredicate;
import derekahedron.invexp.containeritem.ContainerItemContentsReader;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@SuppressWarnings("ModifyVariableMayUseName")
@Mixin(InventoryChangeTrigger.TriggerInstance.class)
public class InventoryChangeTriggerTriggerInstanceMixin {

    @Shadow
    @Final
    private ItemPredicate[] predicates;

    /**
     * Modifies the predicate list so all predicates also test container item contents
     */
    @ModifyVariable(
            method = "matches",
            at = @At("STORE"))
    private List<ItemPredicate> modifyPredicateList(List<ItemPredicate> list) {
        List<ItemPredicate> modified = new ObjectArrayList<>(list.size());
        for (ItemPredicate predicate : list) {
            modified.add(new ContainerItemItemPredicate(predicate));
        }
        return modified;
    }

    /**
     * Modifies the item to be tested to a nested item that will pass the test
     */
    @ModifyArg(
            method = "matches",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/advancements/critereon/ItemPredicate;matches(Lnet/minecraft/world/item/ItemStack;)Z"),
            index = 0)
    private ItemStack changeTestItem(ItemStack stack) {
        ContainerItemContentsReader contents = ContainerItemBehaviors.getInsertableContents(stack)
                .orElse(null);

        if (contents != null && !contents.isEmpty()) {
            for (ItemStack nestedStack : contents.getStacks()) {
                if (predicates[0].matches(nestedStack)) {
                    return nestedStack;
                }
            }
        }
        return stack;
    }
}
