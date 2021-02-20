package com.dfsek.terra.async;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Runnable that locates a biome asynchronously
 */
public class AsyncBiomeFinder extends AsyncFeatureFinder<TerraBiome> {

    public AsyncBiomeFinder(BiomeProvider provider, TerraBiome target, @NotNull Location origin, int startRadius, int maxRadius, Consumer<Vector3> callback, TerraPlugin main) {
        super(provider, target, origin, startRadius, maxRadius, callback, main);
    }

    /**
     * Helper method to get biome at location
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return TerraBiome at coordinates
     */
    @Override
    public boolean isValid(int x, int z, TerraBiome target) {
        int res = main.getTerraConfig().getBiomeSearchResolution();
        return getProvider().getBiome(x * res, z * res).equals(target);
    }

    @Override
    public Vector3 finalizeVector(Vector3 orig) {
        return orig.multiply(main.getTerraConfig().getBiomeSearchResolution());
    }
}
