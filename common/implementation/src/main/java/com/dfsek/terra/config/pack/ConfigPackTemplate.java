package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.generator.ChunkGeneratorProvider;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class ConfigPackTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("variables")
    @Default
    private @Meta Map<String, @Meta Double> variables = new HashMap<>();

    @Value("beta.carving")
    @Default
    private @Meta boolean betaCarvers = false;

    @Value("structures.locatable")
    @Default
    private @Meta Map<@Meta String, @Meta String> locatable = new HashMap<>();

    @Value("blend.terrain.elevation")
    @Default
    private @Meta int elevationBlend = 4;

    @Value("vanilla.mobs")
    @Default
    private @Meta boolean vanillaMobs = true;

    @Value("vanilla.caves")
    @Default
    private @Meta boolean vanillaCaves = false;

    @Value("vanilla.decorations")
    @Default
    private @Meta boolean vanillaDecorations = false;

    @Value("vanilla.structures")
    @Default
    private @Meta boolean vanillaStructures = false;

    @Value("author")
    @Default
    private String author = "Anon Y. Mous";

    @Value("disable.sapling")
    @Default
    private @Meta boolean disableSaplings = false;

    @Value("stages")
    private @Meta List<@Meta GenerationStageProvider> stages;

    @Value("version")
    @Default
    private String version = "0.1.0";

    @Value("disable.carvers")
    @Default
    private @Meta boolean disableCarvers = false;

    @Value("disable.structures")
    @Default
    private @Meta boolean disableStructures = false;

    @Value("disable.ores")
    @Default
    private @Meta boolean disableOres = false;

    @Value("disable.trees")
    @Default
    private @Meta boolean disableTrees = false;

    @Value("disable.flora")
    @Default
    private @Meta boolean disableFlora = false;

    @Value("generator")
    private @Meta ChunkGeneratorProvider generatorProvider;

    public ChunkGeneratorProvider getGeneratorProvider() {
        return generatorProvider;
    }

    public List<GenerationStageProvider> getStages() {
        return stages;
    }

    public boolean disableCarvers() {
        return disableCarvers;
    }

    public boolean disableFlora() {
        return disableFlora;
    }

    public boolean disableOres() {
        return disableOres;
    }

    public boolean disableStructures() {
        return disableStructures;
    }

    public boolean disableTrees() {
        return disableTrees;
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
}
