package derekahedron.invexp.datagen.recipe;

import com.mojang.datafixers.util.Either;
import derekahedron.invexp.item.InvExpItemTags;
import derekahedron.invexp.item.InvExpItems;
import derekahedron.invexp.platform.Services;
import derekahedron.invexp.recipe.DyeBundleRecipe;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

/**
 * Creates recipes for Inventory Expansion.
 */
public class InvExpRecipeProvider extends RecipeProvider {

    public InvExpRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS,
                        InvExpItems.SACK.get())
                .define('-', ingredient(Services.INGREDIENT_PROVIDER.getString()))
                .define('#', ingredient(Services.INGREDIENT_PROVIDER.getLeather()))
                .pattern("-#-")
                .pattern("# #")
                .pattern("###")
                .unlockedBy(
                        getHasName(Items.STRING),
                        has(Services.INGREDIENT_PROVIDER.getString()))
                .save(consumer, InvExpUtil.location(InvExpItems.SACK.get().toString()));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS,
                        InvExpItems.QUIVER.get())
                .define('-', ingredient(Services.INGREDIENT_PROVIDER.getString()))
                .define('X', Items.RABBIT_HIDE)
                .define('#', ingredient(Services.INGREDIENT_PROVIDER.getLeather()))
                .pattern(" XX")
                .pattern("-##")
                .pattern(" ##")
                .unlockedBy(
                        getHasName(Items.ARROW),
                        has(ItemTags.ARROWS))
                .save(consumer, InvExpUtil.location(InvExpItems.QUIVER.get().toString()));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS,
                        Items.BUNDLE)
                .define('-', ingredient(Services.INGREDIENT_PROVIDER.getString()))
                .define('#', ingredient(Services.INGREDIENT_PROVIDER.getLeather()))
                .pattern("-")
                .pattern("#")
                .unlockedBy(
                        getHasName(Items.STRING),
                        has(Services.INGREDIENT_PROVIDER.getString()))
                .save(consumer, InvExpUtil.location(Items.BUNDLE.toString()));

        dyeBundle(consumer, InvExpItems.WHITE_BUNDLE.get(), (DyeItem) Items.WHITE_DYE);
        dyeBundle(consumer, InvExpItems.ORANGE_BUNDLE.get(), (DyeItem) Items.ORANGE_DYE);
        dyeBundle(consumer, InvExpItems.MAGENTA_BUNDLE.get(), (DyeItem) Items.MAGENTA_DYE);
        dyeBundle(consumer, InvExpItems.LIGHT_BLUE_BUNDLE.get(), (DyeItem) Items.LIGHT_BLUE_DYE);
        dyeBundle(consumer, InvExpItems.YELLOW_BUNDLE.get(), (DyeItem) Items.YELLOW_DYE);
        dyeBundle(consumer, InvExpItems.LIME_BUNDLE.get(), (DyeItem) Items.LIME_DYE);
        dyeBundle(consumer, InvExpItems.PINK_BUNDLE.get(), (DyeItem) Items.PINK_DYE);
        dyeBundle(consumer, InvExpItems.GRAY_BUNDLE.get(), (DyeItem) Items.GRAY_DYE);
        dyeBundle(consumer, InvExpItems.LIGHT_GRAY_BUNDLE.get(), (DyeItem) Items.LIGHT_GRAY_DYE);
        dyeBundle(consumer, InvExpItems.CYAN_BUNDLE.get(), (DyeItem) Items.CYAN_DYE);
        dyeBundle(consumer, InvExpItems.PURPLE_BUNDLE.get(), (DyeItem) Items.PURPLE_DYE);
        dyeBundle(consumer, InvExpItems.BLUE_BUNDLE.get(), (DyeItem) Items.BLUE_DYE);
        dyeBundle(consumer, InvExpItems.BROWN_BUNDLE.get(), (DyeItem) Items.BROWN_DYE);
        dyeBundle(consumer, InvExpItems.GREEN_BUNDLE.get(), (DyeItem) Items.GREEN_DYE);
        dyeBundle(consumer, InvExpItems.RED_BUNDLE.get(), (DyeItem) Items.RED_DYE);
        dyeBundle(consumer, InvExpItems.BLACK_BUNDLE.get(), (DyeItem) Items.BLACK_DYE);
    }

    /**
     * Creates a dye bundler recipe.
     *
     * @param consumer accepts a created recipe to be added to the data
     * @param bundle the bundle item being dyed
     * @param dye the dye item that is dying the bundle
     */
    public static void dyeBundle(Consumer<FinishedRecipe> consumer, ItemLike bundle, DyeItem dye) {
        new DyeBundleRecipe.Builder(RecipeCategory.TOOLS,
                bundle,
                Ingredient.of(InvExpItemTags.DYEABLE_BUNDLES),
                ingredient(Services.INGREDIENT_PROVIDER.getDye(dye.getDyeColor())))
                .group("bundle_dye")
                .unlockedBy(
                        getHasName(dye),
                        has(Services.INGREDIENT_PROVIDER.getDye(dye.getDyeColor())))
                .save(consumer, InvExpUtil.location(bundle.asItem().toString()));
    }

    /**
     * Creates a trigger instance from either an Item or an Item tag.
     *
     * @param item either an {@link ItemLike} or {@link TagKey<Item>} to be turned into a trigger instance
     * @return the created trigger instance
     */
    public static InventoryChangeTrigger.TriggerInstance has(Either<ItemLike, TagKey<Item>> item) {
        return item.map(RecipeProvider::has, RecipeProvider::has);
    }

    /**
     * Creates an ingredient from either an Item or an Item tag.
     *
     * @param item either an {@link ItemLike} or {@link TagKey<Item>} to be turned into an ingredient
     * @return a created ingredient
     */
    public static Ingredient ingredient(Either<ItemLike, TagKey<Item>> item) {
        return item.map(Ingredient::of, Ingredient::of);
    }
}