package com.dfsek.terra.addons.biome.pipeline.image;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.biome.pipeline.image.config.ImageSourceTemplate;
import com.dfsek.terra.addons.biome.pipeline.image.config.converter.ClosestPipelineBiomeColorConverterTemplate;
import com.dfsek.terra.addons.biome.pipeline.image.config.converter.ExactPipelineBiomeColorConverterTemplate;
import com.dfsek.terra.addons.biome.pipeline.image.config.converter.mapping.DefinedPipelineBiomeColorMappingTemplate;
import com.dfsek.terra.addons.biome.pipeline.v2.BiomePipelineAddon;
import com.dfsek.terra.addons.biome.pipeline.v2.api.Source;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.DelegatedPipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.image.converter.ColorConverter;
import com.dfsek.terra.addons.image.converter.mapping.BiomeDefinedColorMapping;
import com.dfsek.terra.addons.image.converter.mapping.ColorMapping;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.biome.Biome;


public class PipelineImageAddon implements AddonInitializer {
    
    public static final TypeKey<Supplier<ObjectTemplate<ColorConverter<PipelineBiome>>>> PIPELINE_BIOME_COLOR_CONVERTER_REGISTRY_KEY =
            new TypeKey<>() {
            };
    
    public static final TypeKey<Supplier<ObjectTemplate<ColorMapping<PipelineBiome>>>> PIPELINE_BIOME_COLOR_MAPPING_REGISTRY_KEY =
            new TypeKey<>() {
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
                .priority(500)
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<ColorConverter<PipelineBiome>>>> biomeColorConverterRegistry =
                            event.getPack().getOrCreateRegistry(
                                    PIPELINE_BIOME_COLOR_CONVERTER_REGISTRY_KEY);
                    biomeColorConverterRegistry.register(addon.key("EXACT"), ExactPipelineBiomeColorConverterTemplate::new);
                    biomeColorConverterRegistry.register(addon.key("CLOSEST"), ClosestPipelineBiomeColorConverterTemplate::new);
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<Source>>> sourceRegistry = event.getPack().getOrCreateRegistry(
                            BiomePipelineAddon.SOURCE_REGISTRY_KEY);
                    sourceRegistry.register(addon.key("IMAGE"), ImageSourceTemplate::new);
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<ColorMapping<PipelineBiome>>>> biomeColorMappingRegistry =
                            event.getPack().getOrCreateRegistry(
                                    PIPELINE_BIOME_COLOR_MAPPING_REGISTRY_KEY);
                    biomeColorMappingRegistry.register(addon.key("USE_BIOME_COLORS"),
                                                       () -> () -> new BiomeDefinedColorMapping<>(event.getPack().getRegistry(Biome.class),
                                                                                                  DelegatedPipelineBiome::new));
                    biomeColorMappingRegistry.register(addon.key("MAP"), DefinedPipelineBiomeColorMappingTemplate::new);
                })
                .failThrough();
    }
}
