package com.dfsek.terra.addons.chunkgenerator.layer;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.math.Sampler;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.WritableWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class LayeredChunkGenerator implements ChunkGenerator {
    @Override
    public void generateChunkData(@NotNull ProtoChunk chunk, @NotNull WritableWorld world, int chunkZ, int chunkX) {
    
    }
    
    @Override
    public Sampler createSampler(int chunkX, int chunkZ, BiomeProvider provider, ServerWorld world, int elevationSmooth) {
        return null;
    }
    
    @Override
    public ConfigPack getConfigPack() {
        return null;
    }
    
    @Override
    public List<GenerationStage> getGenerationStages() {
        return null;
    }
    
    @Override
    public BlockState getBlock(ServerWorld world, int x, int y, int z) {
        return null;
    }
}
