package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.world.generation.WorldGenerator;

import java.util.Set;

/**
 * Class representing a config-defined biome
 */
public class UserDefinedBiome implements TerraBiome {
    private final WorldGenerator gen;
    private final ProbabilityCollection<Biome> vanilla;
    private final String id;
    private final BiomeTemplate config;
    private final int color;
    private final Set<String> tags;


    public UserDefinedBiome(ProbabilityCollection<Biome> vanilla, WorldGenerator gen, BiomeTemplate config) {
        this.vanilla = vanilla;
        this.gen = gen;
        this.id = config.getID();
        this.config = config;
        this.color = config.getColor();
        this.tags = config.getTags();
        tags.add("BIOME:" + id);
    }

    /**
     * Gets the Vanilla biomes to represent the custom biome.
     *
     * @return Collection of biomes to represent the custom biome.
     */
    @Override
    public ProbabilityCollection<Biome> getVanillaBiomes() {
        return vanilla;
    }

    @Override
    public String getID() {
        return id;
    }

    public BiomeTemplate getConfig() {
        return config;
    }

    @Override
    public Generator getGenerator(World w) {
        return gen;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Set<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "{BIOME:" + getID() + "}";
    }
}
