/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.manifest.impl;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.manifest.impl.config.AddonManifest;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.inject.Injector;
import com.dfsek.terra.api.inject.annotations.Inject;


public class ManifestAddon implements BaseAddon {
    private static final Logger logger = LoggerFactory.getLogger(ManifestAddon.class);
    private final AddonManifest manifest;
    private final List<AddonInitializer> initializers;
    @Inject
    private Platform platform;
    
    public ManifestAddon(AddonManifest manifest, List<AddonInitializer> initializers) {
        this.manifest = manifest;
        this.initializers = initializers;
    }
    
    public AddonManifest getManifest() {
        return manifest;
    }
    
    @Override
    public String getID() {
        return manifest.getID();
    }
    
    public void initialize() {
        Injector<BaseAddon> addonInjector = Injector.get(this);
        addonInjector.addExplicitTarget(BaseAddon.class);
        
        Injector<Platform> platformInjector = Injector.get(platform);
        platformInjector.addExplicitTarget(Platform.class);
        
        logger.debug("Initializing addon {}", getID());
        
        initializers.forEach(initializer -> {
            Injector<Logger> loggerInjector = Injector.get(LoggerFactory.getLogger(initializer.getClass()));
            loggerInjector.addExplicitTarget(Logger.class);
            
            logger.debug("Invoking entry point {}", initializer.getClass());
            addonInjector.inject(initializer);
            platformInjector.inject(initializer);
            loggerInjector.inject(initializer);
            initializer.initialize();
        });
    }
    
    @Override
    public Map<String, VersionRange> getDependencies() {
        return manifest.getDependencies();
    }
    
    @Override
    public Version getVersion() {
        return manifest.getVersion();
    }
}
