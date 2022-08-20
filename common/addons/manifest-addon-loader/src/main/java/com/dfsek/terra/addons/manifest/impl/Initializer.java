package com.dfsek.terra.addons.manifest.impl;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.manifest.api.MonadAddonInitializer;
import com.dfsek.terra.addons.manifest.api.monad.Init;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.inject.Injector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class Initializer {
    private Initializer() {
    
    }
    
    public static Initializer of(MonadAddonInitializer addon) {
        return new MonadInitializer(addon);
    }
    
    public static Initializer of(AddonInitializer addonInitializer) {
        return new BasicInitializer(addonInitializer);
    }
    
    public abstract void initialize(InitInfo info);
    
    private static class MonadInitializer extends Initializer {
        private final MonadAddonInitializer addon;
        
        private MonadInitializer(MonadAddonInitializer addon) {
            this.addon = addon;
        }
        
        @Override
        public void initialize(InitInfo info) {
            ((Init<?>) addon.initialize()).apply(info);
        }
    }
    
    
    private static class BasicInitializer extends Initializer {
        private final AddonInitializer initializer;
        
        private BasicInitializer(AddonInitializer initializer) {
            this.initializer = initializer;
        }
    
        @Override
        public void initialize(InitInfo info) {
            Injector<BaseAddon> addonInjector = Injector.get(info.addon());
            addonInjector.addExplicitTarget(BaseAddon.class);
    
            Injector<Platform> platformInjector = Injector.get(info.platform());
            platformInjector.addExplicitTarget(Platform.class);
    
            Injector<Logger> loggerInjector = Injector.get(LoggerFactory.getLogger(initializer.getClass()));
            loggerInjector.addExplicitTarget(Logger.class);
            addonInjector.inject(initializer);
            platformInjector.inject(initializer);
            loggerInjector.inject(initializer);
            initializer.initialize();
        }
    }
}
