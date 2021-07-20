package com.dfsek.terra.addons.biome.pipeline.config.stage;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.config.stage.expander.ExpanderStageTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.BorderListMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.BorderMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.ReplaceListMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.ReplaceMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.SmoothMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.addons.biome.pipeline.stages.MutatorStage;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

@SuppressWarnings("unchecked")
public class StageBuilderLoader implements TypeLoader<Stage> {
    @Override
    public Stage load(AnnotatedType t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> raw = (Map<String, Object>) c;

        if(raw.size() != 1) throw new LoadException("Illegal stage map size: " + raw.size());

        Map.Entry<String, Object> entry = null;

        for(Map.Entry<String, Object> e : raw.entrySet()) {
            entry = e;
        }

        Map<String, Object> mutator = (Map<String, Object>) entry.getValue();

        if(entry.getKey().equals("expand")) {
            ExpanderStage.Type stageType = loader.loadType(ExpanderStage.Type.class, mutator.get("type"));
            if(stageType.equals(ExpanderStage.Type.FRACTAL)) {
                return loader.loadType(ExpanderStageTemplate.class, mutator).get();
            } else throw new LoadException("No such expander \"" + stageType + "\"");
        } else if(entry.getKey().equals("mutate")) {
            switch(loader.loadType(MutatorStage.Type.class, mutator.get("type"))) {
                case SMOOTH:
                    return loader.loadType(SmoothMutatorTemplate.class, mutator).get();
                case REPLACE:
                    return loader.loadType(ReplaceMutatorTemplate.class, mutator).get();
                case REPLACE_LIST:
                    return loader.loadType(ReplaceListMutatorTemplate.class, mutator).get();
                case BORDER:
                    return loader.loadType(BorderMutatorTemplate.class, mutator).get();
                case BORDER_LIST:
                    return loader.loadType(BorderListMutatorTemplate.class, mutator).get();
                default:
                    throw new LoadException("No such mutator type \"" + mutator.get("type"));
            }
        }
        throw new LoadException("No such mutator \"" + entry.getKey() + "\"");
    }
}
