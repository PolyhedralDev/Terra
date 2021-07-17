package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.Set;

/**
 * Class representing a config-defined biome
 */
public class UserDefinedBiome implements TerraBiome {
    private final UserDefinedGenerator gen;
    private final ProbabilityCollection<Biome> vanilla;
    private final String id;
    private final BiomeTemplate config;
    private final int color;
    private final Set<String> tags;

    private final Context context;

    public UserDefinedBiome(ProbabilityCollection<Biome> vanilla, UserDefinedGenerator gen, BiomeTemplate config, Context context) {
        this.vanilla = vanilla;
        this.gen = gen;
        this.id = config.getID();
        this.config = config;
        this.color = config.getColor();
        this.tags = config.getTags();
        this.context = context;
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

    @Override
    public Context getContext() {
        return context;
    }
}
