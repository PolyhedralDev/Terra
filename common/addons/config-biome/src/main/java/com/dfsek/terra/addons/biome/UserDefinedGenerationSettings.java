package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.GenerationSettings;


public class UserDefinedGenerationSettings implements GenerationSettings {
    
    private final NoiseSampler noise;
    private final NoiseSampler elevation;
    private final NoiseSampler carving;
    
    private final NoiseSampler biomeNoise;
    private final double elevationWeight;
    private final int blendDistance;
    private final int blendStep;
    private final double blendWeight;
    
    public UserDefinedGenerationSettings(NoiseSampler noise, NoiseSampler elevation, NoiseSampler carving, NoiseSampler biomeNoise,
                                         double elevationWeight, int blendDistance, int blendStep, double blendWeight) {
        this.noise = noise;
        this.elevation = elevation;
        this.carving = carving;
        
        this.biomeNoise = biomeNoise;
        this.elevationWeight = elevationWeight;
        this.blendDistance = blendDistance;
        this.blendStep = blendStep;
        this.blendWeight = blendWeight;
    }
    
    @Override
    public NoiseSampler getBaseSampler() {
        return noise;
    }
    
    @Override
    public NoiseSampler getElevationSampler() {
        return elevation;
    }
    
    @Override
    public NoiseSampler getCarver() {
        return carving;
    }
    
    @Override
    public int getBlendDistance() {
        return blendDistance;
    }
    
    @Override
    public double getWeight() {
        return blendWeight;
    }
    
    @Override
    public NoiseSampler getBiomeNoise() {
        return biomeNoise;
    }
    
    @Override
    public double getElevationWeight() {
        return elevationWeight;
    }
    
    @Override
    public int getBlendStep() {
        return blendStep;
    }
}
