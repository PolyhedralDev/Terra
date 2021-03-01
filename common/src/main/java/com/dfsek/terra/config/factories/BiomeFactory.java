package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.config.builder.UserDefinedBiomeBuilder;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;

public class BiomeFactory implements ConfigFactory<BiomeTemplate, BiomeBuilder<? extends TerraBiome>> {
    private final ConfigPack pack;

    public BiomeFactory(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public BiomeBuilder<? extends TerraBiome> build(BiomeTemplate template, TerraPlugin main) {
        return new UserDefinedBiomeBuilder(template, pack);
    }
}
