package com.dfsek.terra.registry;

import com.dfsek.terra.addons.addon.TerraAddon;
import com.dfsek.terra.addons.loading.AddonClassLoader;
import com.dfsek.terra.addons.loading.AddonLoadException;
import com.dfsek.terra.api.core.TerraPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class AddonRegistry extends TerraRegistry<TerraAddon> {
    public boolean loadAll(TerraPlugin main) {
        boolean valid = true;
        File addonsFolder = new File(main.getDataFolder(), "addons");
        addonsFolder.mkdirs();
        for(File jar : addonsFolder.listFiles(file -> file.getName().endsWith(".jar"))) {
            try {
                main.getLogger().info("Loading Addon(s) from: " + jar.getName());
                load(jar, main);
            } catch(IOException | AddonLoadException e) {
                e.printStackTrace();
                valid = false;
            }
        }
        return valid;
    }

    public void load(File file, TerraPlugin main) throws AddonLoadException, IOException {
        Set<Class<? extends TerraAddon>> addonClasses = AddonClassLoader.fetchAddonClasses(file);

        for(Class<? extends TerraAddon> addonClass : addonClasses) {
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
            main.getLogger().info("Loaded addon " + addon.getName() + " v" + addon.getVersion() + ", by " + addon.getAuthor());
            addon.initialize();
        }
    }
}
