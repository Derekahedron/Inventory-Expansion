package derekahedron.invexp.item.sack;

import derekahedron.invexp.item.ItemDuck;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.registry.InvExpRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.math.Fraction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Manages all sack rules applied to items. Keeps track of a sync id that increases when new data is loaded,
 * and uses that to invalidate existing data. Also manages global rules which are potentially applied to all items.
 */
public class SackRuleManager {

    @Nullable
    private static SackRuleManager INSTANCE;

    private int syncId;
    private final List<Holder.Reference<SackTypeRule>> typeRules;
    private final List<Holder.Reference<SackTypeRule>> globalTypeRules;
    private final List<Holder.Reference<SackWeightRule>> weightRules;
    private final List<Holder.Reference<SackWeightRule>> globalWeightRules;

    public SackRuleManager(RegistryAccess registryAccess) {
        syncId = INSTANCE != null ? INSTANCE.syncId: 0;

        ArrayList<Holder.Reference<SackTypeRule>> types =
                new ArrayList<>(registryAccess.registryOrThrow(InvExpRegistryKeys.SACK_TYPE_RULE).holders()
                        .sorted(SackRuleManager::compareTypes)
                        .toList());
        Collections.reverse(types);

        ArrayList<Holder.Reference<SackWeightRule>> weights =
                new ArrayList<>(registryAccess.registryOrThrow(InvExpRegistryKeys.SACK_WEIGHT_RULE).holders()
                        .sorted(SackRuleManager::compareWeights)
                        .toList());
        Collections.reverse(weights);

        typeRules = types.stream().filter((type) -> type.value().items().isPresent()).toList();
        globalTypeRules = types.stream().filter((type) -> type.value().items().isEmpty()).toList();
        weightRules = weights.stream().filter((weight) -> weight.value().items().isPresent()).toList();
        globalWeightRules = weights.stream().filter((weight) -> weight.value().items().isEmpty()).toList();

        updateSackRules();
    }

    public void updateSackRules() {
        syncId++;

        for (Holder.Reference<SackTypeRule> typeRule : typeRules) {
            typeRule.value().items().ifPresent((items) -> {
                // We load to/from json here to clear the stored item stacks on the Ingredient
                for (ItemStack itemStack : Ingredient.fromJson(items.toJson()).getItems()) {
                    getOrCreateSackRules(itemStack.getItem()).typeRules.add(typeRule);
                }
            });
        }

        for (Holder.Reference<SackWeightRule> weightRule : weightRules) {
            weightRule.value().items().ifPresent((items) -> {
                for (ItemStack itemStack : Ingredient.fromJson(items.toJson()).getItems()) {
                    getOrCreateSackRules(itemStack.getItem()).weightRules.add(weightRule);
                }
            });
        }
    }

    public Optional<ResourceKey<SackType>> getType(ItemStack stack) {
        SackRules defaults = getSackRules(stack.getItem());
        List<Holder.Reference<SackTypeRule>> itemTypeDefaults = defaults != null
                ? defaults.typeRules : List.of();

        int i = 0;
        int j = 0;
        while (i < globalTypeRules.size() || j < itemTypeDefaults.size()) {
            SackTypeRule typeDefault;

            if (i < globalTypeRules.size()
                    && (j >= itemTypeDefaults.size()
                    || compareTypes(globalTypeRules.get(i), itemTypeDefaults.get(j)) > 0)) {
                typeDefault = globalTypeRules.get(i).value();
                i++;
            } else {
                typeDefault = itemTypeDefaults.get(j).value();
                j++;
            }

            if (typeDefault.test(stack)) {
                return typeDefault.sackType();
            }
        }

        if (stack.getItem() instanceof SpawnEggItem) {
            return Optional.of(SackTypes.SPAWN_EGG);
        } else if (stack.getItem() instanceof BucketItem) {
            return Optional.of(SackTypes.BUCKET);
        }
        return Optional.empty();
    }

    public Fraction getWeight(ItemStack stack) {
        SackRules defaults = getSackRules(stack.getItem());
        List<Holder.Reference<SackWeightRule>> itemWeightDefaults = defaults != null
                ? defaults.weightRules : List.of();

        int i = 0;
        int j = 0;
        while (i < globalWeightRules.size() || j < itemWeightDefaults.size()) {
            SackWeightRule weightDefault;

            if (i < globalWeightRules.size()
                    && (j >= itemWeightDefaults.size()
                    || compareWeights(globalWeightRules.get(i), itemWeightDefaults.get(j)) > 0)) {
                weightDefault = globalWeightRules.get(i).value();
                i++;
            } else {
                weightDefault = itemWeightDefaults.get(j).value();
                j++;
            }

            if (weightDefault.test(stack)) {
                return weightDefault.sackWeight().orElse(Fraction.ONE);
            }
        }

        // Default buckets to 1/4th sack weight
        if (stack.getItem() instanceof BucketItem bucketItem
                && !Services.GAMEPLAY_HOOKS.getFluid(bucketItem).isSame(Fluids.EMPTY)) {
            return Fraction.ONE_QUARTER;
        }
        return Fraction.ONE;
    }

    @Nullable
    public SackRules getSackRules(Item item) {
        SackRules defaults = ((ItemDuck) item).invexp$getSackRules();

        // Return null or synced defaults
        if (defaults == null) return null;
        else if (defaults.syncId == syncId) return defaults;

        // If defaults is out of date, clear them to restore memory
        ((ItemDuck) item).invexp$setSackRules(null);
        return null;
    }

    public SackRules getOrCreateSackRules(Item item) {
        SackRules defaults = getSackRules(item);
        if (defaults == null) {
            defaults = new SackRules(new ArrayList<>(), new ArrayList<>(), syncId);
            ((ItemDuck) item).invexp$setSackRules(defaults);
        }
        return defaults;
    }

    /**
     * Gets the static manager instance.
     *
     * @return  static instance of the SackInsertableManager
     */
    @Nullable
    public static SackRuleManager getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the static manager instance.
     *
     * @param instance  instance to set the static manager to
     */
    private static void setInstance(@Nullable SackRuleManager instance) {
        INSTANCE = instance;
    }

    public static void createNewInstance(RegistryAccess registryAccess) {
        setInstance(new SackRuleManager(registryAccess));
    }

    public record SackRules(
            List<Holder.Reference<SackTypeRule>> typeRules,
            List<Holder.Reference<SackWeightRule>> weightRules,
            int syncId) {}

    private static int compareTypes(
            Holder.Reference<SackTypeRule> left,
            Holder.Reference<SackTypeRule> right) {
        int compareResult = left.value().compareTo(right.value());
        return compareResult != 0
                ? compareResult
                : left.key().location().compareTo(right.key().location());
    }

    private static int compareWeights(
            Holder.Reference<SackWeightRule> left,
            Holder.Reference<SackWeightRule> right) {
        int compareResult = left.value().compareTo(right.value());
        return compareResult != 0
                ? compareResult
                : left.key().location().compareTo(right.key().location());
    }
}
