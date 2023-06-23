package net.hectus.hectusblockbattles.events;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.turn.Turn;
import net.hectus.hectusblockbattles.turn.TurnInfo;
import net.hectus.hectusblockbattles.util.Cord;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        Player p = event.getPlayer();

        if (!Match.algorithm.isPlacer(p) && !Match.netherPortalAwaitIgnite) {
            p.sendMessage(Component.text(McColor.RED + "It isn't your turn right now!"));
            event.setCancelled(true);
            return;
        }

        Block b = event.getBlock();
        Cord c = new Cord(b.getX(), b.getY(), b.getZ());

        switch (b.getType().name()) {
            case "PURPLE_WOOL" -> turn(Turn.PURPLE_WOOL, p, c);
            case "POWDER_SNOW" -> turn(Turn.POWDER_SNOW, p, c);
            case "SPRUCE_TRAPDOOR" -> turn(Turn.SPRUCE_TRAPDOOR, p, c);
            case "SEA_PICKLE" -> {
                if (b instanceof SeaPickle pickle) {
                    if (pickle.getPickles() >= 4) turn(Turn.SEA_PICKLE_STACK, p, c);
                }
            }

            default -> {
                Structure.BlockData blockData = new Structure.BlockData(b.getType(), b.getX(), b.getY(), b.getZ(), Structure.blockFace(b), Structure.blockBound(b), Structure.isOpen(b));
                Match.algorithm.addBlock(blockData);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.hasChangedPosition() && System.currentTimeMillis() % 50 == 0) {
            if (!Match.algorithm.isPlacer(player)) {
                event.setCancelled(true);
                player.sendMessage(Component.text("//==================Nah!==================\\\\"));
                player.sendMessage(Component.text("|| It isn't your turn, so you can't move. ||"));
                player.sendMessage(Component.text("\\\\========================================//"));
            } else if (!Match.getPlayer(player).canMove()) {
                event.setCancelled(true);
                player.sendMessage(Component.text("//==================Nah!=============\\\\"));
                player.sendMessage(Component.text("|| You're frozen, so you can't move! ||"));
                player.sendMessage(Component.text("\\\\===================================//"));
            }
        }
    }

    private void turn(Turn turn, Player p, Cord cord) {
        BlockBattleEvents.onTurn(new TurnInfo(turn, p, cord));
    }
}
