package com.dfsek.terra.fabric.world.features;

import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.fabric.world.generator.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.world.handles.FabricWorld;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

/**
 * Feature wrapper for Terra populator
 */
public class PopulatorFeature extends Feature<DefaultFeatureConfig> {
    public PopulatorFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        FabricChunkGeneratorWrapper gen = (FabricChunkGeneratorWrapper) chunkGenerator;
        FabricWorld world1 = new FabricWorld(world.toServerWorld(), (com.dfsek.terra.api.platform.world.generator.ChunkGenerator) chunkGenerator);
        gen.getHandle().getPopulators().forEach(populator -> populator.populate(world1, (Chunk) world));
        return true;
    }
}
