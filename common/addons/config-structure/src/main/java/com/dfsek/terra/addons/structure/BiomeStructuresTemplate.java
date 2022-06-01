/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.structure;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.Collections;
import java.util.Set;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;


@SuppressWarnings("FieldMayBeFinal")
public class BiomeStructuresTemplate implements ObjectTemplate<BiomeStructures> {
    @Value("structures")
    @Default
    private @Meta Set<@Meta ConfiguredStructure> structures = Collections.emptySet();
    
    @Override
    public BiomeStructures get() {
        return new BiomeStructures(structures);
    }
}
