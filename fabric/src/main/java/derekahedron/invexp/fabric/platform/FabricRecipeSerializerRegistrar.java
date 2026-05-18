package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IRecipeSerializerRegistrar;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public class FabricRecipeSerializerRegistrar implements IRecipeSerializerRegistrar {

    @Override
    public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> register(String path, Supplier<RecipeSerializer<T>> supplier) {
        RecipeSerializer<T> recipeSerializer = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, InvExpUtil.location(path), supplier.get());
        return () -> recipeSerializer;
    }
}
