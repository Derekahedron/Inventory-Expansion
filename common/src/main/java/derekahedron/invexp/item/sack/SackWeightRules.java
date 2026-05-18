package derekahedron.invexp.item.sack;

import derekahedron.invexp.item.InvExpItemTags;
import derekahedron.invexp.registry.InvExpRegistryKeys;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

/**
 * Holds the Sack Weight Rules used in Inventory Expansion.
 */
public class SackWeightRules {

    public static final ResourceKey<SackWeightRule> DOUBLE = of("double");
    public static final ResourceKey<SackWeightRule> HALF = of("half");
    public static final ResourceKey<SackWeightRule> THIRD = of("third");
    public static final ResourceKey<SackWeightRule> FOURTH = of("fourth");
    public static final ResourceKey<SackWeightRule> FIFTH = of("fifth");

    public static final ResourceKey<SackWeightRule> BEES = of("bees");
    public static final ResourceKey<SackWeightRule> POTION = of("water_bottle");

    public static ResourceKey<SackWeightRule> of(String path) {
        return ResourceKey.create(InvExpRegistryKeys.SACK_WEIGHT_RULE, InvExpUtil.location(path));
    }

    /**
     * Bootstraps the built-in expected rules for Inventory Expansion.
     *
     * @param context the context to apply the rules to
     */
    public static void bootstrap(BootstapContext<SackWeightRule> context) {

        context.register(DOUBLE, new SackWeightRule(InvExpItemTags.SackWeight.DOUBLE, Fraction.getFraction(2)));
        context.register(HALF, new SackWeightRule(InvExpItemTags.SackWeight.HALF, Fraction.ONE_HALF));
        context.register(THIRD, new SackWeightRule(InvExpItemTags.SackWeight.THIRD, Fraction.ONE_THIRD));
        context.register(FOURTH, new SackWeightRule(InvExpItemTags.SackWeight.FOURTH, Fraction.ONE_QUARTER));
        context.register(FIFTH, new SackWeightRule(InvExpItemTags.SackWeight.FIFTH, Fraction.ONE_FIFTH));

        context.register(
                BEES,
                new SackWeightRule(
                        Optional.of(10),
                        Optional.of(Ingredient.of(Items.BEEHIVE, Items.BEE_NEST)),
                        Optional.of(ItemPredicate.Builder.item().hasNbt(getBeesTag()).build()),
                        Optional.of(Fraction.getFraction(64))));
        context.register(
                POTION,
                new SackWeightRule(
                        Optional.of(10),
                        Optional.of(Ingredient.of(Items.POTION)),
                        Optional.of(ItemPredicate.Builder.item().isPotion(Potions.WATER).build()),
                        Optional.of(Fraction.ONE_QUARTER)));
    }

    /**
     * Create a tag that can be used to test if a hive has bees.
     *
     * @return the tag with bees in it
     */
    public static CompoundTag getBeesTag() {
        CompoundTag entityData = new CompoundTag();
        entityData.putString("id", "minecraft:bee");
        CompoundTag bee = new CompoundTag();
        bee.put("EntityData", entityData);
        ListTag bees = new ListTag();
        bees.add(bee);
        CompoundTag blockEntityTag = new CompoundTag();
        blockEntityTag.put("Bees", bees);
        CompoundTag tag = new CompoundTag();
        tag.put("BlockEntityTag", blockEntityTag);
        return tag;
    }
}
