package com.dfsek.terra.forge.world.features;

import com.dfsek.terra.forge.world.generator.ForgeChunkGenerator;
import com.dfsek.terra.forge.world.generator.ForgeChunkGeneratorWrapper;
import com.dfsek.terra.forge.world.handles.ForgeWorld;
import com.dfsek.terra.forge.world.handles.chunk.ForgeChunkWorldAccess;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Feature wrapper for Terra populator
 */
public class PopulatorFeature extends Feature<NoFeatureConfig> {
    public PopulatorFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull ISeedReader world, @NotNull ChunkGenerator generator, @NotNull Random random, BlockPos pos, @NotNull NoFeatureConfig config) {
        ForgeChunkGeneratorWrapper gen = (ForgeChunkGeneratorWrapper) generator;
        ForgeChunkWorldAccess chunk = new ForgeChunkWorldAccess(world, pos.getX() >> 4, pos.getZ() >> 4);
        ForgeWorld world1 = new ForgeWorld(world.getLevel(), new ForgeChunkGenerator(generator));
        gen.getHandle().getPopulators().forEach(populator -> populator.populate(world1, chunk));
        return true;
    }

}
