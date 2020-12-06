package com.dfsek.terra.async;

import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import com.dfsek.terra.config.base.PluginConfig;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;

import java.util.function.Consumer;

/**
 * Runnable that locates a biome asynchronously
 */
public class AsyncBiomeFinder extends AsyncFeatureFinder<Biome> {

    public AsyncBiomeFinder(TerraBiomeGrid grid, Biome target, @NotNull Location origin, int startRadius, int maxRadius, Consumer<Vector> callback, GaeaPlugin main) {
        super(grid, target, origin, startRadius, maxRadius, callback, main);
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
        int res = PluginConfig.getBiomeSearchResolution();
        return getGrid().getBiome(x * res, z * res, GenerationPhase.POST_GEN).equals(target);
    }

    @Override
    public Vector finalizeVector(Vector orig) {
        return orig.multiply(PluginConfig.getBiomeSearchResolution());
    }
}
