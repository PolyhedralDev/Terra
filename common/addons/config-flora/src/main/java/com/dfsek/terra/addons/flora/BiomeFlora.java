package com.dfsek.terra.addons.flora;

import com.dfsek.terra.addons.flora.flora.FloraLayer;
import com.dfsek.terra.api.properties.Properties;

import java.util.List;

public class BiomeFlora implements Properties {
    private final List<FloraLayer> layers;

    public BiomeFlora(List<FloraLayer> layers) {
        this.layers = layers;
    }

    public List<FloraLayer> getLayers() {
        return layers;
    }
}
