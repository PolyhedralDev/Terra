package com.dfsek.terra.biome;

import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.builder.GeneratorBuilder;
import com.dfsek.terra.config.templates.BiomeTemplate;

/**
 * Class representing a config-defined biome
 */
public class UserDefinedBiome implements TerraBiome {
    private final GeneratorBuilder gen;
    private final com.dfsek.terra.api.platform.world.Biome vanilla;
    private final String id;
    private final BiomeTemplate config;
    private final ConfigPack pack;
    private UserDefinedBiome erode;
    private int color;


    public UserDefinedBiome(com.dfsek.terra.api.platform.world.Biome vanilla, GeneratorBuilder gen, BiomeTemplate config, ConfigPack pack) {
        this.vanilla = vanilla;
        this.gen = gen;
        this.id = config.getID();
        this.config = config;
        this.pack = pack;
        this.color = config.getColor();
    }

    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return TerraBiome - The Vanilla biome.
     */
    @Override
    public com.dfsek.terra.api.platform.world.Biome getVanillaBiome() {
        return vanilla;
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

    @Override
    public int getColor() {
        return color;
    }
}
