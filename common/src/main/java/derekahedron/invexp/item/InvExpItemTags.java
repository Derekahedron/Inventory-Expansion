package derekahedron.invexp.item;

import derekahedron.invexp.item.sack.SackTypes;
import derekahedron.invexp.util.InvExpUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Holds item tags for Inventory Expansion.
 */
public class InvExpItemTags {

    public static final TagKey<Item> SACKS = of("sacks");
    public static final TagKey<Item> QUIVERS = of("quivers");
    public static final TagKey<Item> BUNDLES = of("bundles");
    public static final TagKey<Item> DYEABLE_BUNDLES = of("dyeable_bundles");

    /**
     * Creates a new <code>TagKey</code> for Inventory Expansion items.
     *
     * @param path a <code>String</code> to use as the tag name
     * @return the <code>TagKey</code> that was created
     */
    public static TagKey<Item> of(String path) {
        return TagKey.create(Registries.ITEM, InvExpUtil.location(path));
    }

    /**
     * Holds item tags that control sack type for Inventory Expansion.
     */
    public static class SackType {

        // Vanilla Sack Types
        public static final TagKey<Item> WOOD = of(SackTypes.WOOD);
        public static final TagKey<Item> DOOR = of(SackTypes.DOOR);
        public static final TagKey<Item> PRESSURE_PLATE = of(SackTypes.PRESSURE_PLATE);
        public static final TagKey<Item> BUTTON = of(SackTypes.BUTTON);
        public static final TagKey<Item> STONE = of(SackTypes.STONE);
        public static final TagKey<Item> BRICKS = of(SackTypes.BRICKS);
        public static final TagKey<Item> MUD_BRICKS = of(SackTypes.MUD_BRICKS);
        public static final TagKey<Item> SANDSTONE = of(SackTypes.SANDSTONE);
        public static final TagKey<Item> PRISMARINE = of(SackTypes.PRISMARINE);
        public static final TagKey<Item> NETHER_BRICKS = of(SackTypes.NETHER_BRICKS);
        public static final TagKey<Item> PURPUR = of(SackTypes.PURPUR);
        public static final TagKey<Item> METAL_BLOCK = of(SackTypes.METAL_BLOCK);
        public static final TagKey<Item> CRYSTAL_BLOCK = of(SackTypes.CRYSTAL_BLOCK);
        public static final TagKey<Item> CHAINS = of(SackTypes.CHAINS);
        public static final TagKey<Item> WOOL = of(SackTypes.WOOL);
        public static final TagKey<Item> TERRACOTTA = of(SackTypes.TERRACOTTA);
        public static final TagKey<Item> CONCRETE = of(SackTypes.CONCRETE);
        public static final TagKey<Item> CONCRETE_POWDER = of(SackTypes.CONCRETE_POWDER);
        public static final TagKey<Item> GLASS = of(SackTypes.GLASS);
        public static final TagKey<Item> BED = of(SackTypes.BED);
        public static final TagKey<Item> CANDLE = of(SackTypes.CANDLE);
        public static final TagKey<Item> BANNER = of(SackTypes.BANNER);
        public static final TagKey<Item> SOIL = of(SackTypes.SOIL);
        public static final TagKey<Item> ICE = of(SackTypes.ICE);
        public static final TagKey<Item> SNOW = of(SackTypes.SNOW);
        public static final TagKey<Item> BONE_BLOCK = of(SackTypes.BONE_BLOCK);
        public static final TagKey<Item> ORE = of(SackTypes.ORE);
        public static final TagKey<Item> FUNGUS = of(SackTypes.FUNGUS);
        public static final TagKey<Item> PLANT = of(SackTypes.PLANT);
        public static final TagKey<Item> BAMBOO = of(SackTypes.BAMBOO);
        public static final TagKey<Item> CHORUS_FRUIT = of(SackTypes.CHORUS_FRUIT);
        public static final TagKey<Item> EGG = of(SackTypes.EGG);
        public static final TagKey<Item> WHEAT_SEEDS = of(SackTypes.WHEAT_SEEDS);
        public static final TagKey<Item> COCOA_BEANS = of(SackTypes.COCOA_BEANS);
        public static final TagKey<Item> PUMPKIN_SEEDS = of(SackTypes.PUMPKIN_SEEDS);
        public static final TagKey<Item> MELON_SEEDS = of(SackTypes.MELON_SEEDS);
        public static final TagKey<Item> BEETROOT_SEEDS = of(SackTypes.BEETROOT_SEEDS);
        public static final TagKey<Item> TORCHFLOWER_SEEDS = of(SackTypes.TORCHFLOWER_SEEDS);
        public static final TagKey<Item> PITCHER_POD = of(SackTypes.PITCHER_POD);
        public static final TagKey<Item> GLOW_BERRIES = of(SackTypes.GLOW_BERRIES);
        public static final TagKey<Item> SWEET_BERRIES = of(SackTypes.SWEET_BERRIES);
        public static final TagKey<Item> NETHER_WART = of(SackTypes.NETHER_WART);
        public static final TagKey<Item> SEA_CREATURE = of(SackTypes.SEA_CREATURE);
        public static final TagKey<Item> KELP = of(SackTypes.KELP);
        public static final TagKey<Item> CORAL = of(SackTypes.CORAL);
        public static final TagKey<Item> SPONGE = of(SackTypes.SPONGE);
        public static final TagKey<Item> MELON = of(SackTypes.MELON);
        public static final TagKey<Item> PUMPKIN = of(SackTypes.PUMPKIN);
        public static final TagKey<Item> NEST = of(SackTypes.NEST);
        public static final TagKey<Item> HONEY = of(SackTypes.HONEY);
        public static final TagKey<Item> FROGLIGHT = of(SackTypes.FROGLIGHT);
        public static final TagKey<Item> SCULK = of(SackTypes.SCULK);
        public static final TagKey<Item> COBWEB = of(SackTypes.COBWEB);
        public static final TagKey<Item> BEDROCK = of(SackTypes.BEDROCK);
        public static final TagKey<Item> TORCH = of(SackTypes.TORCH);
        public static final TagKey<Item> LANTERN = of(SackTypes.LANTERN);
        public static final TagKey<Item> END_CRYSTAL = of(SackTypes.END_CRYSTAL);
        public static final TagKey<Item> BELL = of(SackTypes.BELL);
        public static final TagKey<Item> SCAFFOLDING = of(SackTypes.SCAFFOLDING);
        public static final TagKey<Item> POT = of(SackTypes.POT);
        public static final TagKey<Item> ARMOR_STAND = of(SackTypes.ARMOR_STAND);
        public static final TagKey<Item> ITEM_FRAME = of(SackTypes.ITEM_FRAME);
        public static final TagKey<Item> PAINTING = of(SackTypes.PAINTING);
        public static final TagKey<Item> SIGN = of(SackTypes.SIGN);
        public static final TagKey<Item> HEAD = of(SackTypes.HEAD);
        public static final TagKey<Item> INFESTED_STONE = of(SackTypes.INFESTED_STONE);
        public static final TagKey<Item> REDSTONE_COMPONENT = of(SackTypes.REDSTONE_COMPONENT);
        public static final TagKey<Item> RAIL = of(SackTypes.RAIL);
        public static final TagKey<Item> MINECART = of(SackTypes.MINECART);
        public static final TagKey<Item> TNT = of(SackTypes.TNT);
        public static final TagKey<Item> BUCKET = of(SackTypes.BUCKET);
        public static final TagKey<Item> FIRE_CHARGE = of(SackTypes.FIRE_CHARGE);
        public static final TagKey<Item> BONE_MEAL = of(SackTypes.BONE_MEAL);
        public static final TagKey<Item> NAME_TAG = of(SackTypes.NAME_TAG);
        public static final TagKey<Item> LEAD = of(SackTypes.LEAD);
        public static final TagKey<Item> COMPASS = of(SackTypes.COMPASS);
        public static final TagKey<Item> CLOCK = of(SackTypes.CLOCK);
        public static final TagKey<Item> MAP = of(SackTypes.MAP);
        public static final TagKey<Item> FIREWORK_ROCKET = of(SackTypes.FIREWORK_ROCKET);
        public static final TagKey<Item> SADDLE = of(SackTypes.SADDLE);
        public static final TagKey<Item> BOAT = of(SackTypes.BOAT);
        public static final TagKey<Item> GOAT_HORN = of(SackTypes.GOAT_HORN);
        public static final TagKey<Item> MUSIC_DISC = of(SackTypes.MUSIC_DISC);
        public static final TagKey<Item> TOTEM_OF_UNDYING = of(SackTypes.TOTEM_OF_UNDYING);
        public static final TagKey<Item> ARROW = of(SackTypes.ARROW);
        public static final TagKey<Item> FOOD = of(SackTypes.FOOD);
        public static final TagKey<Item> CARROT = of(SackTypes.CARROT);
        public static final TagKey<Item> POTATO = of(SackTypes.POTATO);
        public static final TagKey<Item> BEETROOT = of(SackTypes.BEETROOT);
        public static final TagKey<Item> RAW_FISH = of(SackTypes.RAW_FISH);
        public static final TagKey<Item> BOTTLE = of(SackTypes.BOTTLE);
        public static final TagKey<Item> POTION = of(SackTypes.POTION);
        public static final TagKey<Item> WHEAT = of(SackTypes.WHEAT);
        public static final TagKey<Item> CREATURE = of(SackTypes.CREATURE);
        public static final TagKey<Item> HEART_OF_THE_SEA = of(SackTypes.HEART_OF_THE_SEA);
        public static final TagKey<Item> DYE = of(SackTypes.DYE);
        public static final TagKey<Item> PAPER = of(SackTypes.PAPER);
        public static final TagKey<Item> BOOK = of(SackTypes.BOOK);
        public static final TagKey<Item> FIREWORK_STAR = of(SackTypes.FIREWORK_STAR);
        public static final TagKey<Item> SUGAR = of(SackTypes.SUGAR);
        public static final TagKey<Item> BANNER_PATTERN = of(SackTypes.BANNER_PATTERN);
        public static final TagKey<Item> POTTERY_SHERD = of(SackTypes.POTTERY_SHERD);
        public static final TagKey<Item> KEY = of(SackTypes.KEY);
        public static final TagKey<Item> SMITHING_TEMPLATE = of(SackTypes.SMITHING_TEMPLATE);
        public static final TagKey<Item> SPAWN_EGG = of(SackTypes.SPAWN_EGG);
        public static final TagKey<Item> COMMAND_BLOCK = of(SackTypes.COMMAND_BLOCK);
        // Extra Sack Types
        public static final TagKey<Item> ROPE = of(SackTypes.ROPE);
        public static final TagKey<Item> METAL_PLATE = of(SackTypes.METAL_PLATE);
        public static final TagKey<Item> TROPHY = of(SackTypes.TROPHY);
        // No sack type
        public static final TagKey<Item> NONE = of("none");

