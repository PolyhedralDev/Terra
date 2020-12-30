package com.dfsek.terra.api.world.biome;


import com.dfsek.terra.api.platform.world.World;

/**
 * Interface to be implemented by a custom generator's Biome enum.<br>
 * Represents a custom biome, and contains methods to retrieve information about each type.
 */
public interface Biome {

    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return Biome - The Vanilla biome.
     */
    com.dfsek.terra.api.platform.world.Biome getVanillaBiome();

    /**
     * Gets the BiomeTerrain instance used to generate the biome.
     *
     * @return BiomeTerrain - The terrain generation instance.
     */
    Generator getGenerator();

    /**
     * Gets the BiomeTerrain instance used to generate the biome in this world.
     *
     * @return BiomeTerrain - The terrain generation instance.
     */
    default Generator getGenerator(World w) {
        return getGenerator();
    }
}
