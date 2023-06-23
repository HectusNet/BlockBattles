package net.hectus.hectusblockbattles.structures.v2;

import net.hectus.color.McColor;
import net.hectus.hectusblockbattles.HBB;
import net.hectus.hectusblockbattles.events.BlockBattleEvents;
import net.hectus.hectusblockbattles.events.StructurePlaceEvent;
import net.hectus.hectusblockbattles.match.Match;
import net.hectus.hectusblockbattles.util.Cord;
import net.hectus.storing.pair.Pair;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Algorithm {
    public boolean running = false; // If the algorithm is already running
    public AlgorithmTimer timer = new AlgorithmTimer(this); // The timer for block placement detection
    public Player placer; // The player who's currently placing blocks
    private final HashSet<Structure.BlockData> placed = new HashSet<>(); // All placed blocks
    private final HashMap<Structure, Double> possible = new HashMap<>(); // Structures and their chances
    private Cord relative; // The 0 0 0 relative, used to compare with the structure relatives

    public void start(Player player) {
        HBB.LOGGER.info("The algorithm was started!");

        running = true;
        placer = player;
        timer.instance(this);

        StructureManager.loadedStructures.forEach(struct -> possible.put(struct, 0.0));
    }

    public void addBlock(Structure.BlockData blockData) {
        if (placed.isEmpty()) {
            relative = new Cord(blockData.x(), blockData.y(), blockData.z());
        } else {
            if (relative.x() > blockData.x()) relative = new Cord(blockData.x(), relative.y(), relative.z());
            if (relative.y() > blockData.y()) relative = new Cord(relative.x(), blockData.y(), relative.z());
            if (relative.z() > blockData.z()) relative = new Cord(relative.x(), relative.y(), blockData.z());
        }
        placed.add(blockData);

        timer.lap(this);
    }

    public void calculateChances() {
        Player opponent;
        if (isPlacer(Match.p1.player))
            opponent = Match.p2.player;
        else opponent = Match.p1.player;

        for (Structure structure : new HashSet<>(possible.keySet())) {

            HashMap<Material, Integer> materials = new HashMap<>();
            for (Structure.BlockData structureData : structure.blockData) {
                Material mat = structureData.material();

                int oldCount = 0;
                if (materials.containsKey(mat)) oldCount = materials.get(mat);

                materials.put(mat, oldCount + 1);
            }

            int matchingBlocks = 0;
            int misplacedBlocks = 0;

            int misplacedMaterials = 0;

            for (Structure.BlockData data : placed) {
                Material mat = data.material();

                int rx = data.x() - relative.x();
                int ry = data.y() - relative.y();
                int rz = data.z() - relative.z();

                Structure.BlockData tmpData = new Structure.BlockData(mat, rx, ry, rz, data.direction(), data.blockBound(), data.open());

                if (structure.blockData.contains(tmpData)) {
                    matchingBlocks++;
                } else {
                    misplacedBlocks++;
                }

                if (materials.containsKey(mat)) {
                    int count = materials.get(mat);

                    if (count <= 1) {
                        materials.remove(mat);
                    } else {
                        materials.put(mat, count - 1);
                    }
                } else {
                    misplacedMaterials--;
                }
            }

            double noMisChance = (double) matchingBlocks / structure.blockData.size();

            if (noMisChance >= 1.0) {
                double chance = (double) (matchingBlocks - misplacedBlocks) / structure.blockData.size();
                possible.put(structure, chance);

                if (chance >= 1.0) {
                    BlockBattleEvents.onStructurePlace(new StructurePlaceEvent(structure, relative, placer, opponent, possible));
                } else {
                    placer.showTitle(BlockBattleEvents.subtitle(McColor.RED + "Structure was misplaced!"));
                    opponent.sendActionBar(Component.text(placer.getName() + " misplaced his structure!"));
                }
                timer.stop();
                clear();
                start(opponent);
            } else {
                if (misplacedMaterials > 0) {
                    placer.showTitle(BlockBattleEvents.subtitle(McColor.RED + "Misplace!"));
                    opponent.sendActionBar(Component.text(placer.getName() + " misplaced!"));
                }
            }
        }
    }

    public void clear() {
        running = false;
        placed.clear();
        possible.clear();
        placer = null;
        relative = null;
    }

    public boolean isPlacer(@NotNull Player player) {
        if (running) {
            return player.getName().equals(placer.getName());
        } else {
            return false;
        }
    }

    public Pair<Structure, Double> getHighestChance() {
        Pair<Structure, Double> pair = new Pair<>();
        for (Map.Entry<Structure, Double> entry : possible.entrySet()) {
            if (pair.isEmpty()) {
                pair.set(entry.getValue(), entry.getKey());
            } else if (entry.getValue() > pair.right()) {
                pair.set(entry.getValue(), entry.getKey());
            }
        }
        return pair;
    }
}
