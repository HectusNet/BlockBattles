package net.hectus.hectusblockbattles.maps;

import net.hectus.hectusblockbattles.warps.Warp;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

public class LocalGameMap implements GameMap {
    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World world;

    private World sourceWorld;

    private Warp currentWarp;
    private boolean night;

    public LocalGameMap(File mapsFolder, String mapName, boolean loadOnInit, World sourceWorld) {
        this.sourceWorldFolder = new File(mapsFolder, mapName);
        this.night = false;
        this.currentWarp = Warp.DEFAULT;
        this.sourceWorld = sourceWorld;
        if (loadOnInit) load();
    }

    @Override
    public boolean load() {
        if (isLoaded()) return true;

        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer(),
                sourceWorldFolder.getName() + "_" + System.currentTimeMillis()
        );

        try {
            FileUtils.copyDirectory(sourceWorldFolder.toPath().toString(), activeWorldFolder.toPath().toString());
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to copy source to world folder " + activeWorldFolder.getName());
            e.printStackTrace();
            return false;
        }

        world = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()).generator(new MapWorldGenerator()));
        if (world != null) world.setAutoSave(false);
        return isLoaded();
    }

    @Override
    public void unload() {
        if (world != null) Bukkit.unloadWorld(world, false);
        if (activeWorldFolder != null) {
            try {
                FileUtils.deleteDirectory(activeWorldFolder.toPath());
            } catch (IOException e) {
                Bukkit.getLogger().severe("Failed to delete " + activeWorldFolder.getName());
                e.printStackTrace();
            }
        }

        world = null;
        activeWorldFolder = null;
    }

    @Override
    public boolean restoreFromSource() {
        unload();
        return load();
    }

    @Override
    public boolean isLoaded() {
        return getWorld() != null;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public World getSourceWorld() {
        return this.sourceWorld;
    }

    @Override
    public boolean isNight() {
        return this.night;
    }

    @Override
    public void setNight(boolean isNight) {
        if (!isNight) {
            world.setTime(0);
        } else {
            if (currentWarp.isNight()) {
                world.setTime(13000);
            }
        }
    }

    @Override
    public Warp currentWarp() {
        return this.currentWarp;
    }

    @Override
    public boolean setWarp(Warp warp) {
        this.currentWarp = warp;
        return true;
    }

    private static class FileUtils {
        public static void copyDirectory(String sourceDirectory, String destinationDirectory) throws IOException {
            try (Stream<Path> walk = Files.walk(Paths.get(sourceDirectory))) {
                walk.forEach(source -> {
                    Path destination = Paths.get(destinationDirectory, source.toString()
                            .substring(sourceDirectory.length()));

                    try {
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }

        public static void deleteDirectory(Path root) throws IOException {
            try (Stream<Path> walk = Files.walk(root)) {
                walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }
}
