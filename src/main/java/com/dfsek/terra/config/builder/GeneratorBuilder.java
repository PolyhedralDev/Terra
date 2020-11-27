package com.dfsek.terra.config.builder;

import com.dfsek.terra.biome.PaletteHolder;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.generation.config.WorldGenerator;
import parsii.eval.Scope;

import java.util.Map;

public class GeneratorBuilder {
    private String noiseEquation;

    private String elevationEquation;

    private Scope varScope;

    private Map<String, NoiseBuilder> noiseBuilderMap;

    private PaletteHolder palettes;

    private PaletteHolder slantPalettes;

    private boolean preventInterpolation;

    public WorldGenerator build(long seed) {
        return new WorldGenerator(seed, noiseEquation, elevationEquation, varScope, noiseBuilderMap, palettes, slantPalettes, preventInterpolation);
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
}
