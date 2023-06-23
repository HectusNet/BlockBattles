package net.hectus.hectusblockbattles.events;

<<<<<<< Updated upstream
import net.hectus.hectusblockbattles.match.LocalMatchSingles;
import net.hectus.hectusblockbattles.match.MatchManager;
import net.hectus.hectusblockbattles.playermode.PlayerMode;
import net.hectus.hectusblockbattles.playermode.PlayerModeManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BlockBattleEvents  implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerModeManager.initializePlayerMode(player);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLoc = player.getLocation();
        int x = playerLoc.getBlockX();
        int z = playerLoc.getBlockZ();

        LocalMatchSingles localMatchSingles = (LocalMatchSingles) MatchManager.getMatch(player.getWorld());
        if (localMatchSingles == null) {
            return;
        }

        if (PlayerModeManager.getPlayerMode(player) != PlayerMode.BLOCK_BATTLES) {
            return;
        }

        if (localMatchSingles.getCurrentTurnPlayer() != player && event.hasChangedBlock()) {
            localMatchSingles.end(localMatchSingles.getCurrentTurnPlayer(), player, player, "moved out of turn");
        }

        if (!localMatchSingles.checkBounds(x, z)) {
            localMatchSingles.end(localMatchSingles.getOppositeTurnPlayer(), player,  player, "stepped out of bounds");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (PlayerModeManager.getPlayerMode(event.getPlayer()) != PlayerMode.BLOCK_BATTLES) {
            return;
=======
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.hectus.hectusblockbattles.util.BBPlayer;
import net.hectus.hectusblockbattles.util.Cord;
import net.hectus.hectusblockbattles.warps.Warp;
import net.hectus.hectusblockbattles.warps.WarpManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Random;

import static net.hectus.hectusblockbattles.warps.WarpSettings.Class.*;

public class BlockBattleEvents {

    public static void onStructurePlace(@NotNull StructurePlaceEvent event) {
        Structure structure = event.structure();
        String name = structure.name;

        if (name.contains("WARP")) {
            String warp = name.replace("_WARP", "");

            WarpManager.warp(Warp.valueOf(warp), event.placer(), event.opponent());
>>>>>>> Stashed changes
        }

        event.getPlayer().sendMessage("You can't break blocks in Block Battles!");
        event.setCancelled(true);
    }

<<<<<<< Updated upstream
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player placerPlayer = event.getPlayer();

        if (PlayerModeManager.getPlayerMode(placerPlayer) != PlayerMode.BLOCK_BATTLES) {
            return;
        }

        Block block = event.getBlock();
        Material material = block.getType();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();

        LocalMatchSingles localMatchSingles = (LocalMatchSingles) MatchManager.getMatch(placerPlayer.getWorld());

        Player currentTurnPlayer = localMatchSingles.getCurrentTurnPlayer();
        Player oppositeTurnPlayer = localMatchSingles.getOppositeTurnPlayer();

        // NOTE: probably all this unused code will be replaced with proper gameplay logic made in LocalMatchSingles class

//        Material lastBlock = localMatchSingles.getLastBlock().getType();
//        boolean turn = localMatchSingles.getTurn();
//        double gameScore = localMatchSingles.getGameScore();
//        if (currentTurnPlayer != placerPlayer) {
//            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer,"placed a block not in their turn");
//            return;
//        }
//        if (!localMatchSingles.check(x, y, z, material, placerPlayer)) {
//            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer, "placed a block incorrectly");
//            return;
//        }
//        if (!BasicBlocks.blockCheck(material, localMatchSingles.getWarp(), localMatchSingles.getNight())) {
//            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer, "placed an illegal block");
//            return;
//        }
//        if (!localMatchSingles.checkBounds(x, z)) {
//            localMatchSingles.end(oppositeTurnPlayer, placerPlayer, placerPlayer,"placed a block out of bounds");
//            return;
//        }
//        gameScore = BasicBlocks.calculateGameScore(gameScore, material, lastBlock, turn, localMatchSingles.getBlockBoosts());
//
//        localMatchSingles.setGameScore(gameScore);
//        if (gameScore > 5) {
//            localMatchSingles.end(localMatchSingles.getPlayer(true), localMatchSingles.getPlayer(false), localMatchSingles.getPlayer(true), "got a high score");
//        } else if (gameScore < -5) {
//            localMatchSingles.end(localMatchSingles.getPlayer(false), localMatchSingles.getPlayer(true), localMatchSingles.getPlayer(false), "got a high score");
//        }
=======
    public static void onTurn(TurnInfo turn) {
        Match.addTurn(turn);

        String name = turn.turn().name().toLowerCase();

        if (name.contains("warp")) {
            WarpManager.warp(Warp.valueOf(name), turn.player(), Match.getOpponent().player);
            return;
        }

        BBPlayer player = Match.getPlayer(turn.player());
        BBPlayer opponent = Match.getOpponent();
        Cord cord = turn.cord();

        switch (turn.turn()) {
            case PURPLE_WOOL -> Match.win();
            case DINNERBONE -> {
                if (player.isAttacked()) {
                    if (Match.getLatestTurn().turn().mob) {
                        player.setAttacked(false);
                    } else {
                        Match.lose();
                    }
                }
                Match.next();
            }
            case NETHER_PORTAL_FRAME -> Match.netherPortalAwaitIgnite = true;
            case NETHER_PORTAL_IGNITE -> {
                if (Match.netherPortalAwaitIgnite) {
                    Match.getOpponent().setAttacked(true);
                    Match.netherPortalAwaitIgnite = false;
                }
                Match.next();
            }
            case POWDER_SNOW -> {
                Match.getOpponent().setMovement(false);
                Match.getOpponent().startDieCounter(2);
            }
            case LIGHTNING_TRIDENT -> {
                // Requires the LIGHTNING_ROD turn first
                // TODO: Add if the last move was a lighting rod
                turn.player().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 1));
                Match.getOpponent().player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 1));
            }
            case SEA_PICKLE_STACK -> Match.getOpponent().addExtraTurns(2);
            case TNT -> {
                if (player.isDefended() && !opponent.isDefended()) Match.win();
                else if (!player.isDefended() && opponent.isDefended()) Match.lose();

                Match.draw();
            }
            case OP_GAP -> {
                if (new Random().nextBoolean()) player.addLuck(100);
                player.player.clearActivePotionEffects();
            }
            case SPRUCE_TRAPDOOR -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, WATER, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
            }
            case IRON_TRAPDOOR -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, WATER, REDSTONE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                }
            }
            case CAULDRON -> opponent.setAttacked(true);
            case GOLD_BLOCK -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, HOT, NATURE) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case BLACK_WOOL -> {
                player.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 1));
                opponent.player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, -1, 1));
            }
            case SCULK_BLOCK -> {
                if (player.isAttacked() && Match.latestTurnIsClass(NEUTRAL, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, -1, 1));
                    opponent.removeLuck(10);
                }
            }
            case GREEN_CARPET -> {
                Material latestMaterial = Match.getLatestTurn().turn().material;
                if (player.isAttacked() && latestMaterial.name().contains("WOOL") && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                    opponent.setAttacked(true);
                }
            }
            case CYAN_CARPET -> {
                if (player.isAttacked() && Match.latestTurnIsClass(REDSTONE, DREAM) && Match.latestTurnIsUnder(cord)) {
                    player.setAttacked(false);
                } else if (!player.isAttacked()) {
                    player.addExtraTurns(1);

                }
            }
        }

        if (player.isAttacked()) Match.lose();
    }


    @Contract("_ -> new")
    public static @NotNull Title subtitle(String content) {
        return Title.title(
                Component.empty(),
                Component.text(content),
                Title.Times.times(
                        Duration.ZERO,
                        Duration.ofMillis(500L),
                        Duration.ofMillis(200L)
                )
        );
>>>>>>> Stashed changes
    }

}
