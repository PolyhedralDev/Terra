package com.dfsek.terra.addons.chunkgenerator.layer.palette;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.terra.addons.chunkgenerator.palette.DoubleNavigableHolder;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;
import com.dfsek.terra.api.world.info.WorldProperties;


public class DotProductLayerPalette extends LayerPalette {
    
    private static final Logger logger = LoggerFactory.getLogger(DotProductLayerPalette.class);
    private final DoubleNavigableHolder<Palette> palettes;
    private final Vector3Int[] samplePoints;
    private final LayerSampler sampler;
    private final Vector3 direction;
    
    public DotProductLayerPalette(Group group, boolean resetsGroup,
                                  PointSet points, DoubleNavigableHolder<Palette> palettes, LayerSampler sampler, Vector3 direction) {
        super(group, resetsGroup);
        this.palettes = palettes;
        this.sampler = sampler;
        this.direction =  direction;
        this.samplePoints = points.toArray();
    }
    
    @Override
    public Palette get(int x, int y, int z, WorldProperties world, BiomeProvider biomeProvider) {
        Vector3.Mutable surfaceNormalApproximation = Vector3.Mutable.of(0, 0, 0);
        for(Vector3Int point : samplePoints) {
            double scalar = -sampler.sample(x+point.getX(), y+point.getY(), z+point.getZ(), world, biomeProvider);
            surfaceNormalApproximation.add(point.toVector3Mutable().multiply(scalar));
        }
        return palettes.get(direction.dot(surfaceNormalApproximation.normalize()));
    }
}
