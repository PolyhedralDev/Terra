package com.dfsek.terra.config.loaders.config.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.BiomePipeline;
import com.dfsek.terra.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.biome.provider.BiomeProvider;
import com.dfsek.terra.biome.provider.ImageBiomeProvider;
import com.dfsek.terra.biome.provider.SingleBiomeProvider;
import com.dfsek.terra.biome.provider.StandardBiomeProvider;
import com.dfsek.terra.config.fileloaders.Loader;
import com.dfsek.terra.registry.TerraRegistry;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class BiomeProviderBuilderLoader implements TypeLoader<BiomeProvider.BiomeProviderBuilder> {
    private final TerraPlugin main;
    private final TerraRegistry<TerraBiome> biomeRegistry;
    private final Loader fileLoader;

    public BiomeProviderBuilderLoader(TerraPlugin main, TerraRegistry<TerraBiome> biomeRegistry, Loader fileLoader) {
        this.main = main;
        this.biomeRegistry = biomeRegistry;
        this.fileLoader = fileLoader;
    }

    @Override
    public BiomeProvider.BiomeProviderBuilder load(Type t, Object c, ConfigLoader loader) throws LoadException { // TODO: clean this up
        Map<String, Object> map = (Map<String, Object>) c;
        int resolution = (Integer) map.getOrDefault("resolution", 1);

        if(map.get("type").equals("PIPELINE")) {
            Map<String, Object> pipeline = (Map<String, Object>) map.get("pipeline");

            List<Map<String, Object>> stages = (List<Map<String, Object>>) pipeline.get("stages");

            if(stages == null) throw new LoadException("No pipeline stages defined!");

            int init = (Integer) pipeline.getOrDefault("initial-size", 2);

            StandardBiomeProvider.StandardBiomeProviderBuilder builder = new StandardBiomeProvider.StandardBiomeProviderBuilder(seed -> {
                BiomePipeline.BiomePipelineBuilder pipelineBuilder = new BiomePipeline.BiomePipelineBuilder(init);

                for(Map<String, Object> stage : stages) {
                    for(Map.Entry<String, Object> entry : stage.entrySet()) {
                        pipelineBuilder.addStage(new StageBuilderLoader().load(SeededBuilder.class, entry, loader));
                    }
                }

                if(!pipeline.containsKey("source")) throw new LoadException("Biome Source not defined!");
                SeededBuilder<BiomeSource> source = new SourceBuilderLoader().load(BiomeSource.class, pipeline.get("source"), loader);

                BiomePipeline biomePipeline = pipelineBuilder.build(source.apply(seed), seed);
                main.getDebugLogger().info("Biome Pipeline scale factor: " + biomePipeline.getSize());
                return biomePipeline;
            }, main);

            builder.setResolution(resolution);
            if(map.containsKey("blend")) {
                Map<String, Object> blend = (Map<String, Object>) map.get("blend");
                if(blend.containsKey("amplitude")) builder.setNoiseAmp(Double.parseDouble(blend.get("amplitude").toString()));
                if(blend.containsKey("noise"))
                    builder.setBlender(loader.loadClass(NoiseSeeded.class, blend.get("noise")));
            }
            return builder;
        } else if(map.get("type").equals("IMAGE")) {
            Map<String, Object> imageMap = (Map<String, Object>) map.get("image");
            try {
                main.getLogger().info("Using image " + imageMap.get("name") + " for biome distribution.");
                BufferedImage image = ImageIO.read(fileLoader.get(imageMap.get("name").toString()));
                return new ImageBiomeProvider(biomeRegistry, image, resolution, ImageBiomeProvider.Align.valueOf((String) imageMap.getOrDefault("align", "CENTER")));
            } catch(IOException e) {
                throw new LoadException("Failed to load image", e);
            }
        } else if(map.get("type").equals("SINGLE")) {
            return new SingleBiomeProvider(loader.loadClass(TerraBiome.class, map.get("biome")));
        }

        throw new LoadException("No such biome provider type: " + map.get("type"));
    }
}
