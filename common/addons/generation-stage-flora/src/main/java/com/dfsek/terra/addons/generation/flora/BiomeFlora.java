package com.dfsek.terra.addons.generation.flora;

import java.util.List;

import com.dfsek.terra.api.properties.Properties;


public class BiomeFlora implements Properties {
    private final List<FloraLayer> layers;
    
    public BiomeFlora(List<FloraLayer> layers) {
        this.layers = layers;
    }
    
    public List<FloraLayer> getLayers() {
        return layers;
    }
}
