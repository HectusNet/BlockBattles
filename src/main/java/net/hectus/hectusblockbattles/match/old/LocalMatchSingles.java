package net.hectus.hectusblockbattles.match.old;

<<<<<<< Updated upstream:src/main/java/net/hectus/hectusblockbattles/match/LocalMatchSingles.java
import net.hectus.hectusblockbattles.IngameShop;
import net.hectus.hectusblockbattles.maps.GameMap;
import net.hectus.hectusblockbattles.playermode.PlayerMode;
import net.hectus.hectusblockbattles.playermode.PlayerModeManager;
import net.hectus.hectusblockbattles.structures.Structure;
import net.hectus.hectusblockbattles.structures.Structures;
=======
import net.hectus.hectusblockbattles.util.Cord;
import net.hectus.hectusblockbattles.maps.GameMap;
import net.hectus.hectusblockbattles.playermode.PlayerMode;
import net.hectus.hectusblockbattles.playermode.PlayerModeManager;
import net.hectus.hectusblockbattles.warps.Warp;
import net.hectus.hectusblockbattles.warps.WarpSettings;
import net.hectus.color.McColor;
import net.hectus.time.Time;
>>>>>>> Stashed changes:src/main/java/net/hectus/hectusblockbattles/match/old/LocalMatchSingles.java
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.*;
import java.util.logging.Level;

<<<<<<< Updated upstream:src/main/java/net/hectus/hectusblockbattles/match/LocalMatchSingles.java
public class LocalMatchSingles implements Match, Listener {
=======
@SuppressWarnings("deprecation")
public class LocalMatchSingles implements OldMatch, Listener {
>>>>>>> Stashed changes:src/main/java/net/hectus/hectusblockbattles/match/old/LocalMatchSingles.java
    private final JavaPlugin plugin;
    private final GameMap gameMap;
    private final List<Player> players;
    private final HashMap<Player, Set<Block>> playerPlacedBlocks;

    private BukkitTask main;
    private int turnIndex;
    private int turnTimeLeft;
    private Location location;

    private Block lastBlock;

    public LocalMatchSingles(JavaPlugin plugin, GameMap gameMap, Player p1, Player p2) {
        this.plugin = plugin;
        this.gameMap = gameMap;
        this.location = new Location(gameMap.getWorld(), 0, 1, 0);

        players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        playerPlacedBlocks = new HashMap<>();

        turnIndex = -1;
        turnTimeLeft = -1;
    }

    @Override
    public void nextTurn(boolean wasSkipped) {
        turnIndex++;
        if (turnIndex >= players.size()) {
            turnIndex = 0;
        }

        // 15 seconds, multiplied by 20 for ticks
        turnTimeLeft = 15 * 20;
    }

    @Override
    public GameMap getGameMap() {
        return gameMap;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    public void end(Player won, Player lost, Player causeSubject, String cause) {
        PlayerModeManager.initializePlayerMode(won);
        PlayerModeManager.initializePlayerMode(lost);
        if (causeSubject != null) {
            if (causeSubject == won) {
                cause = "you " + cause;
            } else {
                cause = "your opponent " + cause;
            }
        }
        won.sendMessage("You won the match because " + cause);
        lost.sendMessage("You lost the match because " + cause);
    }

    @Override
    public boolean start() {
        if (isRunning()) {
            return true;
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);

        // Teleport players
        for (Player player : players) {
            player.teleport(location);
            player.setFlying(false);
            player.setVelocity(new Vector(0, 0, 0));
        }

        // Enter shop phase (45-second buy time)
        for (Player player : players) {
            PlayerModeManager.setPlayerMode(PlayerMode.SHOP_PHASE, player);
            // TODO: OPEN SHOP INVENTORY
            IngameShop.displayShop(player);
        }

        final int BUY_TIME = 45 * 20;
        new BukkitRunnable() {
            int t = BUY_TIME;
            @Override
            public void run() {
                if (!isRunning() || getGameMap().getWorld() == null) {
                    this.cancel();
                }

                if (t % 20 == 0) {
                    for (Player player : players) {
                        player.sendActionBar(Component.text(t, NamedTextColor.GRAY));
                    }
                }

                if (t <= 0) {
                    for (Player player : players) {
                        PlayerModeManager.setPlayerMode(PlayerMode.BLOCK_BATTLES, player);
                        player.closeInventory();
                    }
                    nextTurn(false);
                    this.cancel();
                }

                t--;
            }
        }.runTaskTimer(plugin, 100, 1);

        // Run main match
        main = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isRunning() || getGameMap().getWorld() == null) {
                    this.cancel();
                }

                for (Player player : players) {
                    player.sendActionBar(players.get(turnIndex).displayName().color(NamedTextColor.YELLOW)
                            .append(Component.text("'s turn. Time left: ", NamedTextColor.GRAY))
                            .append(Component.text(turnTimeLeft / 20d, NamedTextColor.YELLOW)));
                }

                turnTimeLeft--;

                if (turnTimeLeft <= 0) {
                    for (Player player : players) {
                        player.sendMessage(players.get(turnIndex).displayName().color(NamedTextColor.YELLOW)
                                .append(Component.text(" did not play in time!", NamedTextColor.RED)));
                    }
                    nextTurn(true);
                }
            }
        }.runTaskTimer(plugin, 101 + BUY_TIME, 1);

