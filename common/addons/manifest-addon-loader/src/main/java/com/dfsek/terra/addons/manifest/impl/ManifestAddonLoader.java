package com.dfsek.terra.addons.manifest.impl;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.addon.bootstrap.BootstrapBaseAddon;

import java.nio.file.Path;
import java.util.Collections;


public class ManifestAddonLoader implements BootstrapBaseAddon {
    @Override
    public Iterable<BaseAddon> loadAddons(Path addonsFolder, ClassLoader parent) {
        return Collections.emptySet();
    }
    
    @Override
    public String getID() {
        return "MANIFEST";
    }
}
