package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.builder.UserDefinedBiomeBuilder;
import com.dfsek.terra.config.pack.ConfigPackImpl;
import com.dfsek.terra.config.templates.BiomeTemplate;

public class BiomeFactory implements ConfigFactory<BiomeTemplate, BiomeBuilder> {
    private final ConfigPackImpl pack;

    public BiomeFactory(ConfigPackImpl pack) {
        this.pack = pack;
    }

    @Override
    public BiomeBuilder build(BiomeTemplate template, TerraPlugin main) {
        return new UserDefinedBiomeBuilder(template, pack);
    }
}
