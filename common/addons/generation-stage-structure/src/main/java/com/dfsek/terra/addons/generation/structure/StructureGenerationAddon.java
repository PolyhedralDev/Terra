package com.dfsek.terra.addons.generation.structure;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.generation.structure.config.BiomeStructuresTemplate;
import com.dfsek.terra.addons.generation.structure.config.StructureGenerationStageTemplate;
import com.dfsek.terra.addons.generation.structure.config.StructureLayerGridDescription;
import com.dfsek.terra.addons.generation.structure.config.StructureLayerGridDescription.Template;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;

import java.util.function.Supplier;


public class StructureGenerationAddon implements AddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<GenerationStage>>> STAGE_TYPE_KEY = new TypeKey<>() {
    };

    @Inject
    private Platform platform;

    @Inject
    private BaseAddon addon;

    @Override
    public void initialize() {
        platform.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(addon, ConfigPackPreLoadEvent.class)
            .then(event -> {
                event.getPack().applyLoader(StructureLayerGridDescription.class, Template::new);

                event.getPack()
                    .getOrCreateRegistry(STAGE_TYPE_KEY)
                    .register(addon.key("STRUCTURE"), StructureGenerationStageTemplate::new);
            })
            .failThrough();

//        platform.getEventManager()
//            .getHandler(FunctionalEventHandler.class)
//            .register(addon, ConfigurationLoadEvent.class)
//            .then(event -> {
//                if(event.is(Biome.class)) {
//                    event.getLoadedObject(Biome.class).getContext().put(event.load(new BiomeStructuresTemplate()).get());
//                }
//            })
//            .failThrough();

    }
}
