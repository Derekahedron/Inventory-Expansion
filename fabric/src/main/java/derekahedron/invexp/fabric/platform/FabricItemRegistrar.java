package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IItemRegistrar;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FabricItemRegistrar implements IItemRegistrar {

    @Override
    public Supplier<Item> register(String path, Supplier<Item> supplier) {
        Item item = Registry.register(BuiltInRegistries.ITEM, InvExpUtil.location(path), supplier.get());
        return () -> item;
    }
}
