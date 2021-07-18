package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;

public class BiomeFactory implements ConfigFactory<BiomeTemplate, SeededTerraBiome> {
    private final ConfigPack pack;

    public BiomeFactory(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public SeededTerraBiome build(BiomeTemplate template, TerraPlugin main) {
        return new UserDefinedSeededTerraBiome(template);
    }
}
