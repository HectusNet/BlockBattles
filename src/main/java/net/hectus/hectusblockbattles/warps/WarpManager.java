package net.hectus.hectusblockbattles.warps;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.util.Cord;
import net.hectus.util.Randomizer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WarpManager {
    public static void warp(Warp warp, Player activator, Player otherPlayer) {
        if (Randomizer.boolByChance(warp.chance * 100)) {
            Cord cord = warp.middle;

            activator.teleport(new Location(activator.getWorld(), cord.x() - 2, cord.y(), cord.z()));
            otherPlayer.teleport(new Location(activator.getWorld(), cord.x() + 2, cord.y(), cord.z()));

            activator.showTitle(BlockBattleEvents.subtitle(McColor.GREEN + "Success!"));
            otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + activator.getName() + "'s Warp succeeded!"));
        } else {
            activator.showTitle(BlockBattleEvents.subtitle(McColor.RED + "Fail!"));
            otherPlayer.showTitle(BlockBattleEvents.subtitle(McColor.YELLOW + activator.getName() + "'s Warp failed!"));
        }
    }
}
