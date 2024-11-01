package com.dfsek.terra.addons.generation.structure;

import com.dfsek.terra.addons.generation.structure.impl.StructureLayerGrid;
import com.dfsek.terra.addons.generation.structure.impl.WorldChunk;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;


public class StructureGenerationStage implements GenerationStage {
    private final StructureLayerGrid lastLayer;

    public StructureGenerationStage(StructureLayerGrid lastLayer) {
        this.lastLayer = lastLayer;
    }

    @Override
    public void populate(ProtoWorld world) {
        int chunkX = world.centerChunkX();
        int chunkZ = world.centerChunkZ();
        System.out.printf("Populating X%d Y%d%n", chunkX, chunkZ);
        long seed = world.getSeed();

        Chunk writeChunk = new WorldChunk(world, chunkX, chunkZ);
        Chunk populatedChunk = lastLayer.getChunk(world, chunkX, chunkZ, seed);

        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                for (int y = world.getMinHeight(); y <= world.getMaxHeight(); ++y) {
                    writeChunk.setBlock(x, y, z, populatedChunk.getBlock(x, y, z));
                }
            }
        }
    }
}
