package com.dfsek.terra.registry.master;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.dfsek.terra.addon.AddonClassLoader;
import com.dfsek.terra.addon.AddonPool;
import com.dfsek.terra.addon.PreLoadAddon;
import com.dfsek.terra.addon.exception.AddonLoadException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.inject.Injector;
import com.dfsek.terra.api.inject.exception.InjectionException;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.inject.InjectorImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;


public class AddonRegistry extends OpenRegistryImpl<TerraAddon> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AddonRegistry.class);
    
    private final TerraPlugin main;
    
    public AddonRegistry(TerraPlugin main) {
        this.main = main;
    }
    
    public AddonRegistry(TerraAddon addon, TerraPlugin main) {
        this.main = main;
        register(addon);
    }
    
    @Override
    public boolean register(String identifier, TerraAddon addon) {
        if(contains(identifier)) throw new IllegalArgumentException("Addon " + identifier + " is already registered.");
        addon.initialize();
        logger.info("Loaded addon {} v{}, by {}", addon.getName(), addon.getVersion(), addon.getAuthor());
        return super.register(identifier, addon);
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    public boolean register(TerraAddon addon) {
        return register(addon.getName(), addon);
    }
    
    public boolean loadAll() {
        return loadAll(TerraPlugin.class.getClassLoader());
    }
    
    @SuppressWarnings({ "NestedTryStatement", "ThrowCaughtLocally" })
    public boolean loadAll(ClassLoader parent) {
        InjectorImpl<TerraPlugin> pluginInjector = new InjectorImpl<>(main);
        pluginInjector.addExplicitTarget(TerraPlugin.class);
        
        boolean valid = true;
        File addonsFolder = new File(main.getDataFolder(), "addons");
        addonsFolder.mkdirs();
        
        AddonPool pool = new AddonPool();
        
        try {
            for(File jar : addonsFolder.listFiles(file -> file.getName().endsWith(".jar"))) {
                logger.info("Loading Addon(s) from: {}", jar.getName());
                for(Class<? extends TerraAddon> addonClass : AddonClassLoader.fetchAddonClasses(jar, parent)) {
                    pool.add(new PreLoadAddon(addonClass, jar));
                }
            }
            
            pool.buildAll();
            
            for(PreLoadAddon addon : pool.getAddons()) {
                Class<? extends TerraAddon> addonClass = addon.getAddonClass();
                Constructor<? extends TerraAddon> constructor;
                
                String logPrefix = "Terra:" + addon.getId();
                Logger addonLogger = Logger.getLogger(logPrefix);
                
                if(!LogManager.getLogManager().addLogger(addonLogger)) {
                    addonLogger = LogManager.getLogManager().getLogger(logPrefix);
                }
    
                // TODO: 2021-08-30 Remove logger injector entirely?
                Injector<Logger> loggerInjector = new InjectorImpl<>(addonLogger);
                loggerInjector.addExplicitTarget(Logger.class);
                
                try {
                    constructor = addonClass.getConstructor();
                } catch(NoSuchMethodException e) {
                    throw new AddonLoadException("Addon class has no valid constructor: " + addonClass.getCanonicalName(), e);
                }
                TerraAddon loadedAddon;
                try {
                    loadedAddon = constructor.newInstance();
                    pluginInjector.inject(loadedAddon);
                    loggerInjector.inject(loadedAddon);
                } catch(InstantiationException | IllegalAccessException | InvocationTargetException | InjectionException e) {
                    throw new AddonLoadException(String.format("Failed to load addon \"%s\"", addon.getId()), e);
                }
                try {
                    registerChecked(loadedAddon.getName(), loadedAddon);
                } catch(DuplicateEntryException e) {
                    valid = false;
                    logger.error("""
                                 Duplicate addon ID; addon with ID {} is already loaded.
                                 Existing addon class: {}
                                 Duplicate addon class: {}
                                 """,
                                 loadedAddon.getName(),
                                 get(loadedAddon.getName()).getClass().getCanonicalName(),
                                 addonClass.getCanonicalName());
                }
            }
        } catch(AddonLoadException | IOException e) {
            logger.error("Failed during addon loading", e);
            valid = false;
        }
        
        return valid;
    }
}
