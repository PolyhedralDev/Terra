package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.biome.provider.BiomeProvider;
import com.dfsek.terra.world.generation.config.NoiseBuilder;

import java.util.HashMap;
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

    @Value("structures.locatable")
    @Default
    private Map<String, String> locatable = new HashMap<>();

    @Value("blend.terrain.elevation")
    @Default
    private int elevationBlend = 4;

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

    @Value("vanilla.mobs")
    @Default
    private boolean vanillaMobs = true;

    @Value("vanilla.caves")
    @Default
    private boolean vanillaCaves = false;

    @Value("vanilla.decorations")
    @Default
    private boolean vanillaDecorations = false;

    @Value("vanilla.structures")
    @Default
    private boolean vanillaStructures = false;

    @Value("author")
    @Default
    private String author = "Anon Y. Mous";

    @Value("disable.sapling")
    @Default
    private boolean disableSaplings = false;

    @Value("version")
    @Default
    private String version = "0.1.0";

    @Value("biomes")
    private BiomeProvider.BiomeProviderBuilder providerBuilder;

    public BiomeProvider.BiomeProviderBuilder getProviderBuilder() {
        return providerBuilder;
    }

    public String getVersion() {
        return version;
    }

    public boolean isDisableSaplings() {
        return disableSaplings;
    }

    public String getID() {
        return id;
    }

    public String getAuthor() {
        return author;
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

    public int getElevationBlend() {
        return elevationBlend;
    }

    public Map<String, String> getLocatable() {
        return locatable;
    }
}
