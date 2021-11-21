/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.addon.bootstrap;

import java.nio.file.Path;

import com.dfsek.terra.api.addon.BaseAddon;


public interface BootstrapBaseAddon<T extends BaseAddon> extends BaseAddon {
    /**
     * Load all the relevant addons in the specified path.
     *
     * @param addonsFolder Path containing addons.
     * @param parent parent class loader
     *
     * @return Loaded addons
     */
    Iterable<T> loadAddons(Path addonsFolder, ClassLoader parent);
}
