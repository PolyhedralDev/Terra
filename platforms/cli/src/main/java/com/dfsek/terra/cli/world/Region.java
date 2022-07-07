package com.dfsek.terra.cli.world;

import net.querz.mca.MCAFile;

import com.dfsek.terra.cli.NBTSerializable;
import com.dfsek.terra.cli.world.chunk.CLIChunk;


public class Region implements NBTSerializable<MCAFile> {
    private final CLIChunk[] chunks;
    private final int x, z;
    private final CLIWorld world;
    
    public Region(CLIWorld world, int x, int z) {
        this.x = x;
        this.z = z;
        this.world = world;
        this.chunks = new CLIChunk[32 * 32];
    }
    
    public CLIChunk get(int x, int z) {
        int key = x + z * 32;
        CLIChunk chunk = chunks[key];
        if(chunk == null) {
            chunk = new CLIChunk(x, z, world);
            chunks[key] = chunk;
        }
        return chunk;
    }
    
    @Override
    public MCAFile serialize() {
        MCAFile mcaFile = new MCAFile(x, z);
        int count = 0;
        for(int cx = 0; cx < 32; cx++) {
            for(int cz = 0; cz < 32; cz++) {
                CLIChunk chunk = chunks[cx + cz * 32];
                if(chunk != null) {
                    count++;
                    mcaFile.setChunk(cx + cz * 32, chunk.serialize());
                }
            }
        }
        if(count > 0) {
            mcaFile.cleanupPalettesAndBlockStates();
        }
        return mcaFile;
    }
    
    public int getX() {
        return x;
    }
    
    public int getZ() {
        return z;
    }
}
