package com.dfsek.terra.biome;

import com.dfsek.terra.WorldObject;
import com.dfsek.terra.config.builder.GeneratorBuilder;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.UserDefinedDecorator;
import com.dfsek.terra.generation.config.WorldGenerator;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.structures.features.Feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing a config-defined biome
 */
public class UserDefinedBiome implements Biome, WorldObject {
    private final GeneratorBuilder gen;
    private final UserDefinedDecorator decorator;
    private final org.bukkit.block.Biome vanilla;
    private final String id;
    private final BiomeTemplate config;
    private final boolean erode;
    private final Map<World, WorldGenerator> gens = new HashMap<>();


    public UserDefinedBiome(org.bukkit.block.Biome vanilla, UserDefinedDecorator dec, GeneratorBuilder gen, boolean erode, BiomeTemplate config) {
        this.vanilla = vanilla;
        this.decorator = dec;
        this.gen = gen;
        this.id = config.getID();
        this.erode = erode;
        this.config = config;
    }

    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return Biome - The Vanilla biome.
     */
    @Override
    public org.bukkit.block.Biome getVanillaBiome() {
        return vanilla;
    }

    /**
     * Gets the Generator instance used to generate the biome.
     *
     * @return Generator - The terrain generation instance.
     */
    @Override
    public Generator getGenerator() {
        return gen.build(0);
    }

    /**
     * Gets a list of Structure Features to be applied to all structures in the biome.
     *
     * @return List&lt;Feature&gt; - The list of Features.
     */
    @Override
    public List<Feature> getStructureFeatures() {
        return null;
    }

    /**
     * Returns the Decorator instance containing information about the population in the biome.
     *
     * @return Decorator - the Decorator instance.
     */
    @Override
    public Decorator getDecorator() {
        return decorator;
    }

    public String getID() {
        return id;
    }

    public boolean isErodible() {
        return erode;
    }

    public BiomeTemplate getConfig() {
        return config;
    }

    @Override
    public Generator getGenerator(World w) {
        return gens.computeIfAbsent(w, world -> gen.build(world.getSeed()));
    }
}
