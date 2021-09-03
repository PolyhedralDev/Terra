package com.dfsek.terra.addons.biome.pipeline;

import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.config.BiomePipelineTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.NoiseSourceTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.expander.ExpanderStageTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.BorderListMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.BorderMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.ReplaceListMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.ReplaceMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.config.stage.mutator.SmoothMutatorTemplate;
import com.dfsek.terra.addons.biome.pipeline.source.BiomeSource;
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


@Addon("biome-provider-pipeline")
@Author("Terra")
@Version("1.0.0")
public class BiomePipelineAddon extends TerraAddon {
    
    public static final TypeKey<Supplier<ObjectTemplate<BiomeSource>>> SOURCE_REGISTRY_KEY = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<Stage>>> STAGE_REGISTRY_KEY = new TypeKey<>() {
    };
    public static final TypeKey<Supplier<ObjectTemplate<BiomeProvider>>> PROVIDER_REGISTRY_KEY = new TypeKey<>() {
    };
    @Inject
    private TerraPlugin main;
    
    @Override
    public void initialize() {
        main.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(this, ConfigPackPreLoadEvent.class)
            .then(event -> {
                CheckedRegistry<Supplier<ObjectTemplate<BiomeProvider>>> providerRegistry = event.getPack().getOrCreateRegistry(
                        PROVIDER_REGISTRY_KEY);
                providerRegistry.register("PIPELINE", () -> new BiomePipelineTemplate(main));
            })
            .then(event -> {
                CheckedRegistry<Supplier<ObjectTemplate<BiomeSource>>> sourceRegistry = event.getPack().getOrCreateRegistry(
                        SOURCE_REGISTRY_KEY);
                sourceRegistry.register("NOISE", NoiseSourceTemplate::new);
            })
            .then(event -> {
                CheckedRegistry<Supplier<ObjectTemplate<Stage>>> stageRegistry = event.getPack().getOrCreateRegistry(STAGE_REGISTRY_KEY);
                stageRegistry.register("FRACTAL_EXPAND", ExpanderStageTemplate::new);
                stageRegistry.register("SMOOTH", SmoothMutatorTemplate::new);
                stageRegistry.register("REPLACE", ReplaceMutatorTemplate::new);
                stageRegistry.register("REPLACE_LIST", ReplaceListMutatorTemplate::new);
                stageRegistry.register("BORDER", BorderMutatorTemplate::new);
                stageRegistry.register("BORDER_LIST", BorderListMutatorTemplate::new);
            })
            .failThrough();
    }
}
