package com.dfsek.terra.async;

import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;

import java.util.function.Consumer;

/**
 * Runnable that locates a biome asynchronously
 */
public class AsyncBiomeFinder extends AsyncFeatureFinder<Biome> {

    public AsyncBiomeFinder(TerraBiomeGrid grid, Biome target, @NotNull Location origin, int startRadius, int maxRadius, Consumer<Vector> callback) {
        super(grid, target, origin, startRadius, maxRadius, callback);
    }

    /**
     * Helper method to get biome at location
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Biome at coordinates
     */
    @Override
    public boolean isValid(int x, int z, Biome target) {
        return getGrid().getBiome(x, z, GenerationPhase.POST_GEN).equals(target);
    }

    @Override
    public Vector finalizeVector(Vector orig) {
        return orig;
    }
}
