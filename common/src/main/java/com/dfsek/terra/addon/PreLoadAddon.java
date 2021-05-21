package com.dfsek.terra.addon;

import com.dfsek.terra.addon.exception.AddonLoadException;
import com.dfsek.terra.addon.exception.CircularDependencyException;
import com.dfsek.terra.addon.exception.DependencyMissingException;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.addons.annotations.Addon;
import com.dfsek.terra.api.addons.annotations.Depends;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreLoadAddon {
    private final List<PreLoadAddon> depends = new ArrayList<>();
    private final Class<? extends TerraAddon> addonClass;
    private final String id;
    private final String[] dependencies;
    private final File file;

    public PreLoadAddon(Class<? extends TerraAddon> addonClass, File file) {
        this.addonClass = addonClass;
        this.id = addonClass.getAnnotation(Addon.class).value();
        this.file = file;
        Depends depends = addonClass.getAnnotation(Depends.class);
        this.dependencies = depends == null ? new String[] {} : depends.value();
    }

    public List<PreLoadAddon> getDepends() {
        return depends;
    }

    public void rebuildDependencies(AddonPool pool, PreLoadAddon origin, boolean levelG1) throws AddonLoadException {
        if(this.equals(origin) && !levelG1)
            throw new CircularDependencyException("Detected circular dependency in addon \"" + id + "\", dependencies: " + Arrays.toString(dependencies));

        for(String dependency : dependencies) {
            PreLoadAddon preLoadAddon = pool.get(dependency);
            if(preLoadAddon == null)
                throw new DependencyMissingException("Dependency " + dependency + " was not found. Please install " + dependency + " to use " + id + ".");
            depends.add(preLoadAddon);
            preLoadAddon.rebuildDependencies(pool, origin, false);
        }
    }

    public String getId() {
        return id;
    }

    public Class<? extends TerraAddon> getAddonClass() {
        return addonClass;
    }

    public File getFile() {
        return file;
    }
}
