package derekahedron.invexp.item;

import derekahedron.invexp.platform.Services;
import derekahedron.invexp.platform.services.ICreativeItemsRegistrar;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.util.List;
import java.util.function.Supplier;

/**
 * Controls which items are added to the creative inventory and where they are added.
 */
public class InvExpCreativeItems {

    public static void init() {
        Services.CREATIVE_ITEMS_REGISTRAR.register(
                of("combat"),
                new ICreativeItemsRegistrar.PutAfter(
                        of(Items.CROSSBOW),
                        List.of(
                                of(InvExpItems.QUIVER))));

        Services.CREATIVE_ITEMS_REGISTRAR.register(
                of(new ResourceLocation("tools_and_utilities")),
                new ICreativeItemsRegistrar.PutAfter(
                        of(Items.LEAD),
                        List.of(
                                of(Items.BUNDLE),
                                of(InvExpItems.WHITE_BUNDLE),
                                of(InvExpItems.LIGHT_BLUE_BUNDLE),
                                of(InvExpItems.GRAY_BUNDLE),
                                of(InvExpItems.BLACK_BUNDLE),
                                of(InvExpItems.BROWN_BUNDLE),
                                of(InvExpItems.RED_BUNDLE),
                                of(InvExpItems.ORANGE_BUNDLE),
                                of(InvExpItems.YELLOW_BUNDLE),
                                of(InvExpItems.LIME_BUNDLE),
                                of(InvExpItems.GREEN_BUNDLE),
                                of(InvExpItems.CYAN_BUNDLE),
                                of(InvExpItems.LIGHT_BLUE_BUNDLE),
                                of(InvExpItems.BLUE_BUNDLE),
                                of(InvExpItems.PURPLE_BUNDLE),
                                of(InvExpItems.MAGENTA_BUNDLE),
                                of(InvExpItems.PINK_BUNDLE),
                                of(InvExpItems.SACK))));
    }

    public static ResourceKey<CreativeModeTab> of(String path) {
        return of(new ResourceLocation(path));
    }

    public static ResourceKey<CreativeModeTab> of(ResourceLocation id) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, id);
    }

    static Supplier<ItemStack> of(Item item) {
        return () -> new ItemStack(item);
    }

    static Supplier<ItemStack> of(Supplier<Item> item) {
        return () -> new ItemStack(item.get());
    }
}
