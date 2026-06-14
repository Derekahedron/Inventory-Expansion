package derekahedron.invexp.item.sack;

import derekahedron.invexp.item.InvExpItemTags;
import derekahedron.invexp.registry.InvExpRegistryKeys;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;

/**
 * Holds all the rules for sack types in Inventory Expansion
 */
public class SackTypeRules {

    // Vanilla Sack Types
    public static final ResourceKey<SackTypeRule> WOOD = of("wood");
    public static final ResourceKey<SackTypeRule> DOOR = of("door");
    public static final ResourceKey<SackTypeRule> PRESSURE_PLATE = of("pressure_plate");
    public static final ResourceKey<SackTypeRule> BUTTON = of("button");
    public static final ResourceKey<SackTypeRule> STONE = of("stone");
    public static final ResourceKey<SackTypeRule> BRICKS = of("bricks");
    public static final ResourceKey<SackTypeRule> MUD_BRICKS = of("mud_bricks");
    public static final ResourceKey<SackTypeRule> RESIN_BRICKS = of("resin_bricks");
    public static final ResourceKey<SackTypeRule> SANDSTONE = of("sandstone");
    public static final ResourceKey<SackTypeRule> PRISMARINE = of("prismarine");
    public static final ResourceKey<SackTypeRule> NETHER_BRICKS = of("nether_bricks");
    public static final ResourceKey<SackTypeRule> PURPUR = of("purpur");
    public static final ResourceKey<SackTypeRule> METAL_BLOCK = of("metal_block");
    public static final ResourceKey<SackTypeRule> CRYSTAL_BLOCK = of("crystal_block");
    public static final ResourceKey<SackTypeRule> CHAINS = of("chains");
    public static final ResourceKey<SackTypeRule> WOOL = of("wool");
    public static final ResourceKey<SackTypeRule> TERRACOTTA = of("terracotta");
    public static final ResourceKey<SackTypeRule> CONCRETE = of("concrete");
    public static final ResourceKey<SackTypeRule> CONCRETE_POWDER = of("concrete_powder");
    public static final ResourceKey<SackTypeRule> GLASS = of("glass");
    public static final ResourceKey<SackTypeRule> BED = of("bed");
    public static final ResourceKey<SackTypeRule> CANDLE = of("candle");
    public static final ResourceKey<SackTypeRule> BANNER = of("banner");
    public static final ResourceKey<SackTypeRule> SOIL = of("soil");
    public static final ResourceKey<SackTypeRule> ICE = of("ice");
    public static final ResourceKey<SackTypeRule> SNOW = of("snow");
    public static final ResourceKey<SackTypeRule> BONE_BLOCK = of("bone_block");
    public static final ResourceKey<SackTypeRule> ORE = of("ore");
    public static final ResourceKey<SackTypeRule> FUNGUS = of("fungus");
    public static final ResourceKey<SackTypeRule> PLANT = of("plant");
    public static final ResourceKey<SackTypeRule> BAMBOO = of("bamboo");
    public static final ResourceKey<SackTypeRule> CHORUS_FRUIT = of("chorus_fruit");
    public static final ResourceKey<SackTypeRule> EGG = of("egg");
    public static final ResourceKey<SackTypeRule> DRIED_GHAST = of("dried_ghast");
    public static final ResourceKey<SackTypeRule> WHEAT_SEEDS = of("wheat_seeds");
    public static final ResourceKey<SackTypeRule> COCOA_BEANS = of("cocoa_beans");
    public static final ResourceKey<SackTypeRule> PUMPKIN_SEEDS = of("pumpkin_seeds");
    public static final ResourceKey<SackTypeRule> MELON_SEEDS = of("melon_seeds");
    public static final ResourceKey<SackTypeRule> BEETROOT_SEEDS = of("beetroot_seeds");
    public static final ResourceKey<SackTypeRule> TORCHFLOWER_SEEDS = of("torchflower_seeds");
    public static final ResourceKey<SackTypeRule> PITCHER_POD = of("pitcher_pod");
    public static final ResourceKey<SackTypeRule> GLOW_BERRIES = of("glow_berries");
    public static final ResourceKey<SackTypeRule> SWEET_BERRIES = of("sweet_berries");
    public static final ResourceKey<SackTypeRule> NETHER_WART = of("nether_wart");
    public static final ResourceKey<SackTypeRule> SEA_CREATURE = of("sea_creature");
    public static final ResourceKey<SackTypeRule> KELP = of("kelp");
    public static final ResourceKey<SackTypeRule> CORAL = of("coral");
    public static final ResourceKey<SackTypeRule> SPONGE = of("sponge");
    public static final ResourceKey<SackTypeRule> MELON = of("melon");
    public static final ResourceKey<SackTypeRule> PUMPKIN = of("pumpkin");
    public static final ResourceKey<SackTypeRule> NEST = of("nest");
    public static final ResourceKey<SackTypeRule> HONEY = of("honey");
    public static final ResourceKey<SackTypeRule> FROGLIGHT = of("froglight");
    public static final ResourceKey<SackTypeRule> SCULK = of("sculk");
    public static final ResourceKey<SackTypeRule> COBWEB = of("cobweb");
    public static final ResourceKey<SackTypeRule> BEDROCK = of("bedrock");
    public static final ResourceKey<SackTypeRule> TORCH = of("torch");
    public static final ResourceKey<SackTypeRule> LANTERN = of("lantern");
    public static final ResourceKey<SackTypeRule> END_CRYSTAL = of("end_crystal");
    public static final ResourceKey<SackTypeRule> BELL = of("bell");
    public static final ResourceKey<SackTypeRule> SCAFFOLDING = of("scaffolding");
    public static final ResourceKey<SackTypeRule> POT = of("pot");
    public static final ResourceKey<SackTypeRule> ARMOR_STAND = of("armor_stand");
    public static final ResourceKey<SackTypeRule> ITEM_FRAME = of("item_frame");
    public static final ResourceKey<SackTypeRule> PAINTING = of("painting");
    public static final ResourceKey<SackTypeRule> SIGN = of("sign");
    public static final ResourceKey<SackTypeRule> HEAD = of("head");
    public static final ResourceKey<SackTypeRule> COPPER_GOLEM_STATUE = of("copper_golem_statue");
    public static final ResourceKey<SackTypeRule> INFESTED_STONE = of("infested_stone");
    public static final ResourceKey<SackTypeRule> REDSTONE_COMPONENT = of("redstone_component");
    public static final ResourceKey<SackTypeRule> RAIL = of("rail");
    public static final ResourceKey<SackTypeRule> MINECART = of("minecart");
    public static final ResourceKey<SackTypeRule> TNT = of("tnt");
    public static final ResourceKey<SackTypeRule> BUCKET = of("bucket");
    public static final ResourceKey<SackTypeRule> FIRE_CHARGE = of("fire_charge");
    public static final ResourceKey<SackTypeRule> BONE_MEAL = of("bone_meal");
    public static final ResourceKey<SackTypeRule> NAME_TAG = of("name_tag");
    public static final ResourceKey<SackTypeRule> LEAD = of("lead");
    public static final ResourceKey<SackTypeRule> COMPASS = of("compass");
    public static final ResourceKey<SackTypeRule> CLOCK = of("clock");
    public static final ResourceKey<SackTypeRule> MAP = of("map");
    public static final ResourceKey<SackTypeRule> WIND_CHARGE = of("wind_charge");
    public static final ResourceKey<SackTypeRule> FIREWORK_ROCKET = of("firework_rocket");
    public static final ResourceKey<SackTypeRule> SADDLE = of("saddle");
    public static final ResourceKey<SackTypeRule> HARNESS = of("harness");
    public static final ResourceKey<SackTypeRule> BOAT = of("boat");
    public static final ResourceKey<SackTypeRule> GOAT_HORN = of("goat_horn");
    public static final ResourceKey<SackTypeRule> MUSIC_DISC = of("music_disc");
    public static final ResourceKey<SackTypeRule> TOTEM_OF_UNDYING = of("totem_of_undying");
    public static final ResourceKey<SackTypeRule> ARROW = of("arrow");
    public static final ResourceKey<SackTypeRule> FOOD = of("food");
    public static final ResourceKey<SackTypeRule> CARROT = of("carrot");
    public static final ResourceKey<SackTypeRule> POTATO = of("potato");
    public static final ResourceKey<SackTypeRule> BEETROOT = of("beetroot");
    public static final ResourceKey<SackTypeRule> RAW_FISH = of("raw_fish");
    public static final ResourceKey<SackTypeRule> BOTTLE = of("bottle");
    public static final ResourceKey<SackTypeRule> POTION = of("potion");
    public static final ResourceKey<SackTypeRule> WHEAT = of("wheat");
    public static final ResourceKey<SackTypeRule> CREATURE = of("creature");
    public static final ResourceKey<SackTypeRule> HEART_OF_THE_SEA = of("heart_of_the_sea");
    public static final ResourceKey<SackTypeRule> HEAVY_CORE = of("heavy_core");
    public static final ResourceKey<SackTypeRule> DYE = of("dye");
    public static final ResourceKey<SackTypeRule> PAPER = of("paper");
    public static final ResourceKey<SackTypeRule> BOOK = of("book");
    public static final ResourceKey<SackTypeRule> FIREWORK_STAR = of("firework_star");
    public static final ResourceKey<SackTypeRule> SUGAR = of("sugar");
    public static final ResourceKey<SackTypeRule> BANNER_PATTERN = of("banner_pattern");
    public static final ResourceKey<SackTypeRule> POTTERY_SHERD = of("pottery_sherd");
    public static final ResourceKey<SackTypeRule> KEY = of("key");
    public static final ResourceKey<SackTypeRule> SMITHING_TEMPLATE = of("smithing_template");
    public static final ResourceKey<SackTypeRule> SPAWN_EGG = of("spawn_egg");
    public static final ResourceKey<SackTypeRule> COMMAND_BLOCK = of("command_block");
    public static final ResourceKey<SackTypeRule> NONE = of("none");

