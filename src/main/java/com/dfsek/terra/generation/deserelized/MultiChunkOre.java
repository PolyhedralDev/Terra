package com.dfsek.terra.generation.deserelized;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.population.ChunkCoordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("DefaultAnnotationParam")
@JsonDeserialize(using = JsonDeserializer.None.class)
public class MultiChunkOre extends Ore {
    protected int min;
    protected int max;

    @Override
    public void generate(Location location, Random random, JavaPlugin plugin) {
        FastNoiseLite noise = new FastNoiseLite(random.nextInt());
        noise.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.setFrequency(deformFrequency);

        Chunk chunk = location.getChunk();

        int rad = randomInRange(random, min, max);
        Map<ChunkCoordinate, Chunk> chunks = new HashMap<>();  // Cache chunks to prevent re-loading chunks every time one is needed.
        chunks.put(new ChunkCoordinate(chunk), chunk);
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector origin = location.toVector();
                    Vector source = origin.clone().add(new Vector(x, y, z));

                    Vector orig = new Vector(location.getBlockX() + (chunk.getX() << 4), location.getBlockY(), location.getBlockZ() + (chunk.getZ() << 4));
                    Vector oreLocation = orig.clone().add(new Vector(x, y, z));

                    if(oreLocation.getBlockY() > 255 || oreLocation.getBlockY() < 0)
                        continue;
                    if(source.distance(origin) < (rad + 0.5) * ((noise.getNoise(x, y, z) + 1) * deform)) {
                        ChunkCoordinate coord = new ChunkCoordinate(Math.floorDiv(oreLocation.getBlockX(), 16), Math.floorDiv(oreLocation.getBlockZ(), 16), chunk.getWorld().getUID());

                        Block block = chunks.computeIfAbsent(coord, k -> chunk.getWorld().getChunkAt(oreLocation.toLocation(chunk.getWorld())))
                                .getBlock(Math.floorMod(source.getBlockX(), 16), source.getBlockY(), Math.floorMod(source.getBlockZ(), 16)); // Chunk caching conditional computation
                        if(replaceable.contains(block.getType()) && block.getLocation().getY() >= 0)
                            block.setBlockData(material, update);
                    }
                }
            }
        }
    }
}
