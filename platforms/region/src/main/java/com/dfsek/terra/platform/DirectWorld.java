package com.dfsek.terra.platform;

import com.dfsek.terra.DirectUtils;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import net.jafama.FastMath;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DirectWorld implements World {
    private final long seed;
    private final GenWrapper generator;
    private final Map<Long, MCAFile> files = Collections.synchronizedMap(new HashMap<>());

    public DirectWorld(long seed, GenWrapper generator) {
        this.seed = seed;
        this.generator = generator;
    }

    @Override
    public long getSeed() {
        return seed;
    }

    @Override
    public int getMaxHeight() {
        return 255;
    }

    @Override
    public ChunkGenerator getGenerator() {
        return generator;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public UUID getUID() {
        return null;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        return false;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        MCAFile file = compute(x, z);
        net.querz.mca.Chunk chunk = file.getChunk(x, z);
        if(chunk == null) {
            chunk = net.querz.mca.Chunk.newChunk();
            file.setChunk(x, z, chunk);
        }
        return new DirectChunkData(chunk, this, x, z);
    }

    @Override
    public File getWorldFolder() {
        return null;
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        return new DirectBlock(this, new Vector3(x, y, z));
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        return null;
    }

    @Override
    public int getMinHeight() {
        return 0;
    }

    @Override
    public Object getHandle() {
        return generator;
    }

    public MCAFile compute(int x, int z) {
        synchronized(files) {
            return files.computeIfAbsent(DirectUtils.regionID(x, z), k -> {
                File test = new File("region", MCAUtil.createNameFromChunkLocation(x, z));
                if(test.exists()) {
                    try {
                        System.out.println("Re-loading " + MCAUtil.createNameFromChunkLocation(x, z));
                        return MCAUtil.read(test);
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
                return new MCAFile(MCAUtil.chunkToRegion(x), MCAUtil.chunkToRegion(z));
            });
        }
    }

    public CompoundTag getData(int x, int y, int z) {
        return compute(FastMath.floorDiv(x, 16), FastMath.floorDiv(z, 16)).getBlockStateAt(x, y, z);
    }

    public Map<Long, MCAFile> getFiles() {
        return files;
    }
}
