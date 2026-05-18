package derekahedron.invexp.forge.datagen;

import derekahedron.invexp.InventoryExpansion;
import derekahedron.invexp.item.InvExpItems;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class InvExpItemModelProvider extends ItemModelProvider {

    public InvExpItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, InventoryExpansion.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        sack(InvExpItems.SACK.get());
        quiver(InvExpItems.QUIVER.get());
        bundle(InvExpItems.WHITE_BUNDLE.get());
        bundle(InvExpItems.ORANGE_BUNDLE.get());
        bundle(InvExpItems.MAGENTA_BUNDLE.get());
        bundle(InvExpItems.LIGHT_BLUE_BUNDLE.get());
        bundle(InvExpItems.YELLOW_BUNDLE.get());
        bundle(InvExpItems.LIME_BUNDLE.get());
        bundle(InvExpItems.PINK_BUNDLE.get());
        bundle(InvExpItems.GRAY_BUNDLE.get());
        bundle(InvExpItems.LIGHT_GRAY_BUNDLE.get());
        bundle(InvExpItems.PURPLE_BUNDLE.get());
        bundle(InvExpItems.CYAN_BUNDLE.get());
        bundle(InvExpItems.BLUE_BUNDLE.get());
        bundle(InvExpItems.BROWN_BUNDLE.get());
        bundle(InvExpItems.GREEN_BUNDLE.get());
        bundle(InvExpItems.RED_BUNDLE.get());
        bundle(InvExpItems.BLACK_BUNDLE.get());

        // Backport textures to vanilla bundle
        ResourceLocation bundleId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(Items.BUNDLE));

        getBuilder(bundleId + "_open_front")
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", InvExpUtil.location("item/" + bundleId.getPath() + "_open_front"));
        getBuilder(bundleId + "_open_back")
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", InvExpUtil.location("item/" + bundleId.getPath() + "_open_back"));
    }

    public void sack(Item item) {
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        basicItem(id)
                .texture("layer1", id.withPrefix("item/").withSuffix("_overlay"));
        basicItem(id.withSuffix("_open_front"))
                .texture("layer1", id.withPrefix("item/").withSuffix("_open_front_overlay"));
        basicItem(id.withSuffix("_open_back"));
    }

    public void quiver(Item item) {
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        ModelFile filledModel = basicItem(id.withSuffix("_filled"));
        basicItem(id)
                .override()
                .predicate(InvExpUtil.location("quiver/has_contents"), 1.0F)
                .model(filledModel)
                .end();
    }

    public void bundle(Item item) {
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        basicItem(id);
        basicItem(id.withSuffix("_open_front"));
        basicItem(id.withSuffix("_open_back"));
    }
}
