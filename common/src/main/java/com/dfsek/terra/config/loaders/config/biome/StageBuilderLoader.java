package com.dfsek.terra.config.loaders.config.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.biome.pipeline.stages.Stage;
import com.dfsek.terra.config.loaders.config.biome.templates.ExpanderStageTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.BorderListMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.BorderMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.ReplaceListMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.ReplaceMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.mutator.SmoothMutatorTemplate;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class StageBuilderLoader implements TypeLoader<SeededBuilder<Stage>> {
    @Override
    public SeededBuilder<Stage> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map.Entry<String, Object> entry = (Map.Entry<String, Object>) c;

        Map<String, Object> mutator = (Map<String, Object>) entry.getValue();

        if(entry.getKey().equals("expand")) {
            ExpanderStage.Type stageType = loader.loadClass(ExpanderStage.Type.class, mutator.get("type"));
            if(stageType.equals(ExpanderStage.Type.FRACTAL)) {
                return loader.loadClass(ExpanderStageTemplate.class, mutator).get();
            } else throw new LoadException("No such expander \"" + stageType + "\"");
        } else if(entry.getKey().equals("mutate")) {
            switch(loader.loadClass(MutatorStage.Type.class, mutator.get("type"))) {
                case SMOOTH:
                    return loader.loadClass(SmoothMutatorTemplate.class, mutator).get();
                case REPLACE:
                    return loader.loadClass(ReplaceMutatorTemplate.class, mutator).get();
                case REPLACE_LIST:
                    return loader.loadClass(ReplaceListMutatorTemplate.class, mutator).get();
                case BORDER:
                    return loader.loadClass(BorderMutatorTemplate.class, mutator).get();
                case BORDER_LIST:
                    return loader.loadClass(BorderListMutatorTemplate.class, mutator).get();
                default:
                    throw new LoadException("No such mutator type \"" + mutator.get("type"));

            }
        }
        throw new LoadException("No such mutator \"" + entry.getKey() + "\"");
    }
}
