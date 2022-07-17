package com.dfsek.terra.addons.chunkgenerator.layer.predicate;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPredicate;
import com.dfsek.terra.addons.chunkgenerator.api.LayerSampler;
import com.dfsek.terra.api.world.biome.Biome;


public class SamplerLayerPredicate implements LayerPredicate {
    
    private final LayerSampler sampler;
    
    private final double threshold;
    
    private final Operator operator;
    
    public SamplerLayerPredicate(LayerSampler sampler, Operator operator, double threshold) {
        this.sampler = sampler;
        this.operator = operator;
        this.threshold = threshold;
    }
    
    @Override
    public boolean test(long seed, Biome biome, int x, int y, int z) {
        return operator.evaluate(sampler.sample(seed, biome, x, y, z), threshold);
    }
    
    public enum Operator {
        GreaterThan {
            @Override
            public boolean evaluate(double a, double b) {
                return a > b;
            }
        },
        GreaterThanOrEqual {
            @Override
            public boolean evaluate(double a, double b) {
                return a >= b;
            }
        },
        LessThan {
            @Override
            public boolean evaluate(double a, double b) {
                return a < b;
            }
        },
        LessThanOrEqual {
            @Override
            public boolean evaluate(double a, double b) {
                return a <= b;
            }
        },
        Equals {
            @Override
            public boolean evaluate(double a, double b) {
                return a == b;
            }
        },
        NotEquals {
            @Override
            public boolean evaluate(double a, double b) {
                return a != b;
            }
        };
        
        public abstract boolean evaluate(double a, double b);
    }
}
