package com.dfsek.terra.biome;

import com.dfsek.terra.generation.UserDefinedDecorator;
import com.dfsek.terra.generation.UserDefinedGenerator;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.biome.Decorator;
import org.polydev.gaea.structures.features.Feature;

import java.util.List;

public class UserDefinedBiome implements Biome {
    private final UserDefinedGenerator gen;
    private final UserDefinedDecorator decorator;
    private final org.bukkit.block.Biome vanilla;
    private final String id;


    public UserDefinedBiome(org.bukkit.block.Biome vanilla, UserDefinedDecorator dec, UserDefinedGenerator gen, String id) {
        this.vanilla = vanilla;
        this.decorator = dec;
        this.gen = gen;
        this.id = id;
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
        return gen;
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
}
