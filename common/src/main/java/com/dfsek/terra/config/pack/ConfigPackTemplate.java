package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.addons.addon.TerraAddon;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class ConfigPackTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("noise")
    private Map<String, NoiseSeeded> noiseBuilderMap;

    @Value("addons")
    @Default
    private Set<TerraAddon> addons = new HashSet<>();

    @Value("variables")
    @Default
    private Map<String, Double> variables = new HashMap<>();

    @Value("beta.carving")
    @Default
    private boolean betaCarvers = false;

    @Value("functions")
    @Default
    private LinkedHashMap<String, FunctionTemplate> functions = new LinkedHashMap<>();

    @Value("structures.locatable")
    @Default
    private Map<String, String> locatable = new HashMap<>();

    @Value("blend.terrain.elevation")
    @Default
    private int elevationBlend = 4;

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

    public Map<String, FunctionTemplate> getFunctions() {
        return functions;
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

    public Map<String, NoiseSeeded> getNoiseBuilderMap() {
        return noiseBuilderMap;
    }

    public Map<String, Double> getVariables() {
        return variables;
    }

    public int getElevationBlend() {
        return elevationBlend;
    }

    public Map<String, String> getLocatable() {
        return locatable;
    }

    public boolean doBetaCarvers() {
        return betaCarvers;
    }

    public Set<TerraAddon> getAddons() {
        return addons;
    }
}
