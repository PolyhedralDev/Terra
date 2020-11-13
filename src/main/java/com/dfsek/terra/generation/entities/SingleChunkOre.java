package com.dfsek.terra.generation.entities;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.FastNoiseLite;

import java.util.Random;
import java.util.Set;

public class SingleChunkOre extends Ore {
    protected final int min;
    protected final int max;

    public SingleChunkOre(BlockData oreData, int min, int max, double deform, double deformFrequency, String id,
                          boolean update, int chunkEdgeOffset, Set<Material> replaceable) {
        super(oreData, deform, deformFrequency, id, update, chunkEdgeOffset, replaceable);
        this.min = min;
        this.max = max;
    }

    @Override
    public void generate(Location location, Random random, JavaPlugin plugin) {
        FastNoiseLite noise = new FastNoiseLite(random.nextInt());
        noise.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.setFrequency(deformFrequency);

        Chunk chunk = location.getChunk();

        int rad = randomInRange(random, min, max);
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector oreLoc = location.toVector().clone().add(new Vector(x, y, z));
                    if(oreLoc.getBlockX() > 15 || oreLoc.getBlockZ() > 15 || oreLoc.getBlockY() > 255 || oreLoc.getBlockX() < 0 || oreLoc.getBlockZ() < 0 || oreLoc.getBlockY() < 0)
                        continue;
                    if(oreLoc.distance(location.toVector()) < (rad + 0.5) * ((noise.getNoise(x, y, z) + 1) * deform)) {
                        Block b = chunk.getBlock(oreLoc.getBlockX(), oreLoc.getBlockY(), oreLoc.getBlockZ());
                        if(replaceable.contains(b.getType()) && b.getLocation().getY() >= 0)
                            b.setBlockData(oreData, update);
                    }
                }
            }
        }
    }
}
