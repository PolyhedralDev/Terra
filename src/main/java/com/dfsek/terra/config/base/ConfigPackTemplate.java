package com.dfsek.terra.config.base;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.util.StructureTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class ConfigPackTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("noise")
    private Map<String, NoiseBuilder> noiseBuilderMap;

    @Value("variables")
    @Default
    private Map<String, Double> variables = new HashMap<>();

    @Value("locatable")
    @Default
    private Map<StructureTypeEnum, String> structureLocatables = new HashMap<>();

    @Value("grids")
    private List<String> grids;

    @Value("frequencies.grid-x")
    @Default
    private int gridFreqX = 4096;

    @Value("frequencies.grid-z")
    @Default
    private int gridFreqZ = 4096;

    @Value("frequencies.zone")
    @Default
    private int zoneFreq = 2048;

    @Value("blend.enable")
    @Default
    private boolean blend = false;

    @Value("blend.frequency")
    @Default
    private double blendFreq = 0.1;

    @Value("blend.amplitude")
    @Default
    private double blendAmp = 4.0D;

    @Value("erode.enable")
    @Default
    private boolean erode = false;

    @Value("erode.frequency")
    @Default
    private double erodeFreq = 0.001D;

    @Value("erode.threshold")
    @Default
    private double erodeThresh = 0.0015D;

    @Value("erode.octaves")
    @Default
    private int erodeOctaves = 5;

    @Value("erode.grid")
    @Default
    private String erodeGrid = null;

    @Value("vanilla.mobs")
    @Default
    private boolean vanillaMobs = false;

    @Value("vanilla.caves")
    @Default
    private boolean vanillaCaves = false;

    @Value("vanilla.decorations")
    @Default
    private boolean vanillaDecorations = false;

    @Value("vanilla.structures")
    @Default
    private boolean vanillaStructures = false;

    public String getID() {
        return id;
    }

    public boolean vanillaMobs() {
        return vanillaMobs;
    }

    public boolean vanillaCaves() {
        return vanillaCaves;
    }

    public boolean vanillaDecorations() {
        return vanillaDecorations;
    }

    public boolean vanillaStructures() {
        return vanillaStructures;
    }

    public Map<String, NoiseBuilder> getNoiseBuilderMap() {
        return noiseBuilderMap;
    }

    public Map<String, Double> getVariables() {
        return variables;
    }

    public Map<StructureTypeEnum, String> getStructureLocatables() {
        return structureLocatables;
    }

    public List<String> getGrids() {
        return grids;
    }

    public int getGridFreqX() {
        return gridFreqX;
    }

    public int getGridFreqZ() {
        return gridFreqZ;
    }

    public int getZoneFreq() {
        return zoneFreq;
    }

    public boolean isBlend() {
        return blend;
    }

    public double getBlendFreq() {
        return blendFreq;
    }

    public double getBlendAmp() {
        return blendAmp;
    }

    public boolean isErode() {
        return erode;
    }

    public double getErodeFreq() {
        return erodeFreq;
    }

    public double getErodeThresh() {
        return erodeThresh;
    }

    public int getErodeOctaves() {
        return erodeOctaves;
    }

    public String getErodeGrid() {
        return erodeGrid;
    }
}
