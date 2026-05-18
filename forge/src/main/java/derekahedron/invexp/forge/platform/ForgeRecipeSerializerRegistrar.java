package derekahedron.invexp.forge.platform;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.platform.services.IRecipeSerializerRegistrar;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeRecipeSerializerRegistrar implements IRecipeSerializerRegistrar {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, InventoryExpansion.MOD_ID);

    @Override
    public <T extends Recipe<?>> Supplier<RecipeSerializer<T>> register(String path, Supplier<RecipeSerializer<T>> supplier) {
        return RECIPE_SERIALIZERS.register(path, supplier);
    }
}
