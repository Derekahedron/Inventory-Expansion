package derekahedron.invexp.item.sack;

import derekahedron.invexp.registry.InvExpRegistryKeys;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * Holds all the Sack Types for Inventory Expansion.
 */
public class SackTypes {

    // Vanilla Sack Types
    public static final ResourceKey<SackType> WOOD = of("wood");
    public static final ResourceKey<SackType> DOOR = of("door");
    public static final ResourceKey<SackType> PRESSURE_PLATE = of("pressure_plate");
    public static final ResourceKey<SackType> BUTTON = of("button");
    public static final ResourceKey<SackType> STONE = of("stone");
    public static final ResourceKey<SackType> BRICKS = of("bricks");
    public static final ResourceKey<SackType> MUD_BRICKS = of("mud_bricks");
    public static final ResourceKey<SackType> SANDSTONE = of("sandstone");
    public static final ResourceKey<SackType> PRISMARINE = of("prismarine");
    public static final ResourceKey<SackType> NETHER_BRICKS = of("nether_bricks");
    public static final ResourceKey<SackType> PURPUR = of("purpur");
    public static final ResourceKey<SackType> METAL_BLOCK = of("metal_block");
    public static final ResourceKey<SackType> CRYSTAL_BLOCK = of("crystal_block");
    public static final ResourceKey<SackType> CHAINS = of("chains");
    public static final ResourceKey<SackType> WOOL = of("wool");
    public static final ResourceKey<SackType> TERRACOTTA = of("terracotta");
    public static final ResourceKey<SackType> CONCRETE = of("concrete");
    public static final ResourceKey<SackType> CONCRETE_POWDER = of("concrete_powder");
    public static final ResourceKey<SackType> GLASS = of("glass");
    public static final ResourceKey<SackType> BED = of("bed");
    public static final ResourceKey<SackType> CANDLE = of("candle");
    public static final ResourceKey<SackType> BANNER = of("banner");
    public static final ResourceKey<SackType> SOIL = of("soil");
    public static final ResourceKey<SackType> ICE = of("ice");
    public static final ResourceKey<SackType> SNOW = of("snow");
    public static final ResourceKey<SackType> BONE_BLOCK = of("bone_block");
    public static final ResourceKey<SackType> ORE = of("ore");
    public static final ResourceKey<SackType> FUNGUS = of("fungus");
    public static final ResourceKey<SackType> PLANT = of("plant");
    public static final ResourceKey<SackType> BAMBOO = of("bamboo");
    public static final ResourceKey<SackType> CHORUS_FRUIT = of("chorus_fruit");
    public static final ResourceKey<SackType> EGG = of("egg");
    public static final ResourceKey<SackType> WHEAT_SEEDS = of("wheat_seeds");
    public static final ResourceKey<SackType> COCOA_BEANS = of("cocoa_beans");
    public static final ResourceKey<SackType> PUMPKIN_SEEDS = of("pumpkin_seeds");
    public static final ResourceKey<SackType> MELON_SEEDS = of("melon_seeds");
    public static final ResourceKey<SackType> BEETROOT_SEEDS = of("beetroot_seeds");
    public static final ResourceKey<SackType> TORCHFLOWER_SEEDS = of("torchflower_seeds");
    public static final ResourceKey<SackType> PITCHER_POD = of("pitcher_pod");
    public static final ResourceKey<SackType> GLOW_BERRIES = of("glow_berries");
    public static final ResourceKey<SackType> SWEET_BERRIES = of("sweet_berries");
    public static final ResourceKey<SackType> NETHER_WART = of("nether_wart");
    public static final ResourceKey<SackType> SEA_CREATURE = of("sea_creature");
    public static final ResourceKey<SackType> KELP = of("kelp");
    public static final ResourceKey<SackType> CORAL = of("coral");
    public static final ResourceKey<SackType> SPONGE = of("sponge");
    public static final ResourceKey<SackType> MELON = of("melon");
    public static final ResourceKey<SackType> PUMPKIN = of("pumpkin");
    public static final ResourceKey<SackType> NEST = of("nest");
    public static final ResourceKey<SackType> HONEY = of("honey");
    public static final ResourceKey<SackType> FROGLIGHT = of("froglight");
    public static final ResourceKey<SackType> SCULK = of("sculk");
    public static final ResourceKey<SackType> COBWEB = of("cobweb");
    public static final ResourceKey<SackType> BEDROCK = of("bedrock");
    public static final ResourceKey<SackType> TORCH = of("torch");
    public static final ResourceKey<SackType> LANTERN = of("lantern");
    public static final ResourceKey<SackType> END_CRYSTAL = of("end_crystal");
    public static final ResourceKey<SackType> BELL = of("bell");
    public static final ResourceKey<SackType> SCAFFOLDING = of("scaffolding");
    public static final ResourceKey<SackType> POT = of("pot");
    public static final ResourceKey<SackType> ARMOR_STAND = of("armor_stand");
    public static final ResourceKey<SackType> ITEM_FRAME = of("item_frame");
    public static final ResourceKey<SackType> PAINTING = of("painting");
    public static final ResourceKey<SackType> SIGN = of("sign");
    public static final ResourceKey<SackType> HEAD = of("head");
    public static final ResourceKey<SackType> INFESTED_STONE = of("infested_stone");
    public static final ResourceKey<SackType> REDSTONE_COMPONENT = of("redstone_component");
    public static final ResourceKey<SackType> RAIL = of("rail");
    public static final ResourceKey<SackType> MINECART = of("minecart");
    public static final ResourceKey<SackType> TNT = of("tnt");
    public static final ResourceKey<SackType> BUCKET = of("bucket");
    public static final ResourceKey<SackType> FIRE_CHARGE = of("fire_charge");
    public static final ResourceKey<SackType> BONE_MEAL = of("bone_meal");
    public static final ResourceKey<SackType> NAME_TAG = of("name_tag");
    public static final ResourceKey<SackType> LEAD = of("lead");
    public static final ResourceKey<SackType> COMPASS = of("compass");
    public static final ResourceKey<SackType> CLOCK = of("clock");
    public static final ResourceKey<SackType> MAP = of("map");
    public static final ResourceKey<SackType> FIREWORK_ROCKET = of("firework_rocket");
    public static final ResourceKey<SackType> SADDLE = of("saddle");
    public static final ResourceKey<SackType> BOAT = of("boat");
    public static final ResourceKey<SackType> GOAT_HORN = of("goat_horn");
    public static final ResourceKey<SackType> MUSIC_DISC = of("music_disc");
    public static final ResourceKey<SackType> TOTEM_OF_UNDYING = of("totem_of_undying");
    public static final ResourceKey<SackType> ARROW = of("arrow");
    public static final ResourceKey<SackType> FOOD = of("food");
    public static final ResourceKey<SackType> CARROT = of("carrot");
    public static final ResourceKey<SackType> POTATO = of("potato");
    public static final ResourceKey<SackType> BEETROOT = of("beetroot");
    public static final ResourceKey<SackType> RAW_FISH = of("raw_fish");
    public static final ResourceKey<SackType> BOTTLE = of("bottle");
    public static final ResourceKey<SackType> POTION = of("potion");
    public static final ResourceKey<SackType> WHEAT = of("wheat");
    public static final ResourceKey<SackType> CREATURE = of("creature");
    public static final ResourceKey<SackType> HEART_OF_THE_SEA = of("heart_of_the_sea");
    public static final ResourceKey<SackType> DYE = of("dye");
    public static final ResourceKey<SackType> PAPER = of("paper");
    public static final ResourceKey<SackType> BOOK = of("book");
    public static final ResourceKey<SackType> FIREWORK_STAR = of("firework_star");
    public static final ResourceKey<SackType> SUGAR = of("sugar");
    public static final ResourceKey<SackType> BANNER_PATTERN = of("banner_pattern");
    public static final ResourceKey<SackType> POTTERY_SHERD = of("pottery_sherd");
    public static final ResourceKey<SackType> SMITHING_TEMPLATE = of("smithing_template");
    public static final ResourceKey<SackType> KEY = of("key");
    public static final ResourceKey<SackType> SPAWN_EGG = of("spawn_egg");
    public static final ResourceKey<SackType> COMMAND_BLOCK = of("command_block");
    // Extra Sack Types
    public static final ResourceKey<SackType> ROPE = of("rope");
    public static final ResourceKey<SackType> METAL_PLATE = of("metal_plate");
    public static final ResourceKey<SackType> TROPHY = of("trophy");

