package com.dfsek.terra.api.world.biome;


import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.World;

import java.util.Set;

/**
 * Interface to be implemented by a custom generator's TerraBiome enum.<br>
 * Represents a custom biome, and contains methods to retrieve information about each type.
 */
public interface TerraBiome {

    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return TerraBiome - The Vanilla biome.
     */
    Biome getVanillaBiome();

    /**
     * Gets the BiomeTerrain instance used to generate the biome.
     *
     * @return BiomeTerrain - The terrain generation instance.
     */
    Generator getGenerator(World w);

    int getColor();

    Set<String> getTags();
}
