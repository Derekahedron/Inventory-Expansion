package derekahedron.invexp.fabric.platform;

import com.mojang.datafixers.util.Either;
import derekahedron.invexp.platform.services.IIngredientProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public class FabricIngredientProvider implements IIngredientProvider {

    @Override
    public Either<ItemLike, TagKey<Item>> getDye(DyeColor color) {
        return switch (color) {
            case WHITE -> Either.right(ConventionalItemTags.WHITE_DYES);
            case ORANGE -> Either.right(ConventionalItemTags.ORANGE_DYES);
            case MAGENTA -> Either.right(ConventionalItemTags.MAGENTA_DYES);
            case LIGHT_BLUE -> Either.right(ConventionalItemTags.LIGHT_BLUE_DYES);
            case YELLOW -> Either.right(ConventionalItemTags.YELLOW_DYES);
            case LIME -> Either.right(ConventionalItemTags.LIME_DYES);
            case PINK -> Either.right(ConventionalItemTags.PINK_DYES);
            case GRAY -> Either.right(ConventionalItemTags.GRAY_DYES);
            case LIGHT_GRAY -> Either.right(ConventionalItemTags.LIGHT_GRAY_DYES);
            case CYAN -> Either.right(ConventionalItemTags.CYAN_DYES);
            case PURPLE -> Either.right(ConventionalItemTags.PURPLE_DYES);
            case BLUE ->  Either.right(ConventionalItemTags.BLUE_DYES);
            case BROWN -> Either.right(ConventionalItemTags.BROWN_DYES);
            case GREEN -> Either.right(ConventionalItemTags.GREEN_DYES);
            case RED -> Either.right(ConventionalItemTags.RED_DYES);
            case BLACK -> Either.right(ConventionalItemTags.BLACK_DYES);
        };
    }
}
