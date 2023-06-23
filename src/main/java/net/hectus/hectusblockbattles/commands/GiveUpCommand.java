package net.hectus.hectusblockbattles.commands;

import net.hectus.hectusblockbattles.match.old.LocalMatchSingles;
import net.hectus.hectusblockbattles.match.old.MatchManager;
import net.hectus.color.McColor;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveUpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        LocalMatchSingles match = (LocalMatchSingles) MatchManager.getMatch(player.getWorld());

        if (match == null){
            player.sendMessage(Component.text(McColor.RED + "You can only use this command in a match."));
            return true;
        }

        player.sendMessage(Component.text(McColor.GREEN + "You have successfully given up."));

        if (player == match.getOppositeTurnPlayer()){
            match.end(match.getCurrentTurnPlayer(), player, player, "Gave up");
        } else {
            match.end(match.getOppositeTurnPlayer(), player, player, "Gave up");
        }

        return true;
    }
}
