package com.dfsek.terra.cli.world;

import com.dfsek.terra.cli.NBTSerializable;
import com.dfsek.terra.cli.world.chunk.CLIChunk;

import net.querz.mca.MCAFile;
import net.querz.mca.MCAUtil;


public class Region implements NBTSerializable<MCAFile> {
    private final CLIChunk[] chunks;
    private final int x, z;
    
    public Region(CLIWorld world, int x, int z) {
        this.x = x;
        this.z = z;
        CLIChunk[] chunks = new CLIChunk[32 * 32];
        for(int cx = 0; cx < 32; cx++) {
            for(int cz = 0; cz < 32; cz++) {
                chunks[cx + cz * 32] = new CLIChunk(cx, cz, world);
            }
        }
        this.chunks = chunks;
    }
    
    public CLIChunk get(int x, int z) {
        return chunks[x + z*32];
    }
    
    @Override
    public MCAFile serialize() {
        MCAFile mcaFile = new MCAFile(x, z);
        for(int cx = 0; cx < 32; cx++) {
            for(int cz = 0; cz < 32; cz++) {
                mcaFile.setChunk(cx + cz * 32, chunks[cx + cz * 32].serialize());
            }
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
