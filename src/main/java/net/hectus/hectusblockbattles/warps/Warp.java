package net.hectus.hectusblockbattles.warps;

<<<<<<< Updated upstream
=======
import org.jetbrains.annotations.Contract;

import net.hectus.hectusblockbattles.util.Cord;
import static net.hectus.hectusblockbattles.warps.WarpSettings.Class;
import static net.hectus.hectusblockbattles.warps.WarpSettings.*;

>>>>>>> Stashed changes
public enum Warp {
    DEFAULT(1,true),
    NETHER(0.8, false),
    ICE(0.69, false),
    SNOW(0.8, false),
    CLIFF(0.67, false),
    UNDERWATER(0.73, false),
    VOID(0.67, true),
    REDSTONE(0.65, false),
    FOREST(1, false),
    DESERT(0.8, false),
    AETHER(0.8, false),
    BOOK(0.2, false),
    SUN(0.75, false),
    MUSHROOM(0.6, false),
    END(0.85, true),
    GEODE(0.3, false), // Amethyst?
    CAVE(0, false), // Doesn't exist in the docs
    HEAVEN(0.5, false),
    HELL(0.5, false);

    private final double chance;
    private final boolean isNight;

    public boolean isNight() {
        return isNight;
    }

    public double getChance() {
        return chance;
    }

    Warp(double chance, boolean isNight) {
        this.chance = chance;
        this.isNight = isNight;
    }
}
