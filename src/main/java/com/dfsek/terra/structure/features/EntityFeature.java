package com.dfsek.terra.structure.features;

import com.dfsek.terra.Debug;
import com.dfsek.terra.structure.Structure;
import com.dfsek.terra.structure.StructureInfo;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.polydev.gaea.math.MathUtil;
import org.polydev.gaea.math.Range;

import java.util.Random;
import java.util.Set;

public class EntityFeature implements Feature {
    private final EntityType type;
    private final Range amount;
    private final int attempts;
    private final Set<Material> in;
    private final Set<Material> stand;
    private final int inSize;

    public EntityFeature(EntityType type, Range amount, int attempts, Set<Material> stand, Set<Material> in, int inSize) {
        this.type = type;
        this.amount = amount;
        this.attempts = attempts;
        this.in = in;
        this.stand = stand;
        this.inSize = inSize;
    }

    private static boolean isInChunk(Chunk c, Location l) {
        return Math.floorDiv(l.getBlockX(), 16) == c.getX() && Math.floorDiv(l.getBlockZ(), 16) == c.getZ();
    }

    @Override
    public void apply(Structure structure, Location l, Chunk chunk) {
        Random random = new Random(MathUtil.getCarverChunkSeed(chunk.getX(), chunk.getZ(), chunk.getWorld().getSeed()));

        int amountSpawn = amount.get(random);

        StructureInfo info = structure.getStructureInfo();
        Range x = new Range(0, info.getSizeZ());
        Range y = new Range(0, info.getSizeY());
        Range z = new Range(0, info.getSizeZ());

        int cx = info.getCenterX();
        int cz = info.getCenterZ();

        for(int i = 0; i < amountSpawn && i < attempts; i++) {
            int yv = y.get(random);
            Location attempt = l.clone().add(x.get(random) - cx, yv, z.get(random) - cz);
            if(! isInChunk(chunk, attempt)) continue; // Don't attempt spawn if not in current chunk.

            boolean canSpawn = false;
            while(yv >= 0 && attempt.getBlockY() >= l.getBlockY()) { // Go down, see if valid spawns exist.
                canSpawn = true;
                Block on = attempt.getBlock();
                attempt.subtract(0, 1, 0);
                yv--;

                if(! stand.contains(on.getType())) continue;

                for(int j = 1; j < inSize + 1; j++)
                    if(! in.contains(on.getRelative(BlockFace.UP, j).getType())) canSpawn = false;

                if(canSpawn) break;
            }
            if(canSpawn) {
                Debug.info("Spawning entity at  " + attempt);
                chunk.getWorld().spawnEntity(attempt.add(0.5, 2, 0.5), type); // Add 0.5 to X & Z so entity spawns in center of block.
            }
        }
    }

    @Override
    public void apply(Structure structure, Location l, Random random) {
        int amountSpawn = amount.get(random);

        StructureInfo info = structure.getStructureInfo();
        Range x = new Range(0, info.getSizeZ());
        Range y = new Range(0, info.getSizeY());
        Range z = new Range(0, info.getSizeZ());

        int cx = info.getCenterX();
        int cz = info.getCenterZ();

        for(int i = 0; i < amountSpawn && i < attempts; i++) {
            int yv = y.get(random);
            Location attempt = l.clone().add(x.get(random) - cx, yv, z.get(random) - cz);

            boolean canSpawn = false;
            while(yv >= 0 && attempt.getBlockY() >= l.getBlockY()) { // Go down, see if valid spawns exist.
                canSpawn = true;
                Block on = attempt.getBlock();
                attempt.subtract(0, 1, 0);
                yv--;

                if(! stand.contains(on.getType())) continue;

                for(int j = 1; j < inSize + 1; j++)
                    if(! in.contains(on.getRelative(BlockFace.UP, j).getType())) canSpawn = false;

                if(canSpawn) break;
            }
            if(canSpawn) {
                Debug.info("Spawning entity at  " + attempt);
                l.getWorld().spawnEntity(attempt.add(0.5, 1, 0.5), type); // Add 0.5 to X & Z so entity spawns in center of block.
            }
        }
    }

    public static class SpawnRule {

    }
}
