package derekahedron.invexp.platform.services;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

/**
 * Service for registering recipe serializers.
 */
public interface IRecipeSerializerRegistrar {

    /**
     * Register a recipe serializer.
     *
     * @param path the path to register under
     * @param supplier the supplier for the recipe serializer
     * @return the supplier for the registered recipe serializer
     * @param <T> the type of recipe to register
     */
    <T extends Recipe<?>> Supplier<RecipeSerializer<T>> register(String path, Supplier<RecipeSerializer<T>> supplier);
}
