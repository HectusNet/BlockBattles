package net.hectus.hectusblockbattles.structures.v2;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.match.Match;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Contract;

public class AlgorithmTimer {
    public int ticks = 10;
    public int laps = 0;
    public BukkitTask task;
    public Algorithm algorithm;

    @Contract(pure = true)
    public AlgorithmTimer(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void instance(Algorithm algorithm) {
        this.algorithm = algorithm;

        task = Bukkit.getScheduler().runTaskTimer(HBB.getPlugin(HBB.class), () -> {
            ticks--;

            algorithm.placer.sendActionBar(Component.text(McColor.GRAY + String.valueOf(ticks)));

            if (ticks <= 0) {
                ticks = 10;
                laps += 1;
                algorithm.calculateChances();
            }
            if (laps >= 20) {
                Player opponent;
                if (Match.algorithm.isPlacer(Match.p1.player))
                    opponent = Match.p2.player;
                else opponent = Match.p1.player;

                Match.algorithm.timer.stop();

                Match.algorithm.clear();
                Match.algorithm.start(opponent);

                HBB.WORLD.showTitle(BlockBattleEvents.subtitle(McColor.GOLD + Match.getOpponent().player.getName() + " was too slow!"));
            }
        }, 0L, 1);
    }

    public void lap(Algorithm algorithm) {
        stop();
        instance(algorithm);
    }

    public void stop() {
        task.cancel();
        ticks = 10;
        laps = 0;
    }
}
