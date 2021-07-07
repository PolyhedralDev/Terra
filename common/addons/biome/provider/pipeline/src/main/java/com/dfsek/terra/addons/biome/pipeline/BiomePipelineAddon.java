package com.dfsek.terra.addons.biome.pipeline;

import com.dfsek.terra.addons.biome.pipeline.config.BiomePipelineTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.BiomeProviderBuilderLoader;
import com.dfsek.terra.addons.biome.pipeline.config.NoiseSourceTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.SourceBuilderLoader;
import com.dfsek.terra.addons.biome.pipeline.config.stage.StageBuilderLoader;
import com.dfsek.terra.addons.biome.pipeline.config.stage.expander.ExpanderStageTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.BorderListMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.BorderMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.ReplaceListMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.ReplaceMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.SmoothMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.addons.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.util.seeded.BiomeProviderBuilder;
import com.dfsek.terra.api.util.seeded.SourceSeeded;

import java.lang.reflect.Type;

@Addon("biome-pipeline")
@Author("Terra")
@Version("1.0.0")
public class BiomePipelineAddon extends TerraAddon implements EventListener {
    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.applyLoader(SourceSeeded.class, new SourceBuilderLoader())
                .applyLoader(StageSeeded.class, new StageBuilderLoader())
                .applyLoader(ExpanderStage.Type.class, (c, o, l) -> ExpanderStage.Type.valueOf((String) o))
                .applyLoader(MutatorStage.Type.class, (c, o, l) -> MutatorStage.Type.valueOf((String) o))
                .applyLoader(NoiseSourceTemplate.class, NoiseSourceTemplate::new)
                .applyLoader(ReplaceMutatorTemplate.class, ReplaceMutatorTemplate::new)
                .applyLoader(BorderMutatorTemplate.class, BorderMutatorTemplate::new)
                .applyLoader(BorderListMutatorTemplate.class, BorderListMutatorTemplate::new)
                .applyLoader(ReplaceListMutatorTemplate.class, ReplaceListMutatorTemplate::new)
                .applyLoader(SmoothMutatorTemplate.class, SmoothMutatorTemplate::new)
                .applyLoader(ExpanderStageTemplate.class, ExpanderStageTemplate::new)
                .applyLoader((Type) BiomePipelineTemplate.class, () -> new BiomePipelineTemplate(main))
                .applyLoader(BiomeProviderBuilder.class, new BiomeProviderBuilderLoader());
    }
}