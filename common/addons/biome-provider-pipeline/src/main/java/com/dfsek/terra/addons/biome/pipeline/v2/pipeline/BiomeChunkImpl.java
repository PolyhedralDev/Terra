package com.dfsek.terra.addons.biome.pipeline.v2.pipeline;

import java.util.List;

import com.dfsek.terra.addons.biome.pipeline.v2.api.BiomeChunk;
import com.dfsek.terra.addons.biome.pipeline.v2.api.Expander;
import com.dfsek.terra.addons.biome.pipeline.v2.api.SeededVector;
import com.dfsek.terra.addons.biome.pipeline.v2.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;


public class BiomeChunkImpl implements BiomeChunk {

    private final SeededVector worldOrigin;
    private final int chunkOriginArrayIndex;
    private final int worldCoordinateScale;
    private final int size;
    private PipelineBiome[] biomes;

    public BiomeChunkImpl(SeededVector worldOrigin, PipelineImpl pipeline) {

        this.worldOrigin = worldOrigin;
        this.chunkOriginArrayIndex = pipeline.getChunkOriginArrayIndex();
        this.worldCoordinateScale = pipeline.getResolution();

        this.size = pipeline.getArraySize();

        int expanderCount = pipeline.getExpanderCount();
        int expansionsApplied = 0;

        // Allocate working arrays
        this.biomes = new PipelineBiome[size * size];
        PipelineBiome[] lookupArray = new PipelineBiome[size * size];
        // A second lookup array is required such that stage application doesn't affect lookups, otherwise application may cascade

        // Construct working grid
        int gridOrigin = 0;
        int gridInterval = calculateGridInterval(expanderCount, expansionsApplied);
        int gridSize = (size / gridInterval);
        gridSize += expanderCount > 0 ? 1 : 0; // Add an extra border if expansion occurs

        // Fill working grid with initial cells
        for(int gridX = 0; gridX < gridSize; gridX++) {
            for(int gridZ = 0; gridZ < gridSize; gridZ++) {
                int xIndex = gridOrigin + gridX * gridInterval;
                int zIndex = gridOrigin + gridZ * gridInterval;
                biomes[(xIndex * size) + zIndex] = pipeline.getSource().get(worldOrigin.seed(), xIndexToWorldCoordinate(xIndex),
                    zIndexToWorldCoordinate(zIndex));
            }
        }

        for(Stage stage : pipeline.getStages()) {
            if(stage instanceof Expander) {
                // Shrink working grid size, the expander will fill in null cells (as a result of shrinking the grid) during mutation
                expansionsApplied++;
                gridInterval = calculateGridInterval(expanderCount, expansionsApplied);
                gridSize = expandSize(gridSize);
            }

            int stageReadDistance = stage.maxRelativeReadDistance();
            if(stageReadDistance > 0) {
                // Discard edges such that adjacent lookups are only ran on valid cells
                gridSize = contractBordersFromSize(gridSize, stageReadDistance);
                gridOrigin += stageReadDistance * gridInterval;
            }

            // Cycle arrays, the previously populated array is swapped to be used for lookups, and the result of the stage application
            // overwrites the previous lookup array. This saves having to allocate a new array copy each time
            PipelineBiome[] tempArray = biomes;
            biomes = lookupArray;
            lookupArray = tempArray;

            // Apply stage to working grid
            for(int gridZ = 0; gridZ < gridSize; gridZ = gridZ + 1) {
                for(int gridX = 0; gridX < gridSize; gridX = gridX + 1) {
                    int xIndex = gridOrigin + gridX * gridInterval;
                    int zIndex = gridOrigin + gridZ * gridInterval;
                    biomes[(xIndex * size) + zIndex] = stage.apply(
                        new ViewPoint(this, gridInterval, gridX, gridZ, xIndex, zIndex, lookupArray, size));
                }
            }
        }
    }

    protected static int initialSizeToArraySize(int expanderCount, int initialSize) {
        int size = initialSize;
        for(int i = 0; i < expanderCount; i++) {
            size = expandSize(size);
        }
        return size;
    }

    protected static int calculateChunkOriginArrayIndex(int totalExpanderCount, List<Stage> stages) {
        int finalGridOrigin = calculateFinalGridOrigin(totalExpanderCount, stages);
        int initialGridInterval = calculateGridInterval(totalExpanderCount, 0);

        // Round the final grid origin up to the nearest multiple of initialGridInterval, such that each
        // chunk samples points on the same overall grid.
        // Without this, shared chunk borders (required because of adjacent cell reads) will not be identical
        // because points would be sampled on grids at different offsets, resulting in artifacts at borders.
        return (int) Math.ceil((double) finalGridOrigin / initialGridInterval) * initialGridInterval;
    }

    private static int calculateFinalGridOrigin(int totalExpanderCount, List<Stage> stages) {
        int gridOrigin = 0;
        int expansionsApplied = 0;
        int gridInterval = calculateGridInterval(totalExpanderCount, expansionsApplied);
        for(Stage stage : stages) {
            if(stage instanceof Expander) {
                expansionsApplied++;
                gridInterval = calculateGridInterval(totalExpanderCount, expansionsApplied);
            }
            gridOrigin += stage.maxRelativeReadDistance() * gridInterval;
        }
        return gridOrigin;
    }

    protected static int calculateChunkSize(int arraySize, int chunkOriginArrayIndex, int totalExpanderCount) {
        return contractBordersFromSize(arraySize, chunkOriginArrayIndex) - (totalExpanderCount > 0 ? 1 : 0);
    }

    private static int expandSize(int size) {
        return size * 2 - 1;
    }

    private static int contractBordersFromSize(int size, int border) {
        return size - border * 2;
    }

    private static int calculateGridInterval(int totalExpansions, int expansionsApplied) {
        return 1 << (totalExpansions - expansionsApplied);
    }

    @Override
    public PipelineBiome get(int xInChunk, int zInChunk) {
        int xIndex = xInChunk + chunkOriginArrayIndex;
        int zIndex = zInChunk + chunkOriginArrayIndex;
        return biomes[(xIndex * size) + zIndex];
    }

    private int xIndexToWorldCoordinate(int xIndex) {
        return (worldOrigin.x() + xIndex - chunkOriginArrayIndex) * worldCoordinateScale;
    }

    private int zIndexToWorldCoordinate(int zIndex) {
        return (worldOrigin.z() + zIndex - chunkOriginArrayIndex) * worldCoordinateScale;
    }

    private SeededVector getOrigin() {
        return worldOrigin;
    }

    /**
     * Represents a point on the operating grid within the biomes array
     */
    public static class ViewPoint {
        private final BiomeChunkImpl chunk;
        private final PipelineBiome biome;
        private final int gridInterval;
        private final int gridX;
        private final int gridZ;
        private final int xIndex;
        private final int zIndex;
        private final PipelineBiome[] lookupArray;
        private final int size;

        private ViewPoint(BiomeChunkImpl chunk, int gridInterval, int gridX, int gridZ, int xIndex, int zIndex,
                          PipelineBiome[] lookupArray, int size) {
            this.chunk = chunk;
            this.gridInterval = gridInterval;
            this.gridX = gridX;
            this.gridZ = gridZ;
            this.xIndex = xIndex;
            this.zIndex = zIndex;
            this.lookupArray = lookupArray;
            this.size = size;
            this.biome = lookupArray[(this.xIndex * this.size) + this.zIndex];
        }

        public PipelineBiome getRelativeBiome(int x, int z) {
            int lookupXIndex = this.xIndex + x * gridInterval;
            int lookupZIndex = this.zIndex + z * gridInterval;
            return lookupArray[(lookupXIndex * this.size) + lookupZIndex];
        }

        public PipelineBiome getBiome() {
            return biome;
        }

        /**
         * @return X position of the point relative to the operating grid
         */
        public int gridX() {
            return gridX;
        }

        /**
         * @return Z position of the point relative to the operating grid
         */
        public int gridZ() {
            return gridZ;
        }

        /**
         * @return X position of the point in the world
         */
        public int worldX() {
            return chunk.xIndexToWorldCoordinate(xIndex);
        }

        /**
         * @return Z position of the point in the world
         */
        public int worldZ() {
            return chunk.zIndexToWorldCoordinate(zIndex);
        }

        public long worldSeed() {
            return chunk.getOrigin().seed();
        }
    }
}
