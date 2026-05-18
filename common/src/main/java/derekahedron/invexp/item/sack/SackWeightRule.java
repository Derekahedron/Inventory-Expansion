package derekahedron.invexp.item.sack;

import derekahedron.invexp.util.InvExpCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

/**
 * Rules for what sack weight an {@link ItemStack} should have.
 *
 * @param priority an optional <code>int</code> that determines if this rule has priority over other matching rules. <code>0</code> if not defined
 * @param items an optional {@link Ingredient} that this rule is applied to. Is applied to all if not given
 * @param predicate an optional {@link ItemPredicate} that controls whether this sack weight applies to an {@link ItemStack}
 * @param sackWeight an optional {@link Fraction} that represents what weight in a sack an {@link ItemStack} holds
 */
public record SackWeightRule(
        Optional<Integer> priority,
        Optional<Ingredient> items,
        Optional<ItemPredicate> predicate,
        Optional<Fraction> sackWeight) implements Comparable<SackWeightRule> {

    public static final Codec<SackWeightRule> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT
                            .optionalFieldOf("priority")
                            .forGetter(SackWeightRule::priority),
                    InvExpCodecs.INGREDIENT
                            .optionalFieldOf("items")
                            .forGetter(SackWeightRule::items),
                    InvExpCodecs.ITEM_PREDICATE
                            .optionalFieldOf("predicate")
                            .forGetter(SackWeightRule::predicate),
                    InvExpCodecs.FRACTION
                            .optionalFieldOf("sack_weight")
                            .forGetter(SackWeightRule::sackWeight)
            ).apply(instance, SackWeightRule::new));

    public SackWeightRule(TagKey<Item> tag, Fraction sackWeight) {
        this(
                Optional.empty(),
                Optional.of(Ingredient.of(tag)),
                Optional.empty(),
                Optional.of(sackWeight));
    }

    /**
     * Test if this rule should be applied to the given item. Does not test for ingredients.
     *
     * @param stack the ItemStack to test
     * @return whether the predicate in this rule applies to the given item
     */
    public boolean test(ItemStack stack) {
        return predicate.isEmpty() || predicate.get().matches(stack);
    }

    @Override
    public int compareTo(SackWeightRule other) {
        return priority.orElse(0) - other.priority.orElse(0);
    }
}
