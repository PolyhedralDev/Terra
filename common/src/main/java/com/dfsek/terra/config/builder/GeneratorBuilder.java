package com.dfsek.terra.config.builder;

import com.dfsek.terra.api.math.noise.samplers.ConstantSampler;
import com.dfsek.terra.api.math.noise.samplers.ExpressionSampler;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.generation.config.WorldGenerator;
import parsii.eval.Scope;
import parsii.tokenizer.ParseException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GeneratorBuilder {
    private final Map<Long, WorldGenerator> gens = Collections.synchronizedMap(new HashMap<>());

    private String noiseEquation;

    private String elevationEquation;

    private String carvingEquation;

    private Scope varScope;

    private Map<String, NoiseBuilder> noiseBuilderMap;

    private PaletteHolder palettes;

    private PaletteHolder slantPalettes;

    private boolean preventInterpolation;

    private boolean interpolateElevation;

    private boolean noise2d;

    private double base;

    private NoiseBuilder biomeNoise;

    private double elevationWeight;

    private int blendDistance;

    private int blendStep;

    private double blendWeight;

    public WorldGenerator build(long seed) {
        synchronized(gens) {
            return gens.computeIfAbsent(seed, k -> {
                NoiseSampler noise;
                NoiseSampler elevation;
                NoiseSampler carving;
                try {
                    noise = new ExpressionSampler(noiseEquation, varScope, seed, noiseBuilderMap);
                    elevation = elevationEquation == null ? new ConstantSampler(0) : new ExpressionSampler(elevationEquation, varScope, seed, noiseBuilderMap);
                    carving = new ExpressionSampler(carvingEquation, varScope, seed, noiseBuilderMap);
                } catch(ParseException e) {
                    throw new RuntimeException(e);
                }
                return new WorldGenerator(palettes, slantPalettes, noise, elevation, carving, noise2d, base, biomeNoise.build((int) seed), elevationWeight, blendDistance, blendStep, blendWeight);
            });
        }
    }

    public void setBlendWeight(double blendWeight) {
        this.blendWeight = blendWeight;
    }

    public void setBlendStep(int blendStep) {
        this.blendStep = blendStep;
    }

    public void setBlendDistance(int blendDistance) {
        this.blendDistance = blendDistance;
    }

    public void setBiomeNoise(NoiseBuilder biomeNoise) {
        this.biomeNoise = biomeNoise;
    }

    public boolean isNoise2d() {
        return noise2d;
    }

    public void setNoise2d(boolean noise2d) {
        this.noise2d = noise2d;
    }

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public void setElevationWeight(double elevationWeight) {
        this.elevationWeight = elevationWeight;
    }

    public String getNoiseEquation() {
        return noiseEquation;
    }

    public void setNoiseEquation(String noiseEquation) {
        this.noiseEquation = noiseEquation;
    }

    public String getElevationEquation() {
        return elevationEquation;
    }

    public void setElevationEquation(String elevationEquation) {
        this.elevationEquation = elevationEquation;
    }

    public void setCarvingEquation(String carvingEquation) {
        this.carvingEquation = carvingEquation;
    }

    public Scope getVarScope() {
        return varScope;
    }

    public void setVarScope(Scope varScope) {
        this.varScope = varScope;
    }

    public Map<String, NoiseBuilder> getNoiseBuilderMap() {
        return noiseBuilderMap;
    }

    public void setNoiseBuilderMap(Map<String, NoiseBuilder> noiseBuilderMap) {
        this.noiseBuilderMap = noiseBuilderMap;
    }

    public PaletteHolder getPalettes() {
        return palettes;
    }

    public void setPalettes(PaletteHolder palettes) {
        this.palettes = palettes;
    }

    public PaletteHolder getSlantPalettes() {
        return slantPalettes;
    }

    public void setSlantPalettes(PaletteHolder slantPalettes) {
        this.slantPalettes = slantPalettes;
    }

    public boolean isPreventInterpolation() {
        return preventInterpolation;
    }

    public void setPreventInterpolation(boolean preventInterpolation) {
        this.preventInterpolation = preventInterpolation;
    }

    public void setInterpolateElevation(boolean interpolateElevation) {
        this.interpolateElevation = interpolateElevation;
    }

    public boolean interpolateElevation() {
        return interpolateElevation;
    }
}
