/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.feature.locator;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.function.Supplier;

import com.dfsek.terra.addons.feature.locator.config.AdjacentPatternLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.AndLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.GaussianRandomLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.OrLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.PatternLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.RandomLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.Sampler3DLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.SamplerLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.SurfaceLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.TopLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.XorLocatorTemplate;
import com.dfsek.terra.addons.feature.locator.config.pattern.AirMatchPatternTemplate;
import com.dfsek.terra.addons.feature.locator.config.pattern.AndPatternTemplate;
import com.dfsek.terra.addons.feature.locator.config.pattern.BlockSetMatchPatternTemplate;
import com.dfsek.terra.addons.feature.locator.config.pattern.NotPatternTemplate;
import com.dfsek.terra.addons.feature.locator.config.pattern.OrPatternTemplate;
import com.dfsek.terra.addons.feature.locator.config.pattern.SingleBlockMatchPatternTemplate;
import com.dfsek.terra.addons.feature.locator.config.pattern.SolidMatchPatternTemplate;
import com.dfsek.terra.addons.feature.locator.config.pattern.XorPatternTemplate;
import com.dfsek.terra.addons.feature.locator.patterns.Pattern;
import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.inject.annotations.Inject;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class LocatorAddon implements AddonInitializer {
    
    public static final TypeKey<Supplier<ObjectTemplate<Locator>>> LOCATOR_TOKEN = new TypeKey<>() {
    };
    public static final TypeKey<Supplier<ObjectTemplate<Pattern>>> PATTERN_TOKEN = new TypeKey<>() {
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
                    CheckedRegistry<Supplier<ObjectTemplate<Locator>>> locatorRegistry = event.getPack().getOrCreateRegistry(LOCATOR_TOKEN);
                    locatorRegistry.register(addon.key("SURFACE"), SurfaceLocatorTemplate::new);
                    locatorRegistry.register(addon.key("TOP"), TopLocatorTemplate::new);
            
                    locatorRegistry.register(addon.key("RANDOM"), RandomLocatorTemplate::new);
                    locatorRegistry.register(addon.key("GAUSSIAN_RANDOM"), GaussianRandomLocatorTemplate::new);
            
                    locatorRegistry.register(addon.key("PATTERN"), PatternLocatorTemplate::new);
                    locatorRegistry.register(addon.key("ADJACENT_PATTERN"), AdjacentPatternLocatorTemplate::new);
            
                    locatorRegistry.register(addon.key("SAMPLER"), SamplerLocatorTemplate::new);
                    locatorRegistry.register(addon.key("SAMPLER_3D"), Sampler3DLocatorTemplate::new);
            
                    locatorRegistry.register(addon.key("AND"), AndLocatorTemplate::new);
                    locatorRegistry.register(addon.key("OR"), OrLocatorTemplate::new);
                    locatorRegistry.register(addon.key("XOR"), XorLocatorTemplate::new);
                })
                .then(event -> {
                    CheckedRegistry<Supplier<ObjectTemplate<Pattern>>> patternRegistry = event.getPack().getOrCreateRegistry(PATTERN_TOKEN);
                    patternRegistry.register(addon.key("MATCH_AIR"), AirMatchPatternTemplate::new);
                    patternRegistry.register(addon.key("MATCH_SOLID"), SolidMatchPatternTemplate::new);
                    patternRegistry.register(addon.key("MATCH"), SingleBlockMatchPatternTemplate::new);
                    patternRegistry.register(addon.key("MATCH_SET"), BlockSetMatchPatternTemplate::new);
            
                    patternRegistry.register(addon.key("AND"), AndPatternTemplate::new);
                    patternRegistry.register(addon.key("OR"), OrPatternTemplate::new);
                    patternRegistry.register(addon.key("XOR"), XorPatternTemplate::new);
                    patternRegistry.register(addon.key("NOT"), NotPatternTemplate::new);
                })
                .failThrough();
    }
}
