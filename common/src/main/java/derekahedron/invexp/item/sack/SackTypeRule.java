package derekahedron.invexp.item.sack;

import derekahedron.invexp.registry.InvExpRegistryKeys;
import derekahedron.invexp.util.InvExpCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;
/**
 * Rules for what sack weight an {@link ItemStack} should have.
 *
 * @param priority an optional <code>int</code> that determines if this rule has priority over other matching rules.
 *                 <code>0</code> if not defined
 * @param items an optional {@link Ingredient} that this rule is applied to. Is applied to all if not given
 * @param predicate an optional {@link ItemPredicate} that controls whether this sack type applies to an {@link ItemStack}
 * @param sackType an optional {@link ResourceKey<SackType>} that represents what type an {@link ItemStack} is assigned;
 *                 <code>empty</code> for no sack type
 */
public record SackTypeRule(
        Optional<Integer> priority,
        Optional<Ingredient> items,
        Optional<ItemPredicate> predicate,
        Optional<ResourceKey<SackType>> sackType) implements Comparable<SackTypeRule> {

    public static final Codec<SackTypeRule> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT
                            .optionalFieldOf("priority")
                            .forGetter(SackTypeRule::priority),
                    InvExpCodecs.INGREDIENT
                            .optionalFieldOf("items")
                            .forGetter(SackTypeRule::items),
                    InvExpCodecs.ITEM_PREDICATE
                            .optionalFieldOf("predicate")
                            .forGetter(SackTypeRule::predicate),
                    ResourceKey.codec(InvExpRegistryKeys.SACK_TYPE)
                            .optionalFieldOf("sack_type")
                            .forGetter(SackTypeRule::sackType)
            ).apply(instance, SackTypeRule::new));

    public SackTypeRule(int priority, Ingredient items, ResourceKey<SackType> sackType) {
        this(Optional.of(priority), Optional.of(items), Optional.empty(), Optional.of(sackType));
    }

    public SackTypeRule(TagKey<Item> tag, ResourceKey<SackType> sackType) {
        this(Optional.empty(), Optional.of(Ingredient.of(tag)), Optional.empty(), Optional.of(sackType));
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
    public int compareTo(SackTypeRule other) {
        return priority.orElse(0) - other.priority.orElse(0);
    }
}
