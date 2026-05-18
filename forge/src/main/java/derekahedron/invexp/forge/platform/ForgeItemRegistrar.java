package derekahedron.invexp.forge.platform;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.platform.services.IItemRegistrar;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeItemRegistrar implements IItemRegistrar {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, InventoryExpansion.MOD_ID);

    @Override
    public Supplier<Item> register(String path, Supplier<Item> supplier) {
        return ITEMS.register(path, supplier);
    }
}
