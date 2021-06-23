package com.dfsek.terra.forge.generation;

import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
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
    public boolean place(@NotNull ISeedReader world, @NotNull ChunkGenerator generator, @NotNull Random random, @NotNull BlockPos pos, @NotNull NoFeatureConfig config) {
        ForgeChunkGeneratorWrapper gen = (ForgeChunkGeneratorWrapper) generator;
        gen.getHandle().getPopulators().forEach(populator -> populator.populate((World) world, (Chunk) world));
        return true;
    }

}
