package com.dfsek.terra.api.addon.bootstrap;

import java.nio.file.Path;

import com.dfsek.terra.api.addon.BaseAddon;


public interface BootstrapBaseAddon<T extends BaseAddon> extends BaseAddon {
    /**
     * Load all the relevant addons in the specified path.
     *
     * @param addonsFolder Path containing addons.
     * @param parent
     *
     * @return Loaded addons
     */
    Iterable<T> loadAddons(Path addonsFolder, ClassLoader parent);
}
