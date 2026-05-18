package derekahedron.invexp.recipe;

import derekahedron.invexp.platform.Services;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

/**
 * Holds all recipe serializers for Inventory Expansion.
 */
@SuppressWarnings("EmptyMethod")
public class InvExpRecipeSerializers {
    public static final Supplier<RecipeSerializer<DyeBundleRecipe>> DYE_BUNDLE_RECIPE =
            Services.RECIPE_SERIALIZER_REGISTRAR.register("dye_bundle", DyeBundleRecipe.Serializer::new);

    /**
     * Initializes recipe serializers.
     */
    public static void init() {
        // Do Nothing: Load Class
    }
}
