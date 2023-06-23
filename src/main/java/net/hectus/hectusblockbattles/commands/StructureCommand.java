package net.hectus.hectusblockbattles.commands;

<<<<<<< Updated upstream
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import net.hectus.hectusblockbattles.structures.Structure;
=======
import net.hectus.hectusblockbattles.util.Cord;
import net.hectus.hectusblockbattles.structures.v2.Structure;
import net.hectus.hectusblockbattles.structures.v2.StructureManager;
import net.hectus.text.Completer;
import net.hectus.color.McColor;
>>>>>>> Stashed changes
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public class StructureCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public StructureCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
<<<<<<< Updated upstream
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players may execute this command.",  NamedTextColor.RED));
            return true;
=======
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player player = (Player) sender;

        if (args.length == 9) {
            if (args[0].equals("save")) {
                player.sendMessage(Component.text(McColor.GRAY + "Saving structure named " + args[7] + "..."));
                long start = System.currentTimeMillis();

                // The Structure's corners / boundaries
                Cord c1 = new Cord(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                Cord c2 = new Cord(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));

                Structure structure = Structure.save(c1, c2, args[8], Boolean.parseBoolean(args[7]), player.getWorld());
                boolean worked = StructureManager.save(structure);
                StructureManager.loadAll(false);

                player.sendMessage(Component.text(
                        (worked ?
                                McColor.GREEN + "Successfully saved the structure!" :
                                McColor.RED + "Something went wrong! Please take a look at the server logs. "
                            ) + "Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));

                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equals("load")) {
                Structure structure = StructureManager.get(args[1]);

                if (structure == null) {
                    player.sendMessage(Component.text(McColor.RED + "Found no structure named " + args[1] + "!"));
                    return false;
                }

                player.sendMessage(Component.text(McColor.GRAY + "Loading structure named " + structure.name + "..."));
                long start = System.currentTimeMillis();

                // The base location on which all relatives are based on
                Location loc = player.getLocation();
                World world = loc.getWorld();

                for (Structure.BlockData data : structure.blockData) { // Looping through all the structure's data

                    Location relativeLocation = new Location(world, loc.getBlockX() + data.x(), loc.getBlockY() + data.y(), loc.getBlockZ() + data.z());
                    Block block = relativeLocation.getBlock();

                    block.setType(data.material());

                    if (structure.useExactBlockData) { // If some more advanced metadata should be used
                        setBlockDirection(block, data.direction());
                        setBlockBound(block, data.blockBound());
                        setOpen(block, data.open());
                    }

                }

                player.sendMessage(Component.text(McColor.GREEN + "Successfully loaded the structure! Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));

                return true;
            } else if (args[0].equals("remove")) {
                player.sendMessage(Component.text(McColor.GRAY + "Removing structure named " + args[1] + "..."));
                long start = System.currentTimeMillis();

                boolean worked = StructureManager.remove(args[1]);

                StructureManager.loadAll(true);

                player.sendMessage(Component.text(
                        (worked ?
                                McColor.GREEN + "Successfully removed the structure!" :
                                McColor.RED + "Something went wrong! Please take a look at the server logs. "
                        ) + "Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));

                return true;
            }
        } else if (args.length == 1) {
            if (args[0].equals("reload")) {
                player.sendMessage(Component.text(McColor.GRAY + "Reloading the structure data..."));
                long start = System.currentTimeMillis();

                StructureManager.loadAll(true);

                player.sendMessage(Component.text(McColor.GREEN + "Successfully reloaded the structure data! Time elapsed : " + (System.currentTimeMillis() - start) + "ms"));

                return true;
            }
>>>>>>> Stashed changes
        }

        String structureName = args[6];

        if (args.length == 7) {
            Structure toSerialize;
            try {
                toSerialize = new Structure(player.getWorld(), structureName, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
            } catch (NumberFormatException exception) {
                sender.sendMessage(Component.text("Wrong args!", NamedTextColor.RED));
                return false;
            }

            Bukkit.getLogger().log(Level.INFO, "DEBUG: Received structure '" + toSerialize.getName() + "', details:");
            Bukkit.getLogger().log(Level.INFO, toSerialize.getName());
            Bukkit.getLogger().log(Level.INFO, toSerialize.getBlocks().toString());
            Bukkit.getLogger().log(Level.INFO, toSerialize.getMaterials().toString());
            Bukkit.getLogger().log(Level.INFO, toSerialize.getPlacedBlocks().toString());
            Bukkit.getLogger().log(Level.INFO, toSerialize.getBoundary().toString());
            Bukkit.getLogger().log(Level.INFO, "DEBUG: Attempting to serialize...");

            Gson gson = new Gson();
            File structuresFolder = new File(plugin.getDataFolder(), "structures");
            if (!structuresFolder.exists()) {
                if (!structuresFolder.mkdir()) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to create structures folder.");
                    return true;
                }
            }
            File structureFile = new File(structuresFolder, structureName + ".json");
            try (FileWriter fileWriter = new FileWriter(structureFile)) {
                gson.toJson(toSerialize, fileWriter);
            } catch (IOException | JsonIOException e) {
                Bukkit.getLogger().log(Level.WARNING, "Encountered an exception.");
                e.printStackTrace();
                return true;
            }

            Bukkit.getLogger().log(Level.INFO, "DEBUG: Serialized.");

            sender.sendMessage(Component.text("You did it! Check logs for info.", NamedTextColor.GREEN));
            return true;
        }

        return true;
    }
}