        /**
         * Creates a new <code>TagKey</code> for item tags that represent sack types.
         *
         * @param path a <code>String</code> to use as the tag name
         * @return the <code>TagKey</code> that was created
         */
        public static TagKey<Item> of(String path) {
            return TagKey.create(Registries.ITEM, InvExpUtil.location(path).withPrefix("sack_type/"));
        }

        /**
         * Creates a new <code>TagKey</code> for item tags that represent sack types.
         *
         * @param sackType a <code>ResourceKey</code> of the sack type to use as a tag name
         * @return the <code>TagKey</code> that was created
         */
        public static TagKey<Item> of(ResourceKey<derekahedron.invexp.item.sack.SackType> sackType) {
            return TagKey.create(Registries.ITEM, sackType.location().withPrefix("sack_type/"));
        }
    }

    /**
     * Holds item tags that control sack type for Inventory Expansion.
     */
    public static class SackWeight {

        public static final TagKey<Item> DOUBLE = of("double");
        public static final TagKey<Item> HALF = of("half");
        public static final TagKey<Item> THIRD = of("third");
        public static final TagKey<Item> FOURTH = of("fourth");
        public static final TagKey<Item> FIFTH = of("fifth");

        /**
         * Creates a new <code>TagKey</code> for item tags that represent sack weights.
         *
         * @param path a <code>String</code> to use as the tag name
         * @return the <code>TagKey</code> that was created
         */
        public static TagKey<Item> of(String path) {
            return TagKey.create(Registries.ITEM, InvExpUtil.location(path).withPrefix("sack_weight/"));
        }
    }
}
