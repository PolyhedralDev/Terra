package com.dfsek.terra.addons.biome.pipeline;

import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.config.BiomePipelineTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.BiomeProviderLoader;
import com.dfsek.terra.addons.biome.pipeline.config.NoiseSourceTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.StageLoader;
import com.dfsek.terra.addons.biome.pipeline.config.stage.expander.ExpanderStageTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.BorderListMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.BorderMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.ReplaceListMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.ReplaceMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.SmoothMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.mutator.BorderListMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.BorderMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.ReplaceListMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.ReplaceMutator;
import com.dfsek.terra.addons.biome.pipeline.mutator.SmoothMutator;
import com.dfsek.terra.addons.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.addons.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;

import java.util.function.Supplier;

@Addon("biome-provider-pipeline")
@Author("Terra")
@Version("1.0.0")
public class BiomePipelineAddon extends TerraAddon {

    public static final TypeKey<Supplier<ObjectTemplate<BiomeSource>>> SOURCE_REGISTRY_KEY = new TypeKey<>() {};

    @Inject
    private TerraPlugin main;

    @Override
    public void initialize() {
        main.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(this, ConfigPackPreLoadEvent.class)
                .then(event -> event.getPack().applyLoader(Stage.class, new StageLoader())
                        .applyLoader(ExpanderStage.Type.class, (c, o, l) -> ExpanderStage.Type.valueOf((String) o))
                        .applyLoader(MutatorStage.Type.class, (c, o, l) -> MutatorStage.Type.valueOf((String) o))
                        .applyLoader(ReplaceMutator.class, ReplaceMutatorTemplate::new)
                        .applyLoader(BorderMutator.class, BorderMutatorTemplate::new)
                        .applyLoader(BorderListMutator.class, BorderListMutatorTemplate::new)
                        .applyLoader(ReplaceListMutator.class, ReplaceListMutatorTemplate::new)
                        .applyLoader(SmoothMutator.class, SmoothMutatorTemplate::new)
                        .applyLoader(ExpanderStage.class, ExpanderStageTemplate::new)
                        .applyLoader(BiomePipelineProvider.class, () -> new BiomePipelineTemplate(main))
                        .applyLoader(BiomeProvider.class, new BiomeProviderLoader())
                        .applyLoader(BiomeSource.Type.class, (t, object, cf) -> BiomeSource.Type.valueOf((String) object)))
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<BiomeSource>>> sourceRegistry = event.getPack().getOrCreateRegistry(SOURCE_REGISTRY_KEY);
                    sourceRegistry.register("NOISE", NoiseSourceTemplate::new);
                })
                .failThrough();
    }
}
