package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.match.Match;
import net.hectus.time.Time;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Watch {
    public BukkitTask shop;
    public Time shopLeft = new Time(45);
    public void startShop() {
        shop = Bukkit.getScheduler().runTaskTimer(HBB.getPlugin(HBB.class), () -> {
            shopLeft.decrement();

            if (shopLeft.getAs(Time.Unit.S) == 0) {
                shop.cancel();
            }
        }, 0L, 20L);
    }

    public BukkitTask move;
    public Time moveLeft = new Time(10);
    public void startMove() {
        move = Bukkit.getScheduler().runTaskTimer(HBB.getPlugin(HBB.class), () -> {
            moveLeft.decrement();

            if (moveLeft.getAs(Time.Unit.S) == 0) {
                move.cancel();
            }
        }, 0L, 20L);
    }

    public BukkitTask game;
    public Time gameLeft;
    public void startGame() {
        game = Bukkit.getScheduler().runTaskTimer(HBB.getPlugin(HBB.class), () -> {
            gameLeft.decrement();

            if (gameLeft.getAs(Time.Unit.S) == 0) {
                Match.lose();
                Match.win();
            }
        }, 0L, 20L);
    }
}
