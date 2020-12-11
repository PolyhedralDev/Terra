package com.dfsek.terra.async;

import com.dfsek.terra.api.gaea.biome.Biome;
import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import com.dfsek.terra.api.generic.world.vector.Vector3;
import com.dfsek.terra.api.implementations.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Runnable that locates a biome asynchronously
 */
public class AsyncBiomeFinder extends AsyncFeatureFinder<Biome> {

    public AsyncBiomeFinder(TerraBiomeGrid grid, Biome target, @NotNull Location origin, int startRadius, int maxRadius, Consumer<Vector3> callback, TerraBukkitPlugin main) {
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
        int res = main.getTerraConfig().getBiomeSearchResolution();
        return getGrid().getBiome(x * res, z * res, GenerationPhase.POST_GEN).equals(target);
    }

    @Override
    public Vector3 finalizeVector(Vector3 orig) {
        return orig.multiply(main.getTerraConfig().getBiomeSearchResolution());
    }
}
