package derekahedron.invexp.fabric.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import derekahedron.invexp.item.InvExpItems;
import derekahedron.invexp.util.InvExpUtil;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FabricInvExpModelProvider extends FabricModelProvider {

    public FabricInvExpModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        BiConsumer<ResourceLocation, Supplier<JsonElement>> output = itemModelGenerators.output;

        sack(output, InvExpItems.SACK.get());
        quiver(output, InvExpItems.QUIVER.get());
        bundle(output, InvExpItems.WHITE_BUNDLE.get());
        bundle(output, InvExpItems.ORANGE_BUNDLE.get());
        bundle(output, InvExpItems.MAGENTA_BUNDLE.get());
        bundle(output, InvExpItems.LIGHT_BLUE_BUNDLE.get());
        bundle(output, InvExpItems.YELLOW_BUNDLE.get());
        bundle(output, InvExpItems.LIME_BUNDLE.get());
        bundle(output, InvExpItems.PINK_BUNDLE.get());
        bundle(output, InvExpItems.GRAY_BUNDLE.get());
        bundle(output, InvExpItems.LIGHT_GRAY_BUNDLE.get());
        bundle(output, InvExpItems.PURPLE_BUNDLE.get());
        bundle(output, InvExpItems.CYAN_BUNDLE.get());
        bundle(output, InvExpItems.BLUE_BUNDLE.get());
        bundle(output, InvExpItems.BROWN_BUNDLE.get());
        bundle(output, InvExpItems.GREEN_BUNDLE.get());
        bundle(output, InvExpItems.RED_BUNDLE.get());
        bundle(output, InvExpItems.BLACK_BUNDLE.get());

        ResourceLocation bundleId = BuiltInRegistries.ITEM.getKey(Items.BUNDLE);

        ResourceLocation openFrontModel = bundleId.withPrefix("item/").withSuffix("_open_front");
        TextureMapping openFrontMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, InvExpUtil.location("item/" + bundleId.getPath() + "_open_front"));
        ModelTemplates.FLAT_ITEM.create(openFrontModel, openFrontMapping, output);

        ResourceLocation openBackModel = bundleId.withPrefix("item/").withSuffix("_open_back");
        TextureMapping openBackMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, InvExpUtil.location("item/" + bundleId.getPath() + "_open_back"));
        ModelTemplates.FLAT_ITEM.create(openBackModel, openBackMapping, output);
    }

    private void sack(BiConsumer<ResourceLocation, Supplier<JsonElement>> output, Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        ResourceLocation modelLoc = id.withPrefix("item/");

        TextureMapping baseMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, modelLoc)
                .put(TextureSlot.LAYER1, modelLoc.withSuffix("_overlay"));
        ModelTemplates.TWO_LAYERED_ITEM.create(modelLoc, baseMapping, output);

        ResourceLocation openFrontLoc = modelLoc.withSuffix("_open_front");
        TextureMapping openFrontMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, openFrontLoc)
                .put(TextureSlot.LAYER1, modelLoc.withSuffix("_open_front_overlay"));
        ModelTemplates.TWO_LAYERED_ITEM.create(openFrontLoc, openFrontMapping, output);

        ResourceLocation openBackLoc = modelLoc.withSuffix("_open_back");
        TextureMapping openBackMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, openBackLoc);
        ModelTemplates.FLAT_ITEM.create(openBackLoc, openBackMapping, output);
    }

    private void quiver(BiConsumer<ResourceLocation, Supplier<JsonElement>> output, Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        ResourceLocation modelLoc = id.withPrefix("item/");

        ResourceLocation filledLoc = modelLoc.withSuffix("_filled");
        TextureMapping filledMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, filledLoc);
        ModelTemplates.FLAT_ITEM.create(filledLoc, filledMapping, output);

        output.accept(modelLoc, () -> {
            JsonObject json = new JsonObject();
            json.addProperty("parent", "item/generated");

            JsonObject textures = new JsonObject();
            textures.addProperty(TextureSlot.LAYER0.getId(), modelLoc.toString());
            json.add("textures", textures);

            JsonArray overrides = new JsonArray();
            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.addProperty(InvExpUtil.location("quiver/has_contents").toString(), 1.0F);
            override.add("predicate", predicate);
            override.addProperty("model", filledLoc.toString());
            overrides.add(override);
            json.add("overrides", overrides);

            return json;
        });
    }

    private void bundle(BiConsumer<ResourceLocation, Supplier<JsonElement>> output, Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        ResourceLocation modelLoc = id.withPrefix("item/");

        TextureMapping baseMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, modelLoc);
        ModelTemplates.FLAT_ITEM.create(modelLoc, baseMapping, output);

        ResourceLocation openFrontLoc = modelLoc.withSuffix("_open_front");
        TextureMapping openFrontMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, openFrontLoc);
        ModelTemplates.FLAT_ITEM.create(openFrontLoc, openFrontMapping, output);

        ResourceLocation openBackLoc = modelLoc.withSuffix("_open_back");
        TextureMapping openBackMapping = new TextureMapping()
                .put(TextureSlot.LAYER0, openBackLoc);
        ModelTemplates.FLAT_ITEM.create(openBackLoc, openBackMapping, output);
    }
}
