package net.hectus.hectusblockbattles.structures.v2;

import net.hectus.hectusblockbattles.util.Cord;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.PointedDripstone;
import org.bukkit.block.data.type.Slab;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

public class Structure {
    public final String name; // TheStructure's name
    public final HashMap<Material, Integer> materials; // For Stage 1 filtering the StructureManager
    public final HashSet<BlockData> blockData; // Data of all blocks that are in the structure and where they are
    public final boolean useExactBlockData;

    public Structure(String name, boolean exactBlockData) {
        this.name = name;
        materials = new HashMap<>();
        blockData = new HashSet<>();
        useExactBlockData = exactBlockData;
    }

    public record BlockData(Material material, int x, int y, int z, BlockFace direction, BlockBound blockBound, boolean open) {}
    public enum BlockBound {NONE, TOP, BOTTOM, DOUBLE_SLAB, STALACTITE, STALAGMITE}

    public static @NotNull Structure save(@NotNull Cord c1, @NotNull Cord c2, String name, boolean useExactBlockData, World world) {
        int hX, lX, hY, lY, hZ, lZ;

        hX = Math.max(c1.x(), c2.x());
        lX = Math.min(c1.x(), c2.x());
        hY = Math.max(c1.y(), c2.y());
        lY = Math.min(c1.y(), c2.y());
        hZ = Math.max(c1.z(), c2.z());
        lZ = Math.min(c1.z(), c2.z());

        Structure structure = new Structure(name, useExactBlockData);

        Cord startCord = new Cord(lX, lY, lZ);

        for (int x = lX; x <= hX; x++) {
            for (int y = lY; y <= hY; y++) {
                for (int z = lZ; z <= hZ; z++) {
                    org.bukkit.block.Block block = new Location(world, x, y, z).getBlock();
                    if (!block.getType().isAir()) {
                        BlockFace direction = blockFace(block);
                        BlockBound bound = blockBound(block);
                        boolean open = isOpen(block);

                        structure.blockData.add(new BlockData(block.getType(), x - startCord.x(), y - startCord.y(), z - startCord.z(), direction, bound, open));

                        if (structure.materials.containsKey(block.getType())) {
                            structure.materials.put(block.getType(), structure.materials.get(block.getType()) + 1);
                        } else {
                            structure.materials.put(block.getType(), 1);
                        }
                    }
                }
            }
        }

        return structure;
    }

    public static BlockFace blockFace(@NotNull Block block) {
        org.bukkit.block.data.BlockData blockData = block.getBlockData();

        if (blockData instanceof Directional) {
            return ((Directional) blockData).getFacing();
        } else if (blockData instanceof Rotatable) {
            return ((Rotatable) blockData).getRotation();
        }

        return BlockFace.SELF;
    }

    public static BlockBound blockBound(@NotNull Block block) {
        org.bukkit.block.data.BlockData blockData = block.getBlockData();

        if (blockData instanceof Slab slab) {
            if (slab.getType() == Slab.Type.TOP) return BlockBound.TOP;
            else if (slab.getType() == Slab.Type.BOTTOM) return BlockBound.BOTTOM;
            else return BlockBound.DOUBLE_SLAB;
        } else if (blockData instanceof PointedDripstone dripstone) {
            if (dripstone.getVerticalDirection() == BlockFace.UP) return BlockBound.STALAGMITE;
            else return BlockBound.STALACTITE;
        }

        return BlockBound.NONE;
    }

    public static boolean isOpen(@NotNull Block block) {
        org.bukkit.block.data.BlockData blockData = block.getBlockData();

        if (blockData instanceof Openable data) {
            return data.isOpen();
        }

        return false;
    }
}
