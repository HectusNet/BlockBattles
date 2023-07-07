package net.hectus.hectusblockbattles;

import net.hectus.hectusblockbattles.commands.MatchCommand;
import net.hectus.hectusblockbattles.commands.StructureCommand;
import net.hectus.hectusblockbattles.events.InGameShopEvents;
import net.hectus.hectusblockbattles.events.PlayerEvents;
import net.hectus.hectusblockbattles.structures.v2.StructureManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public final class HBB extends JavaPlugin {
    public static Logger LOGGER;
    public static World WORLD;
    public static File dataFolder;

    @Override
    public void onEnable() {
        LOGGER = getLogger();

        // // Don't remove this, this is very important to not break anything!
        // try { PlayerDatabase.connect(); }
        // catch (SQLException e) { throw new RuntimeException(e); }
        // //=================================================================

        getServer().getPluginManager().registerEvents(new InGameShopEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        LOGGER.info("Hectus BlockBattles started.");

        dataFolder = getDataFolder();
        if (!dataFolder.exists()) dataFolder.mkdirs();

        // File mapsFolder = new File(dataFolder, "maps");
        // if (!mapsFolder.exists()) mapsFolder.mkdirs();

        StructureManager.loadAll(true);

        Objects.requireNonNull(getCommand("structure")).setExecutor(new StructureCommand());
        Objects.requireNonNull(getCommand("match")).setExecutor(new MatchCommand());

        WORLD = Bukkit.getWorld("world");
    }

    @Override
    public void onDisable() {
        // // Don't remove this, this is very important to not break anything!
        // try { PlayerDatabase.disconnect(); }
        // catch (SQLException e) { throw new RuntimeException(e); }
        // //=================================================================
        LOGGER.info("Hectus BlockBattles stopped.");
    }
}
