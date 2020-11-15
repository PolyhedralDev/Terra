package com.dfsek.terra.generation.deserelized;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.FastNoiseLite;

import java.util.Random;

public class SingleChunkOre extends Ore {
    protected int min = 0;
    protected int max = 255;

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
                            b.setBlockData(material, update);
                    }
                }
            }
        }
    }
}
