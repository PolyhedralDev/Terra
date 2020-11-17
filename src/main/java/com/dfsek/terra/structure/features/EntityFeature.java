package com.dfsek.terra.structure.features;

import com.dfsek.terra.Debug;
import com.dfsek.terra.structure.Rotation;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private static List<Location> getLocations(Structure structure, Rotation r, Location origin, Random random, int number) {
        StructureInfo info = structure.getStructureInfo();
        Range x = structure.getRange(Rotation.Axis.X, r);
        Range y = structure.getRange(Rotation.Axis.Y, r);
        Range z = structure.getRange(Rotation.Axis.Z, r);

        int cx = info.getCenterX();
        int cz = info.getCenterZ();
        List<Location> locations = new ArrayList<>();
        for(int i = 0; i < number; i++)
            locations.add(origin.clone().add(x.get(random) - cx, y.get(random), z.get(random) - cz));
        return locations;
    }

    @Override
    public void apply(Structure structure, Rotation r, Location l, Chunk chunk) {
        Random random = new Random(MathUtil.getCarverChunkSeed(chunk.getX(), chunk.getZ(), chunk.getWorld().getSeed()));
        for(Location attempt : getLocations(structure, r, l, random, amount.get(random))) {
            if(!isInChunk(chunk, attempt)) continue; // Don't attempt spawn if not in current chunk.
            attemptSpawn(attempt, l);
        }
    }

    private void attemptSpawn(Location attempt, Location origin) {
        boolean canSpawn = false;
        while(attempt.getBlockY() >= origin.getBlockY()) { // Go down, see if valid spawns exist.
            canSpawn = true;
            Block on = attempt.getBlock();
            attempt.subtract(0, 1, 0);

            if(!stand.contains(on.getType())) {
                canSpawn = false;
                continue;
            }

            for(int j = 1; j < inSize + 1; j++)
                if(!in.contains(on.getRelative(BlockFace.UP, j).getType())) canSpawn = false;

            if(canSpawn) break;
        }
        if(canSpawn) {
            Debug.info("Spawning entity at  " + attempt);
            Objects.requireNonNull(attempt.getWorld()).spawnEntity(attempt.add(0.5, 2, 0.5), type); // Add 0.5 to X & Z so entity spawns in center of block.
        }
    }

    @Override
    public void apply(Structure structure, Rotation r, Location l, Random random) {
        for(Location attempt : getLocations(structure, r, l, random, amount.get(random))) {
            attemptSpawn(attempt, l);
        }
    }
}
