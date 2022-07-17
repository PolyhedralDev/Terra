package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.List;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.addons.chunkgenerator.layer.predicate.SamplerLayerPredicate.Operator;
import com.dfsek.terra.api.world.biome.Biome;


public class SamplerListLayerPredicate implements LayerPredicate {
    
    private final List<CoordinateTest> tests;
    private final Operator operator;
    private final LayerSampler sampler;
    
    public SamplerListLayerPredicate(LayerSampler sampler, Operator operator, List<CoordinateTest> tests) {
        this.sampler = sampler;
        this.operator = operator;
        this.tests = tests;
    }
    
    @Override
    public boolean test(long seed, Biome biome, int x, int y, int z) {
        for (CoordinateTest test : tests) {
            if (operator.evaluate(sampler.sample(seed, biome, x + test.x, y + test.y, z + test.z), test.threshold)) return true;
        }
        return false;
    }
    
    public record CoordinateTest(int x, int y, int z, double threshold) {
        
        public static class Template implements ObjectTemplate<CoordinateTest> {
    
            @Value("x")
            private int x;
            
            @Value("y")
            private int y;
            
            @Value("z")
            private int z;
            
            @Value("threshold")
            @Default
            private double threshold = 0;
            
            @Override
            public CoordinateTest get() {
                return new CoordinateTest(x, y, z, threshold);
            }
        }
    }
}
