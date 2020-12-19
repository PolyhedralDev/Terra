package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.builder.GeneratorBuilder;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.UserDefinedDecorator;

public class BiomeFactory implements TerraFactory<BiomeTemplate, UserDefinedBiome> {
    private final ConfigPack pack;

    public BiomeFactory(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public UserDefinedBiome build(BiomeTemplate template, TerraPlugin main) {
        UserDefinedDecorator decorator = new UserDefinedDecorator(new ProbabilityCollection<>(), new ProbabilityCollection<>(), 0, 0);
        GeneratorBuilder generatorBuilder = new GeneratorBuilder();
        generatorBuilder.setElevationEquation(template.getElevationEquation());
        generatorBuilder.setNoiseEquation(template.getNoiseEquation());
        generatorBuilder.setNoiseBuilderMap(template.getPack().getTemplate().getNoiseBuilderMap());
        generatorBuilder.setPalettes(template.getPalette());
        generatorBuilder.setSlantPalettes(template.getSlantPalette());
        generatorBuilder.setVarScope(pack.getVarScope());
        generatorBuilder.setInterpolateElevation(template.interpolateElevation());


        return new UserDefinedBiome(template.getVanilla(), decorator, generatorBuilder, template, pack);
    }
}
