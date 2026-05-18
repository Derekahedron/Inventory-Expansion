package derekahedron.invexp.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.math.Fraction;

/**
 * Holds codecs used in Inventory Expansion.
 */
public class InvExpCodecs {

    public static final Codec<Fraction> FRACTION = new Codec<>() {

        public static final Codec<Fraction> NUMERATOR_DENOMINATOR_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("numerator").forGetter(Fraction::getNumerator),
                Codec.INT.fieldOf("denominator").forGetter(Fraction::getDenominator)
        ).apply(instance, Fraction::getFraction));

        @Override
        public <T> DataResult<Pair<Fraction, T>> decode(DynamicOps<T> ops, T input) {
            DataResult<Pair<Fraction, T>> result;
            try {
                result = Codec.DOUBLE.map(Fraction::getFraction).decode(ops, input);
                if (result.result().isPresent()) return result;
            } catch (Exception e) {
                return DataResult.error(e::getMessage);
            }
            try {
                result = Codec.INT.map(Fraction::getFraction).decode(ops, input);
                if (result.result().isPresent()) return result;
            } catch (Exception e) {
                return DataResult.error(e::getMessage);
            }
            try {
                result = Codec.STRING.map(Fraction::getFraction).decode(ops, input);
                if (result.result().isPresent()) return result;
            } catch (Exception e) {
                return DataResult.error(e::getMessage);
            }
            try {
                result = NUMERATOR_DENOMINATOR_CODEC.decode(ops, input);
                if (result.result().isPresent()) return result;
            } catch (Exception e) {
                return DataResult.error(e::getMessage);
            }
            return DataResult.error(() -> "Not a fraction: " + input);
        }

        @Override
        public <T> DataResult<T> encode(Fraction fraction, DynamicOps<T> ops, T prefix) {
            if (fraction.getDenominator() == 1) {
                return Codec.INT.comap(Fraction::getNumerator).encode(fraction, ops, prefix);
            } else {
                return NUMERATOR_DENOMINATOR_CODEC.encode(fraction, ops, prefix);
            }
        }
    };

    public static final Codec<Ingredient> INGREDIENT =
            ExtraCodecs.JSON.xmap(Ingredient::fromJson, Ingredient::toJson);

    public static final Codec<ItemPredicate> ITEM_PREDICATE =
            ExtraCodecs.JSON.xmap(
                    ItemPredicate::fromJson,
                    (itemPredicate) -> {
                        JsonElement json = itemPredicate.serializeToJson();
                        if (json instanceof JsonObject jsonObject) {
                            jsonObject.entrySet().removeIf(entry -> entry.getValue().isJsonNull());
                        }
                        return json;
                    });
}
