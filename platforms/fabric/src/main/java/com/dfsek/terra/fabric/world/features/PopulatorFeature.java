package com.dfsek.terra.fabric.world.features;

import com.dfsek.terra.fabric.world.generator.FabricChunkGenerator;
import com.dfsek.terra.fabric.world.generator.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.world.handles.FabricWorld;
import com.dfsek.terra.fabric.world.handles.chunk.FabricChunkWorldAccess;
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
        FabricChunkWorldAccess chunk = new FabricChunkWorldAccess(world, pos.getX() >> 4, pos.getZ() >> 4);
        FabricWorld world1 = new FabricWorld(world.toServerWorld(), new FabricChunkGenerator(chunkGenerator));
        gen.getCavePopulator().populate(world1, random, chunk);
        gen.getOrePopulator().populate(world1, random, chunk);
        gen.getFloraPopulator().populate(world1, random, chunk);
        gen.getTreePopulator().populate(world1, random, chunk);
        return true;
    }
}
