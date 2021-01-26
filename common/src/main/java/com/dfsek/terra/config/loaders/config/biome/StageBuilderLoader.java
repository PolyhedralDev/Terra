package com.dfsek.terra.config.loaders.config.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.expand.FractalExpander;
import com.dfsek.terra.biome.pipeline.mutator.BorderListMutator;
import com.dfsek.terra.biome.pipeline.mutator.BorderMutator;
import com.dfsek.terra.biome.pipeline.mutator.ReplaceListMutator;
import com.dfsek.terra.biome.pipeline.mutator.ReplaceMutator;
import com.dfsek.terra.biome.pipeline.mutator.SmoothMutator;
import com.dfsek.terra.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.biome.pipeline.stages.SeededBuilder;
import com.dfsek.terra.biome.pipeline.stages.Stage;
import com.dfsek.terra.config.loaders.SelfProbabilityCollectionLoader;
import com.dfsek.terra.config.loaders.Types;
import com.dfsek.terra.config.loaders.config.NoiseBuilderLoader;
import com.dfsek.terra.world.generation.config.NoiseBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class StageBuilderLoader implements TypeLoader<SeededBuilder<Stage>> {
    @Override
    public SeededBuilder<Stage> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map.Entry<String, Object> entry = (Map.Entry<String, Object>) c;

        Map<String, Object> mutator = (Map<String, Object>) entry.getValue();
        NoiseBuilder mutatorNoise = new NoiseBuilderLoader().load(NoiseBuilder.class, mutator.get("noise"), loader);

        if(entry.getKey().equals("expand")) {
            if(mutator.get("type").equals("FRACTAL"))
                return new SeededBuilder<>(seed -> new ExpanderStage(new FractalExpander(mutatorNoise.build((int) seed.longValue()))));
            else throw new LoadException("No such expander \"" + mutator.get("type"));
        } else if(entry.getKey().equals("mutate")) {

            switch(mutator.get("type").toString()) {
                case "SMOOTH": {
                    return new SeededBuilder<>(seed -> new MutatorStage(new SmoothMutator(mutatorNoise.build((int) seed.longValue()))));
                }
                case "REPLACE": {
                    String fromTag = mutator.get("from").toString();
                    ProbabilityCollection<TerraBiome> replaceBiomes = new SelfProbabilityCollectionLoader<TerraBiome>().load(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, mutator.get("to"), loader);

                    return new SeededBuilder<>(seed -> new MutatorStage(new ReplaceMutator(fromTag, replaceBiomes, mutatorNoise.build((int) seed.longValue()))));
                }
                case "REPLACE_LIST": {
                    String fromTag = mutator.get("default-from").toString();
                    ProbabilityCollection<TerraBiome> replaceBiomes = new SelfProbabilityCollectionLoader<TerraBiome>().load(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, mutator.get("default-to"), loader);

                    Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace = new HashMap<>();
                    for(Map.Entry<String, Object> e : ((Map<String, Object>) mutator.get("to")).entrySet()) {
                        replace.put((TerraBiome) loader.loadType(TerraBiome.class, e.getKey()), new SelfProbabilityCollectionLoader<TerraBiome>().load(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, e.getValue(), loader));
                    }

                    return new SeededBuilder<>(seed -> new MutatorStage(new ReplaceListMutator(replace, fromTag, replaceBiomes, mutatorNoise.build((int) seed.longValue()))));
                }
                case "BORDER": {
                    String fromTag = mutator.get("from").toString();
                    String replaceTag = mutator.get("replace").toString();
                    ProbabilityCollection<TerraBiome> replaceBiomes = new SelfProbabilityCollectionLoader<TerraBiome>().load(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, mutator.get("to"), loader);

                    return new SeededBuilder<>(seed -> new MutatorStage(new BorderMutator(fromTag, replaceTag, mutatorNoise.build((int) seed.longValue()), replaceBiomes)));
                }
                case "BORDER_LIST": {
                    String fromTag = mutator.get("from").toString();
                    String replaceTag = mutator.get("default-replace").toString();
                    ProbabilityCollection<TerraBiome> replaceBiomes = new SelfProbabilityCollectionLoader<TerraBiome>().load(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, mutator.get("default-to"), loader);

                    Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace = new HashMap<>();
                    for(Map.Entry<String, Object> e : ((Map<String, Object>) mutator.get("replace")).entrySet()) {
                        replace.put((TerraBiome) loader.loadType(TerraBiome.class, e.getKey()), new SelfProbabilityCollectionLoader<TerraBiome>().load(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, e.getValue(), loader));
                    }

                    return new SeededBuilder<>(seed -> new MutatorStage(new BorderListMutator(replace, fromTag, replaceTag, mutatorNoise.build((int) seed.longValue()), replaceBiomes)));
                }
                default:
                    throw new LoadException("No such mutator type \"" + mutator.get("type"));

            }
        }
        throw new LoadException("No such mutator \"" + entry.getKey() + "\"");
    }
}
