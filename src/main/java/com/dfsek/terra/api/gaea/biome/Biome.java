package com.dfsek.terra.api.gaea.biome;

import org.bukkit.World;

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
    com.dfsek.terra.api.generic.world.Biome getVanillaBiome();

    /**
     * Gets the BiomeTerrain instance used to generate the biome.
     *
     * @return BiomeTerrain - The terrain generation instance.
     */
    Generator getGenerator();

    /**
     * Returns the Decorator instance containing information about the population in the biome.
     *
     * @return Decorator - the Decorator instance.
     */
    Decorator getDecorator();

    /**
     * Gets the BiomeTerrain instance used to generate the biome in this world.
     *
     * @return BiomeTerrain - The terrain generation instance.
     */
    default Generator getGenerator(World w) {
        return getGenerator();
    }
}
