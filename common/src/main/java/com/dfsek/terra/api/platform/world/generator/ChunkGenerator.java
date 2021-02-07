package com.dfsek.terra.api.platform.world.generator;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.world.BiomeGrid;
import com.dfsek.terra.api.platform.world.ChunkAccess;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public interface ChunkGenerator extends Handle {
    boolean isParallelCapable();

    boolean shouldGenerateCaves();

    boolean shouldGenerateDecorations();

    boolean shouldGenerateMobs();

    boolean shouldGenerateStructures();

    ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome);


    List<BlockPopulator> getDefaultPopulators(World world);

    @Nullable
    TerraChunkGenerator getTerraGenerator();

    interface ChunkData extends ChunkAccess {
        /**
         * Get the maximum height for the chunk.
         * <p>
         * Setting blocks at or above this height will do nothing.
         *
         * @return the maximum height
         */
        int getMaxHeight();
    }
}
