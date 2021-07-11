package com.dfsek.terra.fabric.generation;

import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.mojang.serialization.Codec;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

/**
 * Feature wrapper for Terra populator
 */
public class PopulatorFeature extends Feature<DefaultFeatureConfig> {
    public PopulatorFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        ChunkGenerator chunkGenerator = context.getGenerator();
        if(!(chunkGenerator instanceof FabricChunkGeneratorWrapper)) return true;
        StructureWorldAccess world = context.getWorld();
        FabricChunkGeneratorWrapper gen = (FabricChunkGeneratorWrapper) chunkGenerator;
        gen.getHandle().getGenerationStages().forEach(populator -> {
            if(!(populator instanceof Chunkified)) {
                populator.populate((World) world, (Chunk) world);
            }
        });
        return true;
    }
}
