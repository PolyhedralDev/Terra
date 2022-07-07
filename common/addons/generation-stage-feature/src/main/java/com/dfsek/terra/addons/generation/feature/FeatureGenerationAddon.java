/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.generation.feature;

import com.dfsek.tectonic.api.config.template.dynamic.DynamicTemplate;
import com.dfsek.tectonic.api.config.template.dynamic.DynamicValue;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.dfsek.terra.addons.generation.feature.config.BiomeFeatures;
import com.dfsek.terra.addons.generation.feature.config.FeatureStageTemplate;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.structure.feature.Feature;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;


public class FeatureGenerationAddon implements AddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<GenerationStage>>> STAGE_TYPE_KEY = new TypeKey<>() {
    };
    
    public static final TypeKey<@Meta List<@Meta Feature>> FEATURE_LIST_TYPE_KEY = new TypeKey<>() {
    };
    @Inject
    private Platform platform;
    
    @Inject
    private BaseAddon addon;
    
    @SuppressWarnings("unchecked")
    @Override
    public void initialize() {
        PropertyKey<BiomeFeatures> biomeFeaturesKey = Context.create(BiomeFeatures.class);
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigPackPreLoadEvent.class)
                .then(event -> event.getPack()
                                    .getOrCreateRegistry(STAGE_TYPE_KEY)
                                    .register(addon.key("FEATURE"), () -> new FeatureStageTemplate(platform, biomeFeaturesKey)))
                .failThrough();
        
        platform.getEventManager()
                .getHandler(FunctionalEventHandler.class)
                .register(addon, ConfigurationLoadEvent.class)
                .then(event -> {
                    if(event.is(Biome.class)) {
                        DynamicTemplate.Builder templateBuilder = DynamicTemplate.builder();
                
                        List<FeatureGenerationStage> featureGenerationStages = new ArrayList<>();
                        event.getPack().getStages().forEach(stage -> {
                            if(stage instanceof FeatureGenerationStage featureGenerationStage) {
                                featureGenerationStages.add(featureGenerationStage);
                                templateBuilder
                                        .value(featureGenerationStage.getID(),
                                               DynamicValue
                                                       .builder("features." + featureGenerationStage.getID(), List.class)
                                                       .annotatedType(FEATURE_LIST_TYPE_KEY.getAnnotatedType())
                                                       .setDefault(Collections.emptyList())
                                                       .build());
                            }
                        });
                
                        DynamicTemplate template = event.load(templateBuilder.build());
                
                        Map<FeatureGenerationStage, List<Feature>> features = new HashMap<>();
                
                        featureGenerationStages.forEach(stage -> features.put(stage, template.get(stage.getID(), List.class)));
                
                        event.getLoadedObject(Biome.class).getContext().put(biomeFeaturesKey, new BiomeFeatures(features));
                    }
                })
                .failThrough();
    }
}
