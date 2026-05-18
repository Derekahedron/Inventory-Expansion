package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IItemColorRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FabricItemColorRegistrar implements IItemColorRegistrar {

    private static final List<ItemColorRegistration> REGISTRATIONS = new ArrayList<>();

    @Override
    public void register(ItemColor itemColor, Supplier<Item> item) {
        REGISTRATIONS.add(new ItemColorRegistration(itemColor, item));
    }

    /**
     * Initializes the item colors.
     */
    public static void init() {
        for (ItemColorRegistration registration : REGISTRATIONS) {
            ColorProviderRegistry.ITEM.register(registration.itemColor, registration.item.get());
        }
    }

    public record ItemColorRegistration(ItemColor itemColor, Supplier<Item> item) {}
}
