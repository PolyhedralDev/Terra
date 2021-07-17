package com.dfsek.terra.addons.flora;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.flora.flora.FloraLayer;

import java.util.Collections;
import java.util.List;

public class BiomeFloraTemplate implements ObjectTemplate<BiomeFlora> {
    @Value("flora")
    @Default
    private List<FloraLayer> flora = Collections.emptyList();

    @Override
    public BiomeFlora get() {
        return new BiomeFlora(flora);
    }
}
