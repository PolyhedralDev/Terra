/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.addon.bootstrap;

import java.nio.file.Path;

import com.dfsek.terra.api.addon.BaseAddon;


/**
 * Interface representing a bootstrap addon.
 * <p>
 * A bootstrap addon is the only type of addon Terra implements a loader for.
 * It is a minimal base for addon loaders to be implemented on top of.
 * <p>
 * Unless you are writing your own addon loader, you will want to depend on the
 * {@code manifest-addon-loader} addon, and implement its AddonInitializer.
 *
 * @param <T> Type of addon this bootstrap addon loads
 */
public interface BootstrapBaseAddon<T extends BaseAddon> extends BaseAddon {
    /**
     * Load all the relevant addons in the specified path.
     *
     * @param addonsFolder Path containing addons.
     * @param parent       parent class loader
     *
     * @return Loaded addons
     */
    Iterable<T> loadAddons(Path addonsFolder, BootstrapAddonClassLoader parent);
}
