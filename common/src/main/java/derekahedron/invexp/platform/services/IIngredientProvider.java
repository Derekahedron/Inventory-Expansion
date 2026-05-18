package derekahedron.invexp.platform.services;

import com.mojang.datafixers.util.Either;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

/**
 * Service that fetches different ingredients using tags from the different mod loaders.
 */
public interface IIngredientProvider {

    /**
     * Gets the ingredient for string.
     *
     * @return either an item or a tag for string
     */
    default Either<ItemLike, TagKey<Item>> getString() {
        return Either.left(Items.STRING);
    }

    /**
     * Gets the ingredient for leather.
     *
     * @return either an item or a tag for leather
     */
    default Either<ItemLike, TagKey<Item>> getLeather() {
        return Either.left(Items.LEATHER);
    }

    /**
     * Gets the dye ingredient for the given color.
     *
     * @param color the dye color to get the item for
     * @return either an item or a tag for the dye
     */
    default Either<ItemLike, TagKey<Item>> getDye(DyeColor color) {
        return switch (color) {
            case WHITE -> Either.left(Items.WHITE_DYE);
            case ORANGE -> Either.left(Items.ORANGE_DYE);
            case MAGENTA -> Either.left(Items.MAGENTA_DYE);
            case LIGHT_BLUE -> Either.left(Items.LIGHT_BLUE_DYE);
            case YELLOW -> Either.left(Items.YELLOW_DYE);
            case LIME -> Either.left(Items.LIME_DYE);
            case PINK -> Either.left(Items.PINK_DYE);
            case GRAY -> Either.left(Items.GRAY_DYE);
            case LIGHT_GRAY -> Either.left(Items.LIGHT_GRAY_DYE);
            case CYAN -> Either.left(Items.CYAN_DYE);
            case PURPLE -> Either.left(Items.PURPLE_DYE);
            case BLUE -> Either.left(Items.BLUE_DYE);
            case BROWN -> Either.left(Items.BROWN_DYE);
            case GREEN -> Either.left(Items.GREEN_DYE);
            case RED -> Either.left(Items.RED_DYE);
            case BLACK -> Either.left(Items.BLACK_DYE);
        };
    }
}
