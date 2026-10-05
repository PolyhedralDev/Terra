package com.dfsek.terra.bukkit.nms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.bukkit.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;


public final class NMSStructureBeardifier {
    private NMSStructureBeardifier() { }

    public static void apply(ChunkGenerator delegate, ChunkAccess chunk, WorldProperties world,
                             BiomeProvider biomeProvider, PreLoadCompatibilityOptions compatibilityOptions,
                             StructureDensitySampler sampler) {
        double threshold = compatibilityOptions.getBeardThreshold();
        double airThreshold = compatibilityOptions.getAirThreshold();
        int chunkX = chunk.getPos().x() << 4;
        int chunkZ = chunk.getPos().z() << 4;

        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                int depth = 0;
                for(int y = world.getMaxHeight() - 1; y >= world.getMinHeight(); y--) {
                    int worldX = x + chunkX;
                    int worldZ = z + chunkZ;
                    double density = sampler.sample(worldX, y, worldZ);
                    if(density > threshold) {
                        chunk.setBlockState(new BlockPos(x, y, z), ((CraftBlockData) ((BukkitBlockState) delegate
                            .getPalette(worldX, y, worldZ, world, biomeProvider)
                            .get(depth, worldX, y, worldZ, world.getSeed())).getHandle()).getState(), 0);
                        depth++;
                    } else if(density < airThreshold) {
                        chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 0);
                    } else {
                        depth = 0;
                    }
                }
            }
        }
    }

    @FunctionalInterface
    public interface StructureDensitySampler {
        double sample(int x, int y, int z);
    }
}
