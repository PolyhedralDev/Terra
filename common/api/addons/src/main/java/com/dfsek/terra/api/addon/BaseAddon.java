/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.addon;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;

import com.dfsek.terra.api.util.StringIdentifiable;

import java.util.Collections;
import java.util.Map;


public interface BaseAddon extends StringIdentifiable {
    default void initialize() { }
    
    default Map<String, VersionRange> getDependencies() {
        return Collections.emptyMap();
    }
    
    Version getVersion();
}