    /**
     * Creates a {@link ResourceKey} for a {@link SackType} for Inventory Expansion.
     *
     * @param path a {@link String} to create the sack type under
     * @return the sack type that was created
     */
    public static ResourceKey<SackType> of(String path) {
        return ResourceKey.create(InvExpRegistryKeys.SACK_TYPE, InvExpUtil.location(path));
    }

    /**
     * Bootstraps all sack types for data generation.
     *
     * @param context a {@link BootstapContext} to register the sack types under
     */
    public static void bootstrap(BootstapContext<SackType> context) {
        // Vanilla Sack Types
        context.register(WOOD, new SackType());
        context.register(DOOR, new SackType());
        context.register(PRESSURE_PLATE, new SackType());
        context.register(BUTTON, new SackType());
        context.register(STONE, new SackType());
        context.register(BRICKS, new SackType());
        context.register(MUD_BRICKS, new SackType());
        context.register(SANDSTONE, new SackType());
        context.register(PRISMARINE, new SackType());
        context.register(NETHER_BRICKS, new SackType());
        context.register(PURPUR, new SackType());
        context.register(METAL_BLOCK, new SackType());
        context.register(CRYSTAL_BLOCK, new SackType());
        context.register(CHAINS, new SackType());
        context.register(WOOL, new SackType());
        context.register(TERRACOTTA, new SackType());
        context.register(CONCRETE, new SackType());
        context.register(CONCRETE_POWDER, new SackType());
        context.register(GLASS, new SackType());
        context.register(BED, new SackType());
        context.register(CANDLE, new SackType());
        context.register(BANNER, new SackType());
        context.register(SOIL, new SackType());
        context.register(ICE, new SackType());
        context.register(SNOW, new SackType());
        context.register(BONE_BLOCK, new SackType());
        context.register(ORE, new SackType());
        context.register(FUNGUS, new SackType());
        context.register(PLANT, new SackType());
        context.register(BAMBOO, new SackType());
        context.register(CHORUS_FRUIT, new SackType());
        context.register(EGG, new SackType());
        context.register(WHEAT_SEEDS, new SackType());
        context.register(COCOA_BEANS, new SackType());
        context.register(PUMPKIN_SEEDS, new SackType());
        context.register(MELON_SEEDS, new SackType());
        context.register(BEETROOT_SEEDS, new SackType());
        context.register(TORCHFLOWER_SEEDS, new SackType());
        context.register(PITCHER_POD, new SackType());
        context.register(GLOW_BERRIES, new SackType());
        context.register(SWEET_BERRIES, new SackType());
        context.register(NETHER_WART, new SackType());
        context.register(SEA_CREATURE, new SackType());
        context.register(KELP, new SackType());
        context.register(CORAL, new SackType());
        context.register(SPONGE, new SackType());
        context.register(MELON, new SackType());
        context.register(PUMPKIN, new SackType());
        context.register(NEST, new SackType());
        context.register(HONEY, new SackType());
        context.register(FROGLIGHT, new SackType());
        context.register(SCULK, new SackType());
        context.register(COBWEB, new SackType());
        context.register(BEDROCK, new SackType());
        context.register(TORCH, new SackType());
        context.register(LANTERN, new SackType());
        context.register(END_CRYSTAL, new SackType());
        context.register(BELL, new SackType());
        context.register(SCAFFOLDING, new SackType());
        context.register(POT, new SackType());
        context.register(ARMOR_STAND, new SackType());
        context.register(ITEM_FRAME, new SackType());
        context.register(PAINTING, new SackType());
        context.register(SIGN, new SackType());
        context.register(HEAD, new SackType());
        context.register(INFESTED_STONE, new SackType());
        context.register(REDSTONE_COMPONENT, new SackType());
        context.register(RAIL, new SackType());
        context.register(MINECART, new SackType());
        context.register(TNT, new SackType());
        context.register(BUCKET, new SackType());
        context.register(FIRE_CHARGE, new SackType());
        context.register(BONE_MEAL, new SackType());
        context.register(NAME_TAG, new SackType());
        context.register(LEAD, new SackType());
        context.register(COMPASS, new SackType());
        context.register(CLOCK, new SackType());
        context.register(MAP, new SackType());
        context.register(FIREWORK_ROCKET, new SackType());
        context.register(SADDLE, new SackType());
        context.register(BOAT, new SackType());
        context.register(GOAT_HORN, new SackType());
        context.register(MUSIC_DISC, new SackType());
        context.register(TOTEM_OF_UNDYING, new SackType());
        context.register(ARROW, new SackType());
        context.register(FOOD, new SackType());
        context.register(CARROT, new SackType());
        context.register(POTATO, new SackType());
        context.register(BEETROOT, new SackType());
        context.register(RAW_FISH, new SackType());
        context.register(BOTTLE, new SackType());
        context.register(POTION, new SackType());
        context.register(WHEAT, new SackType());
        context.register(CREATURE, new SackType());
        context.register(HEART_OF_THE_SEA, new SackType());
        context.register(DYE, new SackType());
        context.register(PAPER, new SackType());
        context.register(BOOK, new SackType());
        context.register(FIREWORK_STAR, new SackType());
        context.register(SUGAR, new SackType());
        context.register(BANNER_PATTERN, new SackType());
        context.register(POTTERY_SHERD, new SackType());
        context.register(SMITHING_TEMPLATE, new SackType());
        context.register(KEY, new SackType());
        context.register(SPAWN_EGG, new SackType());
        context.register(COMMAND_BLOCK, new SackType());
        // Extra Sack Types
        context.register(ROPE, new SackType());
        context.register(METAL_PLATE, new SackType());
        context.register(TROPHY, new SackType());
    }
}
