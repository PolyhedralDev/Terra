package com.dfsek.terra.cli.world;

import com.dfsek.terra.cli.world.chunk.CLIChunk;


public class Region {
    private final CLIChunk[] chunks;
    
    public Region(CLIWorld world) {
        CLIChunk[] chunks = new CLIChunk[32 * 32];
        for(int x = 0; x < 32; x++) {
            for(int z = 0; z < 32; z++) {
                chunks[x * z * 32] = new CLIChunk(x, z, world);
            }
        }
        this.chunks = chunks;
    }
    
    public CLIChunk get(int x, int z) {
        return chunks[x + z*32];
    }
}
