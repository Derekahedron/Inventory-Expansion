package derekahedron.invexp.forge.platform;

import com.mojang.datafixers.util.Either;
import derekahedron.invexp.platform.services.IIngredientProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

public class ForgeIngredientProvider implements IIngredientProvider {

    @Override
    public Either<ItemLike, TagKey<Item>> getString() {
        return Either.right(Tags.Items.STRING);
    }

    @Override
    public Either<ItemLike, TagKey<Item>> getLeather() {
        return Either.right(Tags.Items.LEATHER);
    }

    @Override
    public Either<ItemLike, TagKey<Item>> getDye(DyeColor color) {
        return switch (color) {
            case WHITE -> Either.right(Tags.Items.DYES_WHITE);
            case ORANGE -> Either.right(Tags.Items.DYES_ORANGE);
            case MAGENTA -> Either.right(Tags.Items.DYES_MAGENTA);
            case LIGHT_BLUE -> Either.right(Tags.Items.DYES_LIGHT_BLUE);
            case YELLOW -> Either.right(Tags.Items.DYES_YELLOW);
            case LIME -> Either.right(Tags.Items.DYES_LIME);
            case PINK -> Either.right(Tags.Items.DYES_PINK);
            case GRAY -> Either.right(Tags.Items.DYES_GRAY);
            case LIGHT_GRAY -> Either.right(Tags.Items.DYES_LIGHT_GRAY);
            case CYAN -> Either.right(Tags.Items.DYES_CYAN);
            case PURPLE -> Either.right(Tags.Items.DYES_PURPLE);
            case BLUE -> Either.right(Tags.Items.DYES_BLUE);
            case BROWN -> Either.right(Tags.Items.DYES_BROWN);
            case GREEN -> Either.right(Tags.Items.DYES_GREEN);
            case RED -> Either.right(Tags.Items.DYES_RED);
            case BLACK -> Either.right(Tags.Items.DYES_BLACK);
        };
    }
}
