package com.dfsek.terra.platform;

import com.dfsek.terra.DirectUtils;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;

import net.jafama.FastMath;
import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class DirectWorld implements World {
    private final long seed;
    private final GenWrapper generator;
    private final Map<Long, MCAFile> files = Collections.synchronizedMap(new HashMap<>());
    
    public DirectWorld(long seed) {
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
    public BlockState getBlockData(int x, int y, int z) {
        return null;
    }
    
    @Override
    public void setBlockData(int x, int y, int z, BlockState data, boolean physics) {

    }
    
    @Override
    public BlockEntity getBlockState(int x, int y, int z) {
        return null;
    }
    
    @Override
    public Entity spawnEntity(Vector3 location, EntityType entityType) {
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
