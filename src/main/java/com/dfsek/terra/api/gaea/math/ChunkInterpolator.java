package com.dfsek.terra.api.gaea.math;

import com.dfsek.terra.api.gaea.biome.BiomeGrid;
import org.bukkit.World;

public interface ChunkInterpolator {
    double getNoise(double x, double z);

    double getNoise(double x, double y, double z);

    enum InterpolationType {
        /**
         * 2D (Bilinear) interpolation
         */
        BILINEAR {
            @Override
            public com.dfsek.terra.api.gaea.math.ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeGrid grid, FastNoiseLite noise) {
                return new ChunkInterpolator2(w, chunkX, chunkZ, grid, noise);
            }
        },
        /**
         * 3D (Trilinear) interpolation
         */
        TRILINEAR {
            @Override
            public com.dfsek.terra.api.gaea.math.ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeGrid grid, FastNoiseLite noise) {
                return new ChunkInterpolator3(w, chunkX, chunkZ, grid, noise);
            }
        };
        public abstract com.dfsek.terra.api.gaea.math.ChunkInterpolator getInstance(World w, int chunkX, int chunkZ, BiomeGrid grid, FastNoiseLite noise);
    }
}
