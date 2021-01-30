package com.dfsek.terra.config.builder;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.ConstantSampler;
import com.dfsek.terra.api.math.noise.samplers.ExpressionSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.world.generation.WorldGenerator;
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

    private Map<String, NoiseSeeded> noiseBuilderMap;

    private PaletteHolder palettes;

    private PaletteHolder slantPalettes;

    private boolean preventInterpolation;

    private boolean interpolateElevation;

    private NoiseSeeded biomeNoise;

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
                return new WorldGenerator(palettes, slantPalettes, noise, elevation, carving, biomeNoise.apply(seed), elevationWeight, blendDistance, blendStep, blendWeight);
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

    public void setBiomeNoise(NoiseSeeded biomeNoise) {
        this.biomeNoise = biomeNoise;
    }

    public void setElevationWeight(double elevationWeight) {
        this.elevationWeight = elevationWeight;
    }

    public void setNoiseEquation(String noiseEquation) {
        this.noiseEquation = noiseEquation;
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

    public void setNoiseBuilderMap(Map<String, NoiseSeeded> noiseBuilderMap) {
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
