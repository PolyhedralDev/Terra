/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.palette;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Final;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.List;

import com.dfsek.terra.addons.palette.palette.PaletteLayerHolder;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class PaletteTemplate implements AbstractableTemplate {
    @Value("sampler")
    @Default
    private @Meta NoiseSampler noise = NoiseSampler.zero();
    
    @Value("id")
    @Final
    private String id;
    
    @Value("layers")
    private @Meta List<@Meta PaletteLayerHolder> palette;
    
    public String getID() {
        return id;
    }
    
    public List<PaletteLayerHolder> getPalette() {
        return palette;
    }
    
    public NoiseSampler getNoise() {
        return noise;
    }
}
