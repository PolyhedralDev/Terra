package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.TerraBiome;

public class BiomeFactory implements ConfigFactory<BiomeTemplate, TerraBiome> {
    private final ConfigPack pack;

    public BiomeFactory(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public TerraBiome build(BiomeTemplate template, TerraPlugin main) {
        UserDefinedGenerationSettings generator = new UserDefinedGenerationSettings(template.getNoiseEquation(), template.getElevationEquation(), template.getCarvingEquation(), template.getBiomeNoise(), template.getElevationWeight(),
                template.getBlendDistance(), template.getBlendStep(), template.getBlendWeight());
        return new UserDefinedBiome(template.getVanilla(), generator, template);
    }
}
