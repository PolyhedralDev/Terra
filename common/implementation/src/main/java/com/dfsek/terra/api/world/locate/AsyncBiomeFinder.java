package com.dfsek.terra.api.world.locate;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Runnable that locates a biome asynchronously
 */
public class AsyncBiomeFinder extends AsyncFeatureFinder<TerraBiome> {

    public AsyncBiomeFinder(BiomeProvider provider, TerraBiome target, @NotNull Vector3 origin, World world, int startRadius, int maxRadius, Consumer<Vector3> callback, TerraPlugin main) {
        super(provider, target, origin, world, startRadius, maxRadius, callback, main);
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
