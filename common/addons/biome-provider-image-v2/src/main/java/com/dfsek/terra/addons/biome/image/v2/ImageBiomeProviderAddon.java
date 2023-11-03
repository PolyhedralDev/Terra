/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.image.v2;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.biome.image.v2.config.ImageProviderTemplate;
import com.dfsek.terra.addons.biome.image.v2.config.converter.ClosestBiomeColorConverterTemplate;
import com.dfsek.terra.addons.biome.image.v2.config.converter.ExactBiomeColorConverterTemplate;
import com.dfsek.terra.addons.biome.image.v2.config.converter.mapping.DefinedBiomeColorMappingTemplate;
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
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class ImageBiomeProviderAddon implements AddonInitializer {
    public static final TypeKey<Supplier<ObjectTemplate<BiomeProvider>>> PROVIDER_REGISTRY_KEY = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<ColorConverter<Biome>>>> BIOME_COLOR_CONVERTER_REGISTRY_KEY = new TypeKey<>() {
    };
    
    public static final TypeKey<Supplier<ObjectTemplate<ColorMapping<Biome>>>> BIOME_COLOR_MAPPING_REGISTRY_KEY = new TypeKey<>() {
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
                .priority(501)
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<BiomeProvider>>> providerRegistry = event.getPack().getOrCreateRegistry(
                            PROVIDER_REGISTRY_KEY);
                    providerRegistry.register(addon.key("IMAGE"), ImageProviderTemplate::new);
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<ColorConverter<Biome>>>> biomeColorConverterRegistry = event.getPack().getOrCreateRegistry(
                            BIOME_COLOR_CONVERTER_REGISTRY_KEY);
                    biomeColorConverterRegistry.register(addon.key("EXACT"), ExactBiomeColorConverterTemplate::new);
                    biomeColorConverterRegistry.register(addon.key("CLOSEST"), ClosestBiomeColorConverterTemplate::new);
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<ColorMapping<Biome>>>> biomeColorMappingRegistry = event.getPack().getOrCreateRegistry(
                            BIOME_COLOR_MAPPING_REGISTRY_KEY);
                    biomeColorMappingRegistry.register(addon.key("USE_BIOME_COLORS"), () -> () -> new BiomeDefinedColorMapping<>(event.getPack().getRegistry(Biome.class), b -> b));
                    biomeColorMappingRegistry.register(addon.key("MAP"), DefinedBiomeColorMappingTemplate::new);
                })
                .failThrough();
    }
}
