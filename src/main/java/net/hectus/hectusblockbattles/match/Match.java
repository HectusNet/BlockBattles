package net.hectus.hectusblockbattles.match;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.InGameShop;
import net.hectus.hectusblockbattles.structures.v2.Algorithm;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.hectus.hectusblockbattles.util.BBPlayer;
import net.hectus.hectusblockbattles.util.Cord;
import net.hectus.hectusblockbattles.warps.Warp;
import net.hectus.hectusblockbattles.warps.WarpSettings;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

<<<<<<< Updated upstream
public interface Match {
    boolean start();
    void stop(boolean isAbrupt);
    Player getCurrentTurnPlayer();
    void nextTurn(boolean wasSkipped);
    GameMap getGameMap();
    List<Player> getPlayers();
    boolean isRunning();
    double getGameScore();
=======
@SuppressWarnings("unused")
public class Match {
    public static BBPlayer p1;
    public static BBPlayer p2;
    public static boolean shopPhase;
    public static Warp currentWarp;
    public static List<TurnInfo> turnHistory = new ArrayList<>();
    public static Algorithm algorithm = new Algorithm();

    public static boolean netherPortalAwaitIgnite, blackWoolDebuff = false;

    public static void start(Player p1, Player p2) {
        Match.p1 = new BBPlayer(p1);
        Match.p2 = new BBPlayer(p2);

        currentWarp = Warp.DEFAULT;

        InGameShop.displayShop(p1, InGameShop.HotBar.NORMAL);
        InGameShop.displayShop(p2, InGameShop.HotBar.NORMAL);
        shopPhase = true;
    }

    public static BBPlayer getOpponent() {
        if (algorithm.isPlacer(p1.player)) {
            return p2;
        } else {
            return p1;
        }
    }

    public static BBPlayer getPlacer() {
        if (algorithm.isPlacer(p1.player)) {
            return p1;
        } else {
            return p2;
        }
    }

    @Contract(pure = true)
    public static BBPlayer getOpposite(BBPlayer player) {
        if (player == p1) {
            return p2;
        } else {
            return p1;
        }
    }

    @Contract(pure = true)
    public static BBPlayer getPlayer(Player player) {
        if (p1.player == player) {
            return p1;
        } else {
            return p2;
        }
    }

    public static void win() {
        getOpponent().player.setHealth(0);
        getOpponent().showTitle("", McColor.RED + "You lost!", null);

        getPlacer().player.setHealth(20.0);
        getPlacer().showTitle("", McColor.GREEN + "You won!", null);
    }

    public static void win(@NotNull BBPlayer player) {
        BBPlayer opponent;
        if (player == p1) {
            opponent = p2;
        } else {
            opponent = p1;
        }

        opponent.player.setHealth(0);
        opponent.showTitle("", McColor.RED + "You lost!", null);

        player.player.setHealth(20.0);
        player.showTitle("", McColor.GREEN + "You won!", null);
    }

    public static void lose() {
        getPlacer().player.setHealth(0);
        getPlacer().showTitle("", McColor.RED + "You lost!", null);

        getOpponent().player.setHealth(20.0);
        getOpponent().showTitle("", McColor.GREEN + "You won!", null);
    }

    public static void draw() {
        getPlacer().player.setHealth(0);
        getPlacer().showTitle("", McColor.GOLD + "Draw!", null);

        getOpponent().player.setHealth(0);
        getOpponent().showTitle("", McColor.GOLD + "Draw!", null);
    }

    public static void shopDone() {
        shopPhase = false;
        algorithm.start(p1.player);
    }

    public static void addTurn(TurnInfo turn) {
        turnHistory.add(turn);
    }

    public static void next() {
        algorithm.timer.stop();
        algorithm.clear();
        algorithm.start(getOpponent().player);
        p1.count();
        p2.count();
    }

    public static TurnInfo getLatestTurn() {
        return turnHistory.get(turnHistory.size() - 1);
    }

    public static boolean latestTurnIsClass(WarpSettings.Class @NotNull ... classes) {
        Turn latestTurn = getLatestTurn().turn();
        for (WarpSettings.Class clazz : classes) {
            if (latestTurn.clazz == clazz) return true;
        }
        return false;
    }

    public static boolean latestTurnIsUnder(@NotNull Cord cord) {
        Block underBlock = new Location(HBB.WORLD, cord.x(), cord.y(), cord.z()).getBlock();
        return Match.getLatestTurn().turn().material == underBlock.getType();
    }

    public enum PlayerState {
        NONE,
        SHOP_NORMAL,
        SHOP_NORMAL_FINISHED,
        SHOP_OVERTIME,
        SHOP_OVERTIME_FINISHED,
        GAME
    }
>>>>>>> Stashed changes
}
