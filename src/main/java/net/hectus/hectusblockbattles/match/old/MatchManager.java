package net.hectus.hectusblockbattles.match.old;

import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class MatchManager {
    private static final Set<OldMatch> OLD_MATCHES = new HashSet<>();

    public static @Nullable OldMatch getMatch(World world) {
        for (OldMatch oldMatch : OLD_MATCHES) {
            if (oldMatch.getGameMap().getWorld() == world) return oldMatch;
        }
        return null;
    }

    public static void addMatch(OldMatch oldMatch) {
        OLD_MATCHES.add(oldMatch);
    }
}
