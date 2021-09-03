package com.dfsek.terra.addons.generation.flora;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.Collections;
import java.util.List;

import com.dfsek.terra.api.config.meta.Meta;


public class BiomeFloraTemplate implements ObjectTemplate<BiomeFlora> {
    @Value("flora")
    @Default
    private final @Meta List<@Meta FloraLayer> flora = Collections.emptyList();
    
    @Override
    public BiomeFlora get() {
        return new BiomeFlora(flora);
    }
}