    public static final ResourceKey<SackTypeRule> ROPE = of("rope");
    public static final ResourceKey<SackTypeRule> METAL_PLATE = of("metal_plate");
    public static final ResourceKey<SackTypeRule> TROPHY = of("trophy");

    // Specific Sack Types
    public static final ResourceKey<SackTypeRule> RAW_INFESTED_STONE = of("raw_infested_stone");
    public static final ResourceKey<SackTypeRule> MOSS_BLOCKS = of("moss_blocks");
    public static final ResourceKey<SackTypeRule> WATER_BOTTLE = of("water_bottle");

    public static ResourceKey<SackTypeRule> of(String path) {
        return ResourceKey.create(InvExpRegistryKeys.SACK_TYPE_RULE, InvExpUtil.location(path));
    }

    public static void bootstrap(BootstapContext<SackTypeRule> context) {
        // Vanilla Sack Types
        context.register(WOOD, new SackTypeRule(InvExpItemTags.SackType.WOOD, SackTypes.WOOD));
        context.register(DOOR, new SackTypeRule(InvExpItemTags.SackType.DOOR, SackTypes.DOOR));
        context.register(PRESSURE_PLATE, new SackTypeRule(InvExpItemTags.SackType.PRESSURE_PLATE, SackTypes.PRESSURE_PLATE));
        context.register(BUTTON, new SackTypeRule(InvExpItemTags.SackType.BUTTON, SackTypes.BUTTON));
        context.register(STONE, new SackTypeRule(InvExpItemTags.SackType.STONE, SackTypes.STONE));
        context.register(BRICKS, new SackTypeRule(InvExpItemTags.SackType.BRICKS, SackTypes.BRICKS));
        context.register(MUD_BRICKS, new SackTypeRule(InvExpItemTags.SackType.MUD_BRICKS, SackTypes.MUD_BRICKS));
        context.register(RESIN_BRICKS, new SackTypeRule(InvExpItemTags.SackType.RESIN_BRICKS, SackTypes.RESIN_BRICKS));
        context.register(SANDSTONE, new SackTypeRule(InvExpItemTags.SackType.SANDSTONE, SackTypes.SANDSTONE));
        context.register(PRISMARINE, new SackTypeRule(InvExpItemTags.SackType.PRISMARINE, SackTypes.PRISMARINE));
        context.register(NETHER_BRICKS, new SackTypeRule(InvExpItemTags.SackType.NETHER_BRICKS, SackTypes.NETHER_BRICKS));
        context.register(PURPUR, new SackTypeRule(InvExpItemTags.SackType.PURPUR, SackTypes.PURPUR));
        context.register(METAL_BLOCK, new SackTypeRule(InvExpItemTags.SackType.METAL_BLOCK, SackTypes.METAL_BLOCK));
        context.register(CRYSTAL_BLOCK, new SackTypeRule(InvExpItemTags.SackType.CRYSTAL_BLOCK, SackTypes.CRYSTAL_BLOCK));
        context.register(CHAINS, new SackTypeRule(InvExpItemTags.SackType.CHAINS, SackTypes.CHAINS));
        context.register(WOOL, new SackTypeRule(InvExpItemTags.SackType.WOOL, SackTypes.WOOL));
        context.register(TERRACOTTA, new SackTypeRule(InvExpItemTags.SackType.TERRACOTTA, SackTypes.TERRACOTTA));
        context.register(CONCRETE, new SackTypeRule(InvExpItemTags.SackType.CONCRETE, SackTypes.CONCRETE));
        context.register(CONCRETE_POWDER, new SackTypeRule(InvExpItemTags.SackType.CONCRETE_POWDER, SackTypes.CONCRETE_POWDER));
        context.register(GLASS, new SackTypeRule(InvExpItemTags.SackType.GLASS, SackTypes.GLASS));
        context.register(BED, new SackTypeRule(InvExpItemTags.SackType.BED, SackTypes.BED));
        context.register(CANDLE, new SackTypeRule(InvExpItemTags.SackType.CANDLE, SackTypes.CANDLE));
        context.register(BANNER, new SackTypeRule(InvExpItemTags.SackType.BANNER, SackTypes.BANNER));
        context.register(SOIL, new SackTypeRule(InvExpItemTags.SackType.SOIL, SackTypes.SOIL));
        context.register(ICE, new SackTypeRule(InvExpItemTags.SackType.ICE, SackTypes.ICE));
        context.register(SNOW, new SackTypeRule(InvExpItemTags.SackType.SNOW, SackTypes.SNOW));
        context.register(BONE_BLOCK, new SackTypeRule(InvExpItemTags.SackType.BONE_BLOCK, SackTypes.BONE_BLOCK));
        context.register(ORE, new SackTypeRule(InvExpItemTags.SackType.ORE, SackTypes.ORE));
        context.register(FUNGUS, new SackTypeRule(InvExpItemTags.SackType.FUNGUS, SackTypes.FUNGUS));
        context.register(PLANT, new SackTypeRule(InvExpItemTags.SackType.PLANT, SackTypes.PLANT));
        context.register(BAMBOO, new SackTypeRule(InvExpItemTags.SackType.BAMBOO, SackTypes.BAMBOO));
        context.register(CHORUS_FRUIT, new SackTypeRule(InvExpItemTags.SackType.CHORUS_FRUIT, SackTypes.CHORUS_FRUIT));
        context.register(EGG, new SackTypeRule(InvExpItemTags.SackType.EGG, SackTypes.EGG));
        context.register(DRIED_GHAST, new SackTypeRule(InvExpItemTags.SackType.DRIED_GHAST, SackTypes.DRIED_GHAST));
        context.register(WHEAT_SEEDS, new SackTypeRule(InvExpItemTags.SackType.WHEAT_SEEDS, SackTypes.WHEAT_SEEDS));
        context.register(COCOA_BEANS, new SackTypeRule(InvExpItemTags.SackType.COCOA_BEANS, SackTypes.COCOA_BEANS));
        context.register(PUMPKIN_SEEDS, new SackTypeRule(InvExpItemTags.SackType.PUMPKIN_SEEDS, SackTypes.PUMPKIN_SEEDS));
        context.register(MELON_SEEDS, new SackTypeRule(InvExpItemTags.SackType.MELON_SEEDS, SackTypes.MELON_SEEDS));
        context.register(BEETROOT_SEEDS, new SackTypeRule(InvExpItemTags.SackType.BEETROOT_SEEDS, SackTypes.BEETROOT_SEEDS));
        context.register(TORCHFLOWER_SEEDS, new SackTypeRule(InvExpItemTags.SackType.TORCHFLOWER_SEEDS, SackTypes.TORCHFLOWER_SEEDS));
        context.register(PITCHER_POD, new SackTypeRule(InvExpItemTags.SackType.PITCHER_POD, SackTypes.PITCHER_POD));
        context.register(GLOW_BERRIES, new SackTypeRule(InvExpItemTags.SackType.GLOW_BERRIES, SackTypes.GLOW_BERRIES));
        context.register(SWEET_BERRIES, new SackTypeRule(InvExpItemTags.SackType.SWEET_BERRIES, SackTypes.SWEET_BERRIES));
        context.register(NETHER_WART, new SackTypeRule(InvExpItemTags.SackType.NETHER_WART, SackTypes.NETHER_WART));
        context.register(SEA_CREATURE, new SackTypeRule(InvExpItemTags.SackType.SEA_CREATURE, SackTypes.SEA_CREATURE));
        context.register(KELP, new SackTypeRule(InvExpItemTags.SackType.KELP, SackTypes.KELP));
        context.register(CORAL, new SackTypeRule(InvExpItemTags.SackType.CORAL, SackTypes.CORAL));
        context.register(SPONGE, new SackTypeRule(InvExpItemTags.SackType.SPONGE, SackTypes.SPONGE));
        context.register(MELON, new SackTypeRule(InvExpItemTags.SackType.MELON, SackTypes.MELON));
        context.register(PUMPKIN, new SackTypeRule(InvExpItemTags.SackType.PUMPKIN, SackTypes.PUMPKIN));
        context.register(NEST, new SackTypeRule(InvExpItemTags.SackType.NEST, SackTypes.NEST));
        context.register(HONEY, new SackTypeRule(InvExpItemTags.SackType.HONEY, SackTypes.HONEY));
        context.register(FROGLIGHT, new SackTypeRule(InvExpItemTags.SackType.FROGLIGHT, SackTypes.FROGLIGHT));
        context.register(SCULK, new SackTypeRule(InvExpItemTags.SackType.SCULK, SackTypes.SCULK));
        context.register(COBWEB, new SackTypeRule(InvExpItemTags.SackType.COBWEB, SackTypes.COBWEB));
        context.register(BEDROCK, new SackTypeRule(InvExpItemTags.SackType.BEDROCK, SackTypes.BEDROCK));
        context.register(TORCH, new SackTypeRule(InvExpItemTags.SackType.TORCH, SackTypes.TORCH));
        context.register(LANTERN, new SackTypeRule(InvExpItemTags.SackType.LANTERN, SackTypes.LANTERN));
        context.register(END_CRYSTAL, new SackTypeRule(InvExpItemTags.SackType.END_CRYSTAL, SackTypes.END_CRYSTAL));
        context.register(BELL, new SackTypeRule(InvExpItemTags.SackType.BELL, SackTypes.BELL));
        context.register(SCAFFOLDING, new SackTypeRule(InvExpItemTags.SackType.SCAFFOLDING, SackTypes.SCAFFOLDING));
        context.register(POT, new SackTypeRule(InvExpItemTags.SackType.POT, SackTypes.POT));
        context.register(ARMOR_STAND, new SackTypeRule(InvExpItemTags.SackType.ARMOR_STAND, SackTypes.ARMOR_STAND));
        context.register(ITEM_FRAME, new SackTypeRule(InvExpItemTags.SackType.ITEM_FRAME, SackTypes.ITEM_FRAME));
        context.register(PAINTING, new SackTypeRule(InvExpItemTags.SackType.PAINTING, SackTypes.PAINTING));
        context.register(SIGN, new SackTypeRule(InvExpItemTags.SackType.SIGN, SackTypes.SIGN));
        context.register(HEAD, new SackTypeRule(InvExpItemTags.SackType.HEAD, SackTypes.HEAD));
        context.register(COPPER_GOLEM_STATUE, new SackTypeRule(InvExpItemTags.SackType.COPPER_GOLEM_STATUE, SackTypes.COPPER_GOLEM_STATUE));
        context.register(INFESTED_STONE, new SackTypeRule(InvExpItemTags.SackType.INFESTED_STONE, SackTypes.INFESTED_STONE));
        context.register(REDSTONE_COMPONENT, new SackTypeRule(InvExpItemTags.SackType.REDSTONE_COMPONENT, SackTypes.REDSTONE_COMPONENT));
        context.register(RAIL, new SackTypeRule(InvExpItemTags.SackType.RAIL, SackTypes.RAIL));
        context.register(MINECART, new SackTypeRule(InvExpItemTags.SackType.MINECART, SackTypes.MINECART));
        context.register(TNT, new SackTypeRule(InvExpItemTags.SackType.TNT, SackTypes.TNT));
        context.register(BUCKET, new SackTypeRule(InvExpItemTags.SackType.BUCKET, SackTypes.BUCKET));
        context.register(FIRE_CHARGE, new SackTypeRule(InvExpItemTags.SackType.FIRE_CHARGE, SackTypes.FIRE_CHARGE));
        context.register(BONE_MEAL, new SackTypeRule(InvExpItemTags.SackType.BONE_MEAL, SackTypes.BONE_MEAL));
        context.register(NAME_TAG, new SackTypeRule(InvExpItemTags.SackType.NAME_TAG, SackTypes.NAME_TAG));
        context.register(LEAD, new SackTypeRule(InvExpItemTags.SackType.LEAD, SackTypes.LEAD));
        context.register(COMPASS, new SackTypeRule(InvExpItemTags.SackType.COMPASS, SackTypes.COMPASS));
        context.register(CLOCK, new SackTypeRule(InvExpItemTags.SackType.CLOCK, SackTypes.CLOCK));
        context.register(MAP, new SackTypeRule(InvExpItemTags.SackType.MAP, SackTypes.MAP));
        context.register(WIND_CHARGE, new SackTypeRule(InvExpItemTags.SackType.WIND_CHARGE, SackTypes.WIND_CHARGE));
        context.register(FIREWORK_ROCKET, new SackTypeRule(InvExpItemTags.SackType.FIREWORK_ROCKET, SackTypes.FIREWORK_ROCKET));
        context.register(SADDLE, new SackTypeRule(InvExpItemTags.SackType.SADDLE, SackTypes.SADDLE));
        context.register(HARNESS, new SackTypeRule(InvExpItemTags.SackType.HARNESS, SackTypes.HARNESS));
        context.register(BOAT, new SackTypeRule(InvExpItemTags.SackType.BOAT, SackTypes.BOAT));
        context.register(GOAT_HORN, new SackTypeRule(InvExpItemTags.SackType.GOAT_HORN, SackTypes.GOAT_HORN));
        context.register(MUSIC_DISC, new SackTypeRule(InvExpItemTags.SackType.MUSIC_DISC, SackTypes.MUSIC_DISC));
        context.register(TOTEM_OF_UNDYING, new SackTypeRule(InvExpItemTags.SackType.TOTEM_OF_UNDYING, SackTypes.TOTEM_OF_UNDYING));
        context.register(ARROW, new SackTypeRule(InvExpItemTags.SackType.ARROW, SackTypes.ARROW));
        context.register(FOOD, new SackTypeRule(InvExpItemTags.SackType.FOOD, SackTypes.FOOD));
        context.register(CARROT, new SackTypeRule(InvExpItemTags.SackType.CARROT, SackTypes.CARROT));
        context.register(POTATO, new SackTypeRule(InvExpItemTags.SackType.POTATO, SackTypes.POTATO));
        context.register(BEETROOT, new SackTypeRule(InvExpItemTags.SackType.BEETROOT, SackTypes.BEETROOT));
        context.register(RAW_FISH, new SackTypeRule(InvExpItemTags.SackType.RAW_FISH, SackTypes.RAW_FISH));
        context.register(BOTTLE, new SackTypeRule(InvExpItemTags.SackType.BOTTLE, SackTypes.BOTTLE));
        context.register(POTION, new SackTypeRule(InvExpItemTags.SackType.POTION, SackTypes.POTION));
        context.register(WHEAT, new SackTypeRule(InvExpItemTags.SackType.WHEAT, SackTypes.WHEAT));
        context.register(CREATURE, new SackTypeRule(InvExpItemTags.SackType.CREATURE, SackTypes.CREATURE));
        context.register(HEART_OF_THE_SEA, new SackTypeRule(InvExpItemTags.SackType.HEART_OF_THE_SEA, SackTypes.HEART_OF_THE_SEA));
        context.register(HEAVY_CORE, new SackTypeRule(InvExpItemTags.SackType.HEAVY_CORE, SackTypes.HEAVY_CORE));
        context.register(DYE, new SackTypeRule(InvExpItemTags.SackType.DYE, SackTypes.DYE));
        context.register(PAPER, new SackTypeRule(InvExpItemTags.SackType.PAPER, SackTypes.PAPER));
        context.register(BOOK, new SackTypeRule(InvExpItemTags.SackType.BOOK, SackTypes.BOOK));
        context.register(FIREWORK_STAR, new SackTypeRule(InvExpItemTags.SackType.FIREWORK_STAR, SackTypes.FIREWORK_STAR));
        context.register(SUGAR, new SackTypeRule(InvExpItemTags.SackType.SUGAR, SackTypes.SUGAR));
        context.register(BANNER_PATTERN, new SackTypeRule(InvExpItemTags.SackType.BANNER_PATTERN, SackTypes.BANNER_PATTERN));
        context.register(POTTERY_SHERD, new SackTypeRule(InvExpItemTags.SackType.POTTERY_SHERD, SackTypes.POTTERY_SHERD));
        context.register(KEY, new SackTypeRule(InvExpItemTags.SackType.KEY, SackTypes.KEY));
        context.register(SMITHING_TEMPLATE, new SackTypeRule(InvExpItemTags.SackType.SMITHING_TEMPLATE, SackTypes.SMITHING_TEMPLATE));
        context.register(SPAWN_EGG, new SackTypeRule(InvExpItemTags.SackType.SPAWN_EGG, SackTypes.SPAWN_EGG));
        context.register(COMMAND_BLOCK, new SackTypeRule(InvExpItemTags.SackType.COMMAND_BLOCK, SackTypes.COMMAND_BLOCK));

        context.register(ROPE, new SackTypeRule(InvExpItemTags.SackType.ROPE, SackTypes.ROPE));
        context.register(METAL_PLATE, new SackTypeRule(InvExpItemTags.SackType.METAL_PLATE, SackTypes.METAL_PLATE));
        context.register(TROPHY, new SackTypeRule(InvExpItemTags.SackType.TROPHY, SackTypes.TROPHY));

        context.register(NONE, new SackTypeRule(
                Optional.of(100),
                Optional.of(Ingredient.of(InvExpItemTags.SackType.NONE)),
                Optional.empty(),
                Optional.empty()));

        // Specific Sack Types
        context.register(
                RAW_INFESTED_STONE,
                new SackTypeRule(
                        10,
                        Ingredient.of(
                                Items.INFESTED_STONE, Items.INFESTED_COBBLESTONE, Items.INFESTED_DEEPSLATE),
                        SackTypes.INFESTED_STONE));
        context.register(
                MOSS_BLOCKS,
                new SackTypeRule(
                        10,
                        Ingredient.of(Items.MOSS_BLOCK),
                        SackTypes.PLANT));
        context.register(
                WATER_BOTTLE,
                new SackTypeRule(
                        Optional.of(10),
                        Optional.of(Ingredient.of(Items.POTION)),
                        Optional.of(ItemPredicate.Builder.item().isPotion(Potions.WATER).build()),
                        Optional.of(SackTypes.BOTTLE)));
    }
}
