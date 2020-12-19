package com.dfsek.terra.biome;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.Decorator;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.builder.GeneratorBuilder;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.UserDefinedDecorator;

/**
 * Class representing a config-defined biome
 */
public class UserDefinedBiome implements Biome {
    private final GeneratorBuilder gen;
    private final UserDefinedDecorator decorator;
    private final com.dfsek.terra.api.platform.world.Biome vanilla;
    private final String id;
    private final BiomeTemplate config;
    private final ConfigPack pack;
    private UserDefinedBiome erode;


    public UserDefinedBiome(com.dfsek.terra.api.platform.world.Biome vanilla, UserDefinedDecorator dec, GeneratorBuilder gen, BiomeTemplate config, ConfigPack pack) {
        this.vanilla = vanilla;
        this.decorator = dec;
        this.gen = gen;
        this.id = config.getID();
        this.config = config;
        this.pack = pack;
    }

    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return Biome - The Vanilla biome.
     */
    @Override
    public com.dfsek.terra.api.platform.world.Biome getVanillaBiome() {
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

    public UserDefinedBiome getErode() {
        if(erode == null) {
            erode = (config.getErode() == null) ? this : pack.getBiome(config.getErode());
        }
        return erode;
    }


    public BiomeTemplate getConfig() {
        return config;
    }

    @Override
    public Generator getGenerator(World w) {
        return gen.build(w.getSeed());
    }
}
