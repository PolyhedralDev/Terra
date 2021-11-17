package com.dfsek.terra.addon.loader;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.addon.bootstrap.BootstrapBaseAddon;

import java.nio.file.Path;
import java.util.Collections;


public class ApiAddonLoader implements BootstrapBaseAddon<BaseAddon> {
    @Override
    public Iterable<BaseAddon> loadAddons(Path addonsFolder, ClassLoader parent) {
        return Collections.emptySet();
    }
    
    @Override
    public String getID() {
        return "API";
    }
}
