package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.population.ChunkCoordinate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class OreConfig extends TerraConfig {
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

    public OreConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        super(file, config);
        if(!yaml.contains("id")) throw new ConfigException("Ore ID not found!", "null");
        this.id = yaml.getString("id");
        if(!yaml.contains("material")) throw new ConfigException("Ore material not found!", getID());
        if(!yaml.contains("deform")) throw new ConfigException("Ore vein deformation not found!", getID());
        if(!yaml.contains("replace")) throw new ConfigException("Ore replaceable materials not found!", getID());
        min = yaml.getInt("radius.min", 1);
        max = yaml.getInt("radius.max", 1);
        deform = yaml.getDouble("deform", 0.75);
        deformFrequency = yaml.getDouble("deform-frequency", 0.1);
        update = yaml.getBoolean("update", false);
        crossChunks = yaml.getBoolean("cross-chunks", true);
        chunkEdgeOffset = yaml.getInt("edge-offset", 1);

        if(chunkEdgeOffset > 7 || chunkEdgeOffset < 0)
            throw new ConfigException("Edge offset is too high/low!", getID());

        replaceable = ConfigUtil.toBlockData(yaml.getStringList("replace"), "replaceable", getID());

        try {
            oreData = Bukkit.createBlockData(Objects.requireNonNull(yaml.getString("material")));
        } catch(NullPointerException | IllegalArgumentException e) {
            throw new ConfigException("Invalid ore material: " + yaml.getString("material"), getID());
        }
    }

    public String getID() {
        return id;
    }

    public void doVein(Vector l, Chunk chunk, Random r) {
        FastNoiseLite ore = new FastNoiseLite(r.nextInt());
        ore.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        ore.setFrequency(deformFrequency);
        int rad = randomInRange(r);
        Map<ChunkCoordinate, Chunk> chunks = new HashMap<>();  // Cache chunks to prevent re-loading chunks every time one is needed.
        chunks.put(new ChunkCoordinate(chunk), chunk);
        Vector orig = new Vector(l.getBlockX() + (chunk.getX() << 4), l.getBlockY(), l.getBlockZ() + (chunk.getZ() << 4));
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector oreLoc = orig.clone().add(new Vector(x, y, z));
                    Vector source = l.clone().add(new Vector(x, y, z));
                    if(oreLoc.getBlockY() > 255 || oreLoc.getBlockY() < 0) continue;
                    if(source.distance(l) < (rad + 0.5) * ((ore.getNoise(x, y, z) + 1) * deform)) {
                        ChunkCoordinate coord = new ChunkCoordinate(Math.floorDiv(oreLoc.getBlockX(), 16), Math.floorDiv(oreLoc.getBlockZ(), 16), chunk.getWorld().getUID());
                        Block b = chunks.computeIfAbsent(coord, k -> chunk.getWorld().getChunkAt(oreLoc.toLocation(chunk.getWorld())))
                                .getBlock(Math.floorMod(source.getBlockX(), 16), source.getBlockY(), Math.floorMod(source.getBlockZ(), 16)); // Chunk caching conditional computation
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

    public void doVeinSingle(Vector l, Chunk chunk, Random r) {
        FastNoiseLite ore = new FastNoiseLite(r.nextInt());
        ore.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        ore.setFrequency(deformFrequency);
        int rad = randomInRange(r);
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    Vector oreLoc = l.clone().add(new Vector(x, y, z));
                    if(oreLoc.getBlockX() > 15 || oreLoc.getBlockZ() > 15 || oreLoc.getBlockY() > 255 || oreLoc.getBlockX() < 0 || oreLoc.getBlockZ() < 0 || oreLoc.getBlockY() < 0)
                        continue;
                    if(oreLoc.distance(l) < (rad + 0.5) * ((ore.getNoise(x, y, z) + 1) * deform)) {
                        Block b = chunk.getBlock(oreLoc.getBlockX(), oreLoc.getBlockY(), oreLoc.getBlockZ());
                        if(replaceable.contains(b.getType()) && b.getLocation().getY() >= 0)
                            b.setBlockData(oreData, update);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Ore with ID " + getID();
    }

    public boolean crossChunks() {
        return crossChunks;
    }

    public int getChunkEdgeOffset() {
        return chunkEdgeOffset;
    }
}
