package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.builder.GeneratorBuilder;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;
import parsii.eval.Scope;

import java.util.HashMap;
import java.util.Map;

public class BiomeFactory implements TerraFactory<BiomeTemplate, TerraBiome> {
    private final ConfigPack pack;

    public BiomeFactory(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public UserDefinedBiome build(BiomeTemplate template, TerraPlugin main) {
        GeneratorBuilder generatorBuilder = new GeneratorBuilder();
        generatorBuilder.setElevationEquation(template.getElevationEquation());
        generatorBuilder.setNoiseEquation(template.getNoiseEquation());
        generatorBuilder.setCarvingEquation(template.getCarvingEquation());
        generatorBuilder.setNoiseBuilderMap(pack.getTemplate().getNoiseBuilderMap());

        Map<String, FunctionTemplate> functions = new HashMap<>(pack.getTemplate().getFunctions());
        functions.putAll(template.getFunctions());
        generatorBuilder.setFunctionTemplateMap(functions);

        generatorBuilder.setPalettes(template.getPalette());
        generatorBuilder.setSlantPalettes(template.getSlantPalette());

        Scope vars = new Scope().withParent(pack.getVarScope());
        template.getVariables().forEach((id, val) -> vars.create(id).setValue(val));
        generatorBuilder.setVarScope(vars);

        generatorBuilder.setInterpolateElevation(template.interpolateElevation());
        generatorBuilder.setElevationWeight(template.getElevationWeight());
        generatorBuilder.setBiomeNoise(template.getBiomeNoise());
        generatorBuilder.setBlendDistance(template.getBlendDistance());
        generatorBuilder.setBlendStep(template.getBlendStep());
        generatorBuilder.setBlendWeight(template.getBlendWeight());


        return new UserDefinedBiome(template.getVanilla(), generatorBuilder, template);
    }
}
