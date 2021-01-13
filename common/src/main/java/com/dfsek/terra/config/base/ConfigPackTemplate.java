package com.dfsek.terra.config.base;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ValidatedConfigTemplate;
import com.dfsek.tectonic.exception.ValidationException;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.generation.config.NoiseBuilder;
import net.jafama.FastMath;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class ConfigPackTemplate implements ValidatedConfigTemplate {
    @Value("id")
    private String id;

    @Value("noise")
    private Map<String, NoiseBuilder> noiseBuilderMap;

    @Value("variables")
    @Default
    private Map<String, Double> variables = new HashMap<>();

    @Value("grids")
    private List<String> grids;

    @Value("frequencies.zone")
    @Default
    private int zoneFreq = 256;

    @Value("blend.enable")
    @Default
    private boolean blend = false;

    @Value("blend.frequency")
    @Default
    private double blendFreq = 0.1;

    @Value("blend.amplitude")
    @Default
    private double blendAmp = 4.0D;

    @Value("blend.terrain.base")
    @Default
    private int baseBlend = 4;

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

    @Value("biome-pipeline")
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

    public List<String> getGrids() {
        return grids;
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

    public int getBaseBlend() {
        return baseBlend;
    }

    public int getElevationBlend() {
        return elevationBlend;
    }

    public Map<String, String> getLocatable() {
        return locatable;
    }

    @Override
    public boolean validate() throws ValidationException {
        if(!MathUtil.equals(FastMath.log(baseBlend) / FastMath.log(2d), FastMath.round(FastMath.log(baseBlend) / FastMath.log(2d)))) {
            throw new ValidationException("TerraBiome base blend value \"" + baseBlend + "\" is not a power of 2.");
        }
        if(!MathUtil.equals(FastMath.log(elevationBlend) / FastMath.log(2d), FastMath.round(FastMath.log(elevationBlend) / FastMath.log(2d)))) {
            throw new ValidationException("TerraBiome elevation blend value \"" + baseBlend + "\" is not a power of 2.");
        }
        return true;
    }
}
