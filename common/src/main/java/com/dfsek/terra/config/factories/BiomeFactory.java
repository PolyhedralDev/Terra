package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.builder.GeneratorBuilder;
import com.dfsek.terra.config.templates.BiomeTemplate;

public class BiomeFactory implements TerraFactory<BiomeTemplate, UserDefinedBiome> {
    private final ConfigPack pack;

    public BiomeFactory(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public UserDefinedBiome build(BiomeTemplate template, TerraPlugin main) {
        GeneratorBuilder generatorBuilder = new GeneratorBuilder();
        generatorBuilder.setElevationEquation(template.getElevationEquation());
        generatorBuilder.setNoiseEquation(template.getNoiseEquation());
        generatorBuilder.setNoiseBuilderMap(template.getPack().getTemplate().getNoiseBuilderMap());
        generatorBuilder.setPalettes(template.getPalette());
        generatorBuilder.setSlantPalettes(template.getSlantPalette());
        generatorBuilder.setVarScope(pack.getVarScope());
        generatorBuilder.setInterpolateElevation(template.interpolateElevation());
        generatorBuilder.setNoise2d(template.isNoise2d());
        generatorBuilder.setBase(template.getNoise2dBase());
        generatorBuilder.setElevationWeight(template.getElevationWeight());
        generatorBuilder.setBiomeNoise(template.getBiomeNoise());


        return new UserDefinedBiome(template.getVanilla(), generatorBuilder, template, pack);
    }
}
