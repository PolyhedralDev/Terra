package com.dfsek.terra.config.loaders.config.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.util.seeded.StageSeeded;
import com.dfsek.terra.api.world.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.api.world.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.expander.ExpanderStageTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.BorderListMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.BorderMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.ReplaceListMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.ReplaceMutatorTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.stage.mutator.SmoothMutatorTemplate;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class StageBuilderLoader implements TypeLoader<StageSeeded> {
    @Override
    public StageSeeded load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> raw = (Map<String, Object>) c;

        if(raw.size() != 1) throw new LoadException("Illegal stage map size: " + raw.size());

        Map.Entry<String, Object> entry = null;

        for(Map.Entry<String, Object> e : raw.entrySet()) {
            entry = e;
        }

        Map<String, Object> mutator = (Map<String, Object>) entry.getValue();

        if(entry.getKey().equals("expand")) {
            ExpanderStage.Type stageType = loader.loadClass(ExpanderStage.Type.class, mutator.get("type"));
            if(stageType.equals(ExpanderStage.Type.FRACTAL)) {
                return loader.loadClass(ExpanderStageTemplate.class, mutator);
            } else throw new LoadException("No such expander \"" + stageType + "\"");
        } else if(entry.getKey().equals("mutate")) {
            return switch(loader.loadClass(MutatorStage.Type.class, mutator.get("type"))) {
                case SMOOTH -> loader.loadClass(SmoothMutatorTemplate.class, mutator);
                case REPLACE -> loader.loadClass(ReplaceMutatorTemplate.class, mutator);
                case REPLACE_LIST -> loader.loadClass(ReplaceListMutatorTemplate.class, mutator);
                case BORDER -> loader.loadClass(BorderMutatorTemplate.class, mutator);
                case BORDER_LIST -> loader.loadClass(BorderListMutatorTemplate.class, mutator);
            };
        }
        throw new LoadException("No such mutator \"" + entry.getKey() + "\"");
    }
}
