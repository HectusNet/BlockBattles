package net.hectus.hectusblockbattles.turn;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.warps.Warp;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nullable;

import static net.hectus.hectusblockbattles.warps.WarpSettings.Class;

public enum Turn {
    PURPLE_WOOL(Type.ATTACK, McColor.PURPLE, "Purple Wool", Material.PURPLE_WOOL, null, Class.NEUTRAL, false),
    DINNERBONE(Type.COUNTER, McColor.GREEN, "Dinnerbone/Grumm Name Tag", Material.NAME_TAG, null, Class.NEUTRAL, false),
    NETHER_PORTAL_FRAME(Type.AWAIT, McColor.BLACK, "Nether Portal Frame", Material.OBSIDIAN, null, Class.HOT, false),
    NETHER_PORTAL_IGNITE(Type.ATTACK, McColor.PURPLE, "Ignite Nether Portal", Material.NETHER_PORTAL, null, Class.HOT, false),
    POWDER_SNOW(Type.BUFF, McColor.WHITE, "Powder Snow", Material.POWDER_SNOW, null, Class.COLD, false),
    LIGHTNING_TRIDENT(Type.COUNTER, McColor.YELLOW, "Lightning Trident", Material.TRIDENT, null, Class.WATER, false),
    SEA_PICKLE_STACK(Type.ATTACK, McColor.DARK_AQUA, "4 Sea Pickles in a Stack", Material.SEA_PICKLE, null, Class.NATURE, false),
    TNT(Type.ATTACK, McColor.RED, "Using TNT", Material.TNT, null, Class.REDSTONE, false),
    OP_GAP(Type.CUSTOM, McColor.GOLD, "Enchanted Golden Apple", Material.ENCHANTED_GOLDEN_APPLE, null, Class.DREAM, false),
    SPRUCE_TRAPDOOR(Type.COUNTER, McColor.DARK_GRAY, "Spruce Trapdoor", Material.SPRUCE_TRAPDOOR, null, Class.NEUTRAL, false),
    OAK_DOORS(Type.DEFENSE, McColor.DARK_GRAY, "Oak Door Turtling", Material.OAK_DOOR, null, Class.NEUTRAL, false),
    IRON_TRAPDOOR(Type.COUNTERATTACK, McColor.GRAY, "Iron Trapdoor", Material.IRON_TRAPDOOR, null, Class.NEUTRAL, false),
    CAULDRON(Type.ATTACK, McColor.DARK_GRAY, "Cauldron", Material.CAULDRON, null, Class.NEUTRAL, false),
    GOLD_BLOCK(Type.COUNTERATTACK, McColor.GOLD, "Gold Block", Material.GOLD_BLOCK, null, Class.NEUTRAL, false),
    JAIL_METHOD(Type.BUFF, McColor.GRAY, "Jail Method (Iron Bars)", Material.IRON_BARS, null, Class.NEUTRAL, false),
    BLACK_WOOL(Type.CUSTOM, McColor.BLACK, "Black Wool", Material.BLACK_WOOL, null, Class.NEUTRAL, false),
    SCULK_BLOCK(Type.COUNTER, McColor.DARK_AQUA, "Sculk Block", Material.SCULK, null, Class.NEUTRAL, false),
    GREEN_CARPET(Type.COUNTERATTACK, McColor.GREEN, "Green Carpet", Material.GREEN_CARPET, null, Class.NEUTRAL, false),
    ENDER_PEARL_TP(Type.COUNTER, McColor.DARK_AQUA, "Throw Ender Pearl", Material.ENDER_PEARL, null, Class.NEUTRAL, false),
    CHORUS_FRUIT_EAT(Type.COUNTER, McColor.PURPLE, "Eat Chorus Fruit to RTP", Material.CHORUS_FRUIT, null, Class.NEUTRAL, false),
    IRON_SHOVEL(Type.COUNTER, McColor.GRAY, "Throw Iron Shovel", Material.IRON_SHOVEL, null, Class.NEUTRAL, false),
    STONECUTTER(Type.COUNTER, McColor.DARK_GRAY, "Stonecutter", Material.STONECUTTER, null, Class.NEUTRAL, false),
    MAGENTA_GLAZED_TERRACOTTA(Type.COUNTERATTACK, McColor.PURPLE, "Magenta glazed Terracotta", Material.MAGENTA_GLAZED_TERRACOTTA, null, Class.NEUTRAL, false),
    CYAN_CARPET(Type.CUSTOM, McColor.AQUA, "Cyan Carpet", Material.CYAN_CARPET, null, Class.NEUTRAL, false),
    NETHER_WARP(Type.WARP, McColor.DARK_RED, "Nether Warp", Material.NETHERRACK, Warp.NETHER, null, false),
    ICE_WARP(Type.WARP, McColor.BLUE, "Ice Warp", Material.ICE, Warp.ICE, null, false),
    SNOW_WARP(Type.WARP, McColor.WHITE, "Snow Warp", Material.SNOW, Warp.SNOW, null, false),
    CLIFF_WARP(Type.WARP, McColor.GRAY, "Ice Warp", Material.STONE, Warp.CLIFF, null, false),
    UNDERWATER_WARP(Type.WARP, McColor.DARK_AQUA, "(Under)water Warp", Material.PRISMARINE, Warp.UNDERWATER, null, false),
    VOID_WARP(Type.WARP, McColor.BLACK, "Void Warp", Material.BLACK_CONCRETE, Warp.VOID, null, false),
    REDSTONE_WARP(Type.WARP, McColor.DARK_RED, "Redstone Warp", Material.REDSTONE_BLOCK, Warp.REDSTONE, null, false),
    WOOD_WARP(Type.WARP, McColor.GREEN, "Wood Warp", Material.OAK_LOG, Warp.WOOD, null, false),
    DESERT_WARP(Type.WARP, McColor.YELLOW, "Desert Warp", Material.SAND, Warp.DESERT, null, false),
    AETHER_WARP(Type.WARP, McColor.RESET, "Aether Warp", Material.WHITE_CONCRETE, Warp.AETHER, null, false),
    BOOK_WARP(Type.WARP, McColor.GOLD, "Book Warp", Material.BOOKSHELF, Warp.BOOK, null, false),
    SUN_WARP(Type.WARP, McColor.YELLOW, "Sun Warp", Material.SHROOMLIGHT, Warp.SUN, null, false),
    MUSHROOM_WARP(Type.WARP, McColor.WHITE, "Mushroom Warp", Material.BROWN_MUSHROOM_BLOCK, Warp.MUSHROOM, null, false),
    END_WARP(Type.WARP, McColor.YELLOW, "End Warp", Material.END_STONE, Warp.END, null, false),
    AMETHYST_WARP(Type.WARP, McColor.PINK, "Amethyst Warp", Material.AMETHYST_BLOCK, Warp.AMETHYST, null, false),
    HEAVEN_WARP(Type.WARP, McColor.WHITE, "Heaven Warp", Material.WHITE_WOOL, Warp.HEAVEN, null, false),
    HELL_WARP(Type.WARP, McColor.DARK_RED, "Hell Warp", Material.NETHER_BRICKS, Warp.HELL, null, false),
    INDUSTRIAL_WARP(Type.WARP, McColor.DARK_GRAY, "Industrial Warp", Material.IRON_BLOCK, Warp.INDUSTRIAL, null, false);

    public final Type type;
    public final McColor color;
    public final String name;
    public final Material material;
    public final Warp warp;
    public final Class clazz;
    public final boolean mob;

    @Contract(pure = true)
    Turn(Type t, McColor co, String n, Material ma, @Nullable Warp w, Class cl, boolean mo) {
        type = t;
        color = co;
        name = n;
        material = ma;
        warp = w;
        clazz = cl;
        mob = mo;
    }

    public enum Type { ATTACK, COUNTER, COUNTERATTACK, WARP, BUFF, DEFENSE, AWAIT, CUSTOM }
}
