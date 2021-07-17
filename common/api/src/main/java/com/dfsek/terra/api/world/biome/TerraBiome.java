package com.dfsek.terra.api.world.biome;


import com.dfsek.terra.api.properties.PropertyHolder;
import com.dfsek.terra.api.properties.annotations.Linked;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;
import com.dfsek.terra.api.world.World;

import java.util.Set;

/**
 * Represents a custom biome
 */
@Linked(BiomeBuilder.class)
public interface TerraBiome extends PropertyHolder {

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
