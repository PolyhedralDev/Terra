/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;


@SuppressWarnings({ "unused", "FieldMayBeFinal" })
public class ScatteredOreTemplate extends OreTemplate {
    @Value("size")
    private @Meta int spread = 7;
    
    
    public int getSpread() {
        return spread;
    }
}
