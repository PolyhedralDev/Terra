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

public class FloraFeature extends Feature<DefaultFeatureConfig> {
    public FloraFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        ((FabricChunkGeneratorWrapper) chunkGenerator).getFloraPopulator().populate(new FabricWorld(world.toServerWorld(), new FabricChunkGenerator(chunkGenerator)),
                random, new FabricChunkWorldAccess(world, pos.getX() >> 4, pos.getZ() >> 4));
        return true;
    }
}
