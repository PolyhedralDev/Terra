package com.dfsek.terra.config.factories;

import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.builder.GeneratorBuilder;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.UserDefinedDecorator;
import org.polydev.gaea.math.ProbabilityCollection;

public class BiomeFactory implements TerraFactory<BiomeTemplate, UserDefinedBiome> {
    @Override
    public UserDefinedBiome build(BiomeTemplate template) {
        UserDefinedDecorator decorator = new UserDefinedDecorator(new ProbabilityCollection<>(), new ProbabilityCollection<>(), 0, 0);
        GeneratorBuilder generatorBuilder = new GeneratorBuilder();
        generatorBuilder.setElevationEquation(template.getElevationEquation());
        generatorBuilder.setNoiseEquation(template.getNoiseEquation());
        generatorBuilder.setNoiseBuilderMap(template.getPack().getTemplate().getNoiseBuilderMap());
        generatorBuilder.setPalettes(template.getPalette());
        generatorBuilder.setSlantPalettes(template.getSlantPalette());


        return new UserDefinedBiome(template.getVanilla(), decorator, generatorBuilder, template.isErodible(), template);
    }
}
