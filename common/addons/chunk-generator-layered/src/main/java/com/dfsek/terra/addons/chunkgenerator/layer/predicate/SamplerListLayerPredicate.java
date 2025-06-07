package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.math.RelationalOperator;
import com.dfsek.terra.addons.chunkgenerator.math.pointset.PointSet;
import com.dfsek.seismic.type.vector.Vector3Int;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class SamplerListLayerPredicate implements LayerPredicate {
    
    private final Vector3Int[] points;
    private final RelationalOperator operator;
    private final LayerSampler sampler;
    private final double threshold;
    
    public SamplerListLayerPredicate(LayerSampler sampler, double threshold, RelationalOperator operator, PointSet points) {
        this.sampler = sampler;
        this.threshold = threshold;
        this.operator = operator;
        this.points = points.toArray();
    }
    
    @Override
    public boolean test(int x, int y, int z, WorldProperties worldProperties, BiomeProvider biomeProvider) {
        for (Vector3Int point : points) {
            if (operator.test(sampler.sample(x + point.getX(), y + point.getY(), z + point.getZ(), worldProperties, biomeProvider), threshold)) return true;
        }
        return false;
    }
}