        return isRunning();
    }

    @Override
    public void stop(boolean isAbrupt) {
        if (main != null) {
            main.cancel();
        }
        main = null;

        HandlerList.unregisterAll(this);

        for (Player player : getGameMap().getWorld().getPlayers()) {
            player.setVelocity(new Vector(0, 2, 0));
            player.setFlying(true);
            if (isAbrupt) {
                sendPlayerToLobby(player);
            }
        }

        if (isAbrupt) {
            getGameMap().unload();
            return;
        }

        new BukkitRunnable() {
            int i = 10 * 20;
            @Override
            public void run() {
                for (Player player : getGameMap().getWorld().getPlayers()) {
                    player.sendActionBar(Component.text("Returning to lobby in ", NamedTextColor.YELLOW)
                            .append(Component.text(i / 20, NamedTextColor.RED))
                            .append(Component.text("...", NamedTextColor.YELLOW)));
                }

                if (getGameMap().getWorld().getPlayers().size() < 1) {
                    this.cancel();
                }

                if (i <= 0) {
                    for (Player player : getGameMap().getWorld().getPlayers()) {
                        sendPlayerToLobby(player);
                    }
                    getGameMap().unload();
                    this.cancel();
                }

                i--;
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void sendPlayerToLobby(Player player) {
        PlayerModeManager.setPlayerMode(PlayerMode.DEFAULT, player);
        // TODO: SEND TO LOBBY
        player.teleport(getGameMap().getSourceWorld().getSpawnLocation());
    }

    @Override
    public Player getCurrentTurnPlayer() {
        return players.get(turnIndex);
    }

    @Override
    public double getGameScore() {
        return 0;
    }

    public Player getPlayer(boolean turn) {
        return turn ? players.get(1) : players.get(0);
    }

    public boolean getTurn() {
        return turnIndex == 1;
    }

    public Player getOppositeTurnPlayer() {
        return getPlayer(!getTurn());
    }

<<<<<<< Updated upstream:src/main/java/net/hectus/hectusblockbattles/match/LocalMatchSingles.java
    public Block getLastBlock() {
        return lastBlock;
    }

    public Location getLocation() {
        return location;
    }
=======
    public boolean outOfBounds(double x, double z) {
        Cord m = this.warp.middle;

        Cord lc = new Cord(m.x() - 4, m.y(), m.z() - 4);
        Cord hc = new Cord(m.x() + 4, m.y(), m.z() + 4);
>>>>>>> Stashed changes:src/main/java/net/hectus/hectusblockbattles/match/old/LocalMatchSingles.java

    public boolean checkBounds(int x, int z) {
        return x >= location.x() && x < location.x() + 9 && z >= location.z() && z < location.z() + 9;
    }

    @Override
    public boolean isRunning() {
        return main != null && !main.isCancelled();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!players.remove(e.getPlayer())) {
            return;
        }

        if (players.size() <= 1) {
            stop(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }

        Player player = e.getPlayer();
        int index = players.indexOf(player);
        if (index < 0) {
            return;
        }

        if (turnIndex < 0 || player != (players.get(turnIndex))) {
            e.setCancelled(true);
            return;
        }

        playerPlacedBlocks.putIfAbsent(player, new HashSet<>());
        Set<Block> blocks = playerPlacedBlocks.get(player);
        blocks.add(e.getBlockPlaced());
        lastBlock = e.getBlockPlaced();

        Structure build = new Structure("Build", playerPlacedBlocks.get(e.getPlayer()));
        for (Structure structure : Structures.getAllStructures()) {
            Bukkit.getLogger().log(Level.INFO, "TESTING: " + structure.getName());
            if (structure.hasSubset(build)) {
                if (structure.getPlacedBlocks().size() == blocks.size()) {
                    // Perfect match
                    Bukkit.broadcast(player.displayName()
                            .append(Component.text(" played ", NamedTextColor.YELLOW))
                            .append(Component.text(structure.getName())));
                    blocks.clear();
                }

                return;
            }
        }

        // No such structure exists: Misplace!
        player.showTitle(Title.title(Component.text(""), Component.text("Misplace!", NamedTextColor.RED), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));

        for (Block block : blocks) {
            player.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5d, 0.5d, 0.5d), 10, 0.5d, 0.5d, 0.5d, block.getBlockData());
            block.setType(Material.AIR);
        }

        blocks.clear();

        nextTurn(true);
    }
<<<<<<< Updated upstream:src/main/java/net/hectus/hectusblockbattles/match/LocalMatchSingles.java
=======
    
    public void failed() {
        getOppositeTurnPlayer().showTitle(Title.title(Component.empty(), Component.text(McColor.RED + "Failed!"), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
        getCurrentTurnPlayer().showTitle(Title.title(Component.empty(), Component.text(McColor.RED + "Failed!"), Title.Times.times(Duration.ofMillis(250), Duration.ofMillis(500), Duration.ofMillis(250))));
    }
>>>>>>> Stashed changes:src/main/java/net/hectus/hectusblockbattles/match/old/LocalMatchSingles.java
}
