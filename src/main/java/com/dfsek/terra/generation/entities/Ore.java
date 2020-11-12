package com.dfsek.terra.generation.entities;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.population.ChunkCoordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Ore implements GenerationEntity {
    private final BlockData oreData;
    private final int min;
    private final int max;
    private final double deform;
    private final double deformFrequency;
    private final String id;
    private final boolean update;
    private final boolean crossChunks;
    private final int chunkEdgeOffset;
    Set<Material> replaceable;

    public Ore(BlockData oreData, int min, int max, double deform, double deformFrequency, String id, boolean update, boolean crossChunks,
               int chunkEdgeOffset, Set<Material> replaceable) {
        this.oreData = oreData;
        this.min = min;
        this.max = max;
        this.deform = deform;
        this.deformFrequency = deformFrequency;
        this.id = id;
        this.update = update;
        this.crossChunks = crossChunks;
        this.chunkEdgeOffset = chunkEdgeOffset;
        this.replaceable = replaceable;
    }

    @Override
    public void generate(Location location, Random random, JavaPlugin plugin) {
        if(crossChunks)
            doVeinMulti(location, random);
        else
            doVeinSingle(location, random);
    }

    @Override
    public boolean isValidLocation(Location location, JavaPlugin plugin) {
        Block block = location.getBlock();
        return (replaceable.contains(block.getType()) && (block.getLocation().getY() >= 0));
    }

    @SuppressWarnings("DuplicatedCode")
    private void doVeinMulti(Location location, Random random) {
        FastNoiseLite ore = new FastNoiseLite(random.nextInt());
        Chunk chunk = location.getChunk();

        ore.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        ore.setFrequency(deformFrequency);
        int rad = randomInRange(random);
        Map<ChunkCoordinate, Chunk> chunks = new HashMap<>();  // Cache chunks to prevent re-loading chunks every time one is needed.
        chunks.put(new ChunkCoordinate(chunk), chunk);
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector origin = location.toVector();
                    Vector source = origin.clone().add(new Vector(x, y, z));

                    Vector orig = new Vector(location.getBlockX() + (chunk.getX() << 4), location.getBlockY(), location.getBlockZ() + (chunk.getZ() << 4));
                    Vector oreLocation = orig.clone().add(new Vector(x, y, z));

                    if(oreLocation.getBlockY() > 255 || oreLocation.getBlockY() < 0) continue;
                    if(source.distance(origin) < (rad + 0.5) * ((ore.getNoise(x, y, z) + 1) * deform)) {
                        ChunkCoordinate coord = new ChunkCoordinate(Math.floorDiv(oreLocation.getBlockX(), 16), Math.floorDiv(oreLocation.getBlockZ(), 16), chunk.getWorld().getUID());

                        Block block = chunks.computeIfAbsent(coord, k -> chunk.getWorld().getChunkAt(oreLocation.toLocation(chunk.getWorld())))
                                .getBlock(Math.floorMod(source.getBlockX(), 16), source.getBlockY(), Math.floorMod(source.getBlockZ(), 16)); // Chunk caching conditional computation
                        if(replaceable.contains(block.getType()) && block.getLocation().getY() >= 0)
                            block.setBlockData(oreData, update);
                    }
                }
            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void doVeinSingle(Location location, Random random) {
        FastNoiseLite ore = new FastNoiseLite(random.nextInt());
        Chunk chunk = location.getChunk();

        ore.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        ore.setFrequency(deformFrequency);
        int rad = randomInRange(random);
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector oreLoc = location.toVector().clone().add(new Vector(x, y, z));
                    if(oreLoc.getBlockX() > 15 || oreLoc.getBlockZ() > 15 || oreLoc.getBlockY() > 255 || oreLoc.getBlockX() < 0 || oreLoc.getBlockZ() < 0 || oreLoc.getBlockY() < 0)
                        continue;
                    if(oreLoc.distance(location.toVector()) < (rad + 0.5) * ((ore.getNoise(x, y, z) + 1) * deform)) {
                        Block b = chunk.getBlock(oreLoc.getBlockX(), oreLoc.getBlockY(), oreLoc.getBlockZ());
                        if(replaceable.contains(b.getType()) && b.getLocation().getY() >= 0)
                            b.setBlockData(oreData, update);
                    }
                }
            }
        }
    }

    private int randomInRange(Random r) {
        return r.nextInt(max - min + 1) + min;
    }
}
