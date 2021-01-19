package com.dfsek.terra.config.loaders.config.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.biome.ImageBiomeProvider;
import com.dfsek.terra.biome.StandardBiomeProvider;
import com.dfsek.terra.biome.pipeline.BiomePipeline;
import com.dfsek.terra.biome.pipeline.expand.FractalExpander;
import com.dfsek.terra.biome.pipeline.mutator.BorderMutator;
import com.dfsek.terra.biome.pipeline.mutator.ReplaceMutator;
import com.dfsek.terra.biome.pipeline.mutator.SmoothMutator;
import com.dfsek.terra.biome.pipeline.source.RandomSource;
import com.dfsek.terra.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.config.files.Loader;
import com.dfsek.terra.config.loaders.SelfProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.Types;
import com.dfsek.terra.config.loaders.config.NoiseBuilderLoader;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.generation.config.NoiseBuilder;
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
    public BiomeProvider.BiomeProviderBuilder load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) c;

        int resolution = 1;
        if(map.containsKey("resolution")) resolution = Integer.parseInt(map.get("resolution").toString());


        if(map.get("type").equals("PIPELINE")) {
            Map<String, Object> pipeline = (Map<String, Object>) map.get("pipeline");
            StandardBiomeProvider.StandardBiomeProviderBuilder builder = new StandardBiomeProvider.StandardBiomeProviderBuilder(seed -> {

                Map<String, Object> source = (Map<String, Object>) pipeline.get("source");
                ProbabilityCollection<TerraBiome> sourceBiomes = (ProbabilityCollection<TerraBiome>) loader.loadType(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, source.get("biomes"));
                NoiseSampler sourceNoise = new NoiseBuilderLoader().load(NoiseBuilder.class, source.get("noise"), loader).build((int) seed.longValue());

                List<Map<String, Object>> stages = (List<Map<String, Object>>) pipeline.get("stages");

                int init;
                if(pipeline.containsKey("initial-size")) init = Integer.parseInt(pipeline.get("initial-size").toString());
                else init = 3;

                BiomePipeline.BiomePipelineBuilder pipelineBuilder = new BiomePipeline.BiomePipelineBuilder(init);

                for(Map<String, Object> stage : stages) {
                    for(Map.Entry<String, Object> entry : stage.entrySet()) {
                        Map<String, Object> mutator = (Map<String, Object>) entry.getValue();
                        NoiseSampler mutatorNoise = new NoiseBuilderLoader().load(NoiseBuilder.class, mutator.get("noise"), loader).build((int) seed.longValue());

                        if(entry.getKey().equals("expand")) {
                            if(mutator.get("type").equals("FRACTAL"))
                                pipelineBuilder.addStage(new ExpanderStage(new FractalExpander(mutatorNoise)));
                            else throw new LoadException("No such expander \"" + mutator.get("type"));
                        } else if(entry.getKey().equals("mutate")) {
                            if(mutator.get("type").equals("SMOOTH"))
                                pipelineBuilder.addStage(new MutatorStage(new SmoothMutator(mutatorNoise)));
                            else if(mutator.get("type").equals("REPLACE")) {
                                String fromTag = mutator.get("from").toString();
                                ProbabilityCollection<TerraBiome> replaceBiomes = new SelfProbabilityCollectionLoader<TerraBiome>().load(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, mutator.get("to"), loader);
                                pipelineBuilder.addStage(new MutatorStage(new ReplaceMutator(fromTag, replaceBiomes, mutatorNoise)));
                            } else if(mutator.get("type").equals("BORDER")) {
                                String fromTag = mutator.get("from").toString();
                                String replaceTag = mutator.get("replace").toString();
                                ProbabilityCollection<TerraBiome> replaceBiomes = (ProbabilityCollection<TerraBiome>) loader.loadType(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, mutator.get("to"));
                                pipelineBuilder.addStage(new MutatorStage(new BorderMutator(fromTag, replaceTag, mutatorNoise, replaceBiomes)));
                            } else throw new LoadException("No such mutator type \"" + mutator.get("type"));
                        } else throw new LoadException("No such mutator \"" + entry.getKey() + "\"");
                    }
                }
                BiomePipeline biomePipeline = pipelineBuilder.build(new RandomSource(sourceBiomes, sourceNoise));
                Debug.info("Biome Pipeline scale factor: " + biomePipeline.getSize());
                return biomePipeline;
            }, main);
            builder.setResolution(resolution);
            if(map.containsKey("blend")) {
                Map<String, Object> blend = (Map<String, Object>) map.get("blend");
                if(blend.containsKey("amplitude")) builder.setNoiseAmp(Integer.parseInt(blend.get("amplitude").toString()));
                if(blend.containsKey("noise"))
                    builder.setBuilder(new NoiseBuilderLoader().load(NoiseBuilder.class, blend.get("noise"), loader));
            }
            return builder;
        } else if(map.get("type").equals("IMAGE")) {
            Map<String, Object> imageMap = (Map<String, Object>) map.get("image");
            try {
                main.getLogger().info("Using image " + imageMap.get("name") + " for biome distribution.");
                BufferedImage image = ImageIO.read(fileLoader.get(imageMap.get("name").toString()));
                return new ImageBiomeProvider.ImageBiomeProviderBuilder(image, resolution, biomeRegistry);
            } catch(IOException e) {
                throw new LoadException("Failed to load image", e);
            }
        } else throw new LoadException("No such biome provider type: " + map.get("type"));
    }
}
