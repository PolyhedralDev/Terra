package com.dfsek.terra.api.addon.bootstrap;

import com.dfsek.terra.api.addon.AddonEntryPoint;

import java.nio.file.Path;


public interface BootstrapAddon extends AddonEntryPoint {
    /**
     * Load all the relevant addons in the specified path.
     * @param addonsFolder Path containing addons.
     * @param parent
     * @return Loaded addons
     */
    Iterable<AddonEntryPoint> loadAddons(Path addonsFolder, ClassLoader parent);
}
