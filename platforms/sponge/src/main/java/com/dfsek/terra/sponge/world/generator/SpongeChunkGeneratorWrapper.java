package com.dfsek.terra.sponge.world.generator;

import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import org.spongepowered.api.world.biome.provider.BiomeProvider;
import org.spongepowered.api.world.generation.ChunkGenerator;
import org.spongepowered.api.world.generation.config.structure.StructureGenerationConfig;

public class SpongeChunkGeneratorWrapper implements ChunkGenerator, GeneratorWrapper {

    @Override
    public TerraChunkGenerator getHandle() {
        return null;
    }

    @Override
    public BiomeProvider biomeProvider() {
        return null;
    }

    @Override
    public StructureGenerationConfig structureConfig() {
        return null;
    }
}
