package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;

public class BiomeFactory implements ConfigFactory<BiomeTemplate, BiomeBuilder> {
    private final ConfigPack pack;

    public BiomeFactory(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public BiomeBuilder build(BiomeTemplate template, TerraPlugin main) {
        return new UserDefinedBiomeBuilder(template, pack);
    }
}
