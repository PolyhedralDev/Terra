package com.dfsek.terra.addons.flora;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.addons.flora.flora.FloraLayer;

import java.util.Collections;
import java.util.List;

public class BiomeFloraTemplate implements ConfigTemplate {
    @Value("flora")
    @Default
    private List<FloraLayer> flora = Collections.emptyList();

    public List<FloraLayer> getFlora() {
        return flora;
    }
}
