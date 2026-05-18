package derekahedron.invexp.item;

import derekahedron.invexp.item.bundle.BetterBundleItem;
import derekahedron.invexp.item.quiver.QuiverItem;
import derekahedron.invexp.platform.Services;
import net.minecraft.world.item.*;

import java.util.function.Supplier;

/**
 * Holds all items in Inventory Expansion.
 */
public class InvExpItems {

    public static final Supplier<Item> SACK = Services.ITEM_REGISTRAR.register("sack", () ->
            new DyeableSackItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> QUIVER = Services.ITEM_REGISTRAR.register("quiver", () ->
            new QuiverItem(
                    new Item.Properties()
                            .stacksTo(1)));

    public static final Supplier<Item> WHITE_BUNDLE = Services.ITEM_REGISTRAR.register("white_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> ORANGE_BUNDLE = Services.ITEM_REGISTRAR.register("orange_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> MAGENTA_BUNDLE = Services.ITEM_REGISTRAR.register("magenta_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> LIGHT_BLUE_BUNDLE = Services.ITEM_REGISTRAR.register("light_blue_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> YELLOW_BUNDLE = Services.ITEM_REGISTRAR.register("yellow_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> LIME_BUNDLE = Services.ITEM_REGISTRAR.register("lime_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> PINK_BUNDLE = Services.ITEM_REGISTRAR.register("pink_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> GRAY_BUNDLE = Services.ITEM_REGISTRAR.register("gray_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> LIGHT_GRAY_BUNDLE = Services.ITEM_REGISTRAR.register("light_gray_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> PURPLE_BUNDLE = Services.ITEM_REGISTRAR.register("purple_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> CYAN_BUNDLE = Services.ITEM_REGISTRAR.register("cyan_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> BLUE_BUNDLE = Services.ITEM_REGISTRAR.register("blue_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> BROWN_BUNDLE = Services.ITEM_REGISTRAR.register("brown_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> GREEN_BUNDLE = Services.ITEM_REGISTRAR.register("green_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> RED_BUNDLE = Services.ITEM_REGISTRAR.register("red_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));
    public static final Supplier<Item> BLACK_BUNDLE = Services.ITEM_REGISTRAR.register("black_bundle", () ->
            new BetterBundleItem(
                    new Item.Properties()
                            .stacksTo(1)));

    /**
     * Initializes items.
     */
    public static void init() {
        // Do nothing: load class
    }
}
