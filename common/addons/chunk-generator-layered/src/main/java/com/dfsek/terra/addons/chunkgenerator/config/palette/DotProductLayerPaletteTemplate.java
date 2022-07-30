package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.HashMap;
import java.util.Map;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.palette.DotProductLayerPalette;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.generative.AdjacentPointSet;
import com.dfsek.terra.addons.chunkgenerator.palette.DoubleNavigableHolder;
import com.dfsek.terra.addons.chunkgenerator.util.InstanceWrapper;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class DotProductLayerPaletteTemplate extends LayerPaletteTemplate {
    
    @Value("normal.approximation-points")
    @Default
    private PointSet normalApproximationPoints = new AdjacentPointSet();
    
    @Value("normal.direction")
    @Default
    private Vector3 direction = Vector3.of(0, 1, 0);
    
    @Value("normal.sampler")
    private InstanceWrapper<LayerSampler> sampler;
    
    @Value("palettes")
    private Map<String, Palette> palettes;
    
    @Override
    public LayerPalette get() {
        Map<Double, Palette> paletteMap = new HashMap<>();
        palettes.forEach((s, p) -> {
            paletteMap.put(Double.parseDouble(s), p);
        });
        return new DotProductLayerPalette(group, resetsGroup, normalApproximationPoints, new DoubleNavigableHolder<>(paletteMap), sampler.get(), direction);
    }
}
