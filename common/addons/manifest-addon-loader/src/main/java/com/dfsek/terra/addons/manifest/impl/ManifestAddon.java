package com.dfsek.terra.addons.manifest.impl;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.manifest.impl.config.AddonManifest;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.inject.Injector;
import com.dfsek.terra.api.inject.annotations.Inject;

import java.util.List;


public class ManifestAddon implements BaseAddon {
    private final AddonManifest manifest;
    
    private final List<AddonInitializer> initializers;
    
    @Inject
    private Platform platform;
    
    public ManifestAddon(AddonManifest manifest, List<AddonInitializer> initializers) {
        this.manifest = manifest;
        this.initializers = initializers;
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
        
        platform.logger().info("Initializing addon " + getID());
        
        initializers.forEach(initializer -> {
            addonInjector.inject(initializer);
            platformInjector.inject(initializer);
            initializer.initialize();
        });
    }
}
