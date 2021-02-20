package com.dfsek.terra.registry;

import com.dfsek.terra.addons.addon.TerraAddon;
import com.dfsek.terra.addons.annotations.Addon;
import com.dfsek.terra.addons.annotations.Depends;
import com.dfsek.terra.addons.loading.AddonClassLoader;
import com.dfsek.terra.addons.loading.AddonLoadException;
import com.dfsek.terra.api.core.TerraPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddonRegistry extends TerraRegistry<TerraAddon> {
    private final TerraPlugin main;

    public AddonRegistry(TerraPlugin main) {

        this.main = main;
    }

    public AddonRegistry(TerraAddon addon, TerraPlugin main) {
        this.main = main;
        add(addon.getName(), addon);
    }

    @Override
    public boolean add(String name, TerraAddon addon) {
        addon.initialize();
        main.getLogger().info("Loaded addon " + addon.getName() + " v" + addon.getVersion() + ", by " + addon.getAuthor());
        return super.add(name, addon);
    }

    public boolean loadAll() {
        boolean valid = true;
        File addonsFolder = new File(main.getDataFolder(), "addons");
        addonsFolder.mkdirs();

        Map<String, Class<? extends TerraAddon>> addonIDs = new HashMap<>();

        try {
            for(File jar : addonsFolder.listFiles(file -> file.getName().endsWith(".jar"))) {

                main.getLogger().info("Loading Addon(s) from: " + jar.getName());

                Set<Class<? extends TerraAddon>> addonClasses = AddonClassLoader.fetchAddonClasses(jar);

                for(Class<? extends TerraAddon> addonClass : addonClasses) {
                    String id = addonClass.getAnnotation(Addon.class).value();
                    if(addonIDs.containsKey(id))
                        throw new AddonLoadException("Duplicate addon ID: " + id);
                    addonIDs.put(id, addonClass);
                }
            }

            for(Map.Entry<String, Class<? extends TerraAddon>> entry : addonIDs.entrySet()) {
                Class<? extends TerraAddon> addonClass = entry.getValue();

                Depends dependencies = addonClass.getAnnotation(Depends.class);

                if(dependencies != null) {
                    for(String dependency : dependencies.value()) {
                        if(!addonIDs.containsKey(dependency))
                            throw new AddonLoadException("Addon " + entry.getKey() + " specifies dependency " + dependency + ", which is not loaded. Please install " + dependency + " to use " + entry.getKey());
                    }
                }

                Constructor<? extends TerraAddon> constructor;
                try {
                    constructor = addonClass.getConstructor();
                } catch(NoSuchMethodException e) {
                    throw new AddonLoadException("Addon class has no valid constructor: " + addonClass.getCanonicalName(), e);
                }
                TerraAddon addon;
                try {
                    addon = constructor.newInstance();
                } catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new AddonLoadException("Failed to instantiate addon: " + addonClass.getCanonicalName(), e);
                }
                try {
                    addChecked(addon.getName(), addon);
                } catch(IllegalArgumentException e) {
                    throw new AddonLoadException("Duplicate addon ID; addon with ID " + addon.getName() + " is already loaded.");
                }
            }
        } catch(IOException | AddonLoadException e) {
            e.printStackTrace();
            valid = false;
            main.getLogger().severe("Addons failed to load. Please ensure all addons are properly installed.");
        }

        return valid;
    }
}
