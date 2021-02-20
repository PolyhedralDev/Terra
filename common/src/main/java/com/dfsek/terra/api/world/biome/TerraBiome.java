package com.dfsek.terra.api.world.biome;


import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.World;

import java.util.Set;

/**
 * Represents a custom biome
 */
public interface TerraBiome {

    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return TerraBiome - The Vanilla biome.
     */
    ProbabilityCollection<Biome> getVanillaBiomes();

    /**
     * Gets the BiomeTerrain instance used to generate the biome.
     *
     * @return BiomeTerrain - The terrain generation instance.
     */
    Generator getGenerator(World w);

    int getColor();

    Set<String> getTags();

    String getID();
}
