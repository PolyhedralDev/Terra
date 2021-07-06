package com.dfsek.terra.registry.master;

import com.dfsek.terra.addon.AddonClassLoader;
import com.dfsek.terra.addon.AddonPool;
import com.dfsek.terra.addon.PreLoadAddon;
import com.dfsek.terra.addon.exception.AddonLoadException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.inject.InjectorImpl;
import com.dfsek.terra.api.injection.exception.InjectionException;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.registry.OpenRegistryImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AddonRegistry extends OpenRegistryImpl<TerraAddon> {
    private final TerraPlugin main;

    public AddonRegistry(TerraPlugin main) {
        this.main = main;
    }

    public AddonRegistry(TerraAddon addon, TerraPlugin main) {
        this.main = main;
        add(addon.getName(), addon);
    }

    @Override
    public boolean add(String identifier, TerraAddon addon) {
        if(contains(identifier)) throw new IllegalArgumentException("Addon " + identifier + " is already registered.");
        addon.initialize();
        main.logger().info("Loaded addon " + addon.getName() + " v" + addon.getVersion() + ", by " + addon.getAuthor());
        return super.add(identifier, addon);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean loadAll() {
        InjectorImpl<TerraPlugin> pluginInjector = new InjectorImpl<>(main);
        pluginInjector.addExplicitTarget(TerraPlugin.class);

        boolean valid = true;
        File addonsFolder = new File(main.getDataFolder(), "addons");
        addonsFolder.mkdirs();

        AddonPool pool = new AddonPool();

        try {
            for(File jar : addonsFolder.listFiles(file -> file.getName().endsWith(".jar"))) {
                main.logger().info("Loading Addon(s) from: " + jar.getName());
                for(Class<? extends TerraAddon> addonClass : AddonClassLoader.fetchAddonClasses(jar)) {
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

                InjectorImpl<Logger> loggerInjector = new InjectorImpl<>(addonLogger);
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
                    throw new AddonLoadException("Failed to load addon \" + " + addon.getId() + "\": ", e);
                }
                try {
                    addChecked(loadedAddon.getName(), loadedAddon);
                } catch(DuplicateEntryException e) {
                    valid = false;
                    main.logger().severe("Duplicate addon ID; addon with ID " + loadedAddon.getName() + " is already loaded.");
                    main.logger().severe("Existing addon class: " + get(loadedAddon.getName()).getClass().getCanonicalName());
                    main.logger().severe("Duplicate addon class: " + addonClass.getCanonicalName());
                }
            }
        } catch(AddonLoadException | IOException e) {
            e.printStackTrace();
            valid = false;
        }

        return valid;
    }
}
