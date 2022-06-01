/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.addon;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;

import java.util.Collections;
import java.util.Map;

import com.dfsek.terra.api.registry.key.Namespaced;
import com.dfsek.terra.api.registry.key.StringIdentifiable;


/**
 * Base interface which all Terra addons extend
 */
public interface BaseAddon extends StringIdentifiable, Namespaced {
    /**
     * Initializes the addon. To be implemented by addons, but never manually invoked.
     */
    default void initialize() { }
    
    /**
     * Gets the dependencies of this addon.
     *
     * @return Map of dependency ID to {@link VersionRange} of dependency
     */
    default Map<String, VersionRange> getDependencies() {
        return Collections.emptyMap();
    }
    
    /**
     * Get the version of the addon
     *
     * @return Version of addon
     */
    Version getVersion();
    
    default String getNamespace() {
        return getID();
    }
}
