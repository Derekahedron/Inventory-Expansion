package derekahedron.invexp.fabric.platform;

import derekahedron.invexp.platform.services.IItemOverrideRegistrar;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FabricItemOverrideRegistrar implements IItemOverrideRegistrar {

    private static final List<ItemOverrideRegistration> REGISTRATIONS = new ArrayList<>();

    @Override
    public void register(
            Supplier<Item> item,
            ResourceLocation resourceLocation,
            ClampedItemPropertyFunction itemPropertyFunction) {
        REGISTRATIONS.add(new ItemOverrideRegistration(item, resourceLocation, itemPropertyFunction));
    }

    /**
     * Initializes item model overrides.
     */
    public static void init() {
        for (ItemOverrideRegistration registration : REGISTRATIONS) {
            ItemProperties.register(
                    registration.item.get(),
                    registration.resourceLocation,
                    registration.itemPropertyFunction);
        }
    }

    public record ItemOverrideRegistration(Supplier<Item> item, ResourceLocation resourceLocation, ClampedItemPropertyFunction itemPropertyFunction) {
    }
}
