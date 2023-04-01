package net.hectus.hectusblockbattles.Warps;

public enum Warp {
    DEFAULT(true),
    NETHER(false),
    GEODE(false),
    CAVE(false);

    private boolean night;

    public boolean isNight() {
        return night;
    }

    Warp(boolean night) {
        this.night = night;
    }
}
