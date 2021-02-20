package com.dfsek.terra.config.factories;

import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.config.builder.GeneratorBuilder;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;

import java.util.LinkedHashMap;
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

        Map<String, FunctionTemplate> functions = new LinkedHashMap<>(pack.getTemplate().getFunctions()); // linked map to preserve order.
        functions.putAll(template.getFunctions());
        generatorBuilder.setFunctionTemplateMap(functions);

        generatorBuilder.setPalettes(template.getPalette());
        generatorBuilder.setSlantPalettes(template.getSlantPalette());

        Scope vars = new Scope().withParent(pack.getVarScope());
        template.getVariables().forEach(vars::create);
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
