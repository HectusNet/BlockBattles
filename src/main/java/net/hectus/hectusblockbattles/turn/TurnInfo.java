package net.hectus.hectusblockbattles.turn;

import net.hectus.hectusblockbattles.util.Cord;
import org.bukkit.entity.Player;

public record TurnInfo(Turn turn, Player player, Cord cord) {}
