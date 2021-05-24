package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;
import com.dfsek.terra.config.loaders.mod.ModDependentConfigSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Map<String, ModDependentConfigSection<String>> locatable = new HashMap<>();

    @Value("blend.terrain.elevation")
    @Default
    private int elevationBlend = 4;

    @Value("vanilla.mobs")
    @Default
    private ModDependentConfigSection<Boolean> vanillaMobs = ModDependentConfigSection.withDefault(true);

    @Value("vanilla.caves")
    @Default
    private ModDependentConfigSection<Boolean> vanillaCaves = ModDependentConfigSection.withDefault(false);

    @Value("vanilla.decorations")
    @Default
    private ModDependentConfigSection<Boolean> vanillaDecorations = ModDependentConfigSection.withDefault(false);

    @Value("vanilla.structures")
    @Default
    private ModDependentConfigSection<Boolean> vanillaStructures = ModDependentConfigSection.withDefault(false);

    @Value("author")
    @Default
    private String author = "Anon Y. Mous";

    @Value("disable.sapling")
    @Default
    private ModDependentConfigSection<Boolean> disableSaplings = ModDependentConfigSection.withDefault(false);

    @Value("version")
    @Default
    private String version = "0.1.0";

    @Value("disable.carvers")
    @Default
    private ModDependentConfigSection<Boolean> disableCarvers = ModDependentConfigSection.withDefault(false);

    @Value("disable.structures")
    @Default
    private ModDependentConfigSection<Boolean> disableStructures = ModDependentConfigSection.withDefault(false);

    @Value("disable.ores")
    @Default
    private ModDependentConfigSection<Boolean> disableOres = ModDependentConfigSection.withDefault(false);

    @Value("disable.trees")
    @Default
    private ModDependentConfigSection<Boolean> disableTrees = ModDependentConfigSection.withDefault(false);

    @Value("disable.flora")
    @Default
    private ModDependentConfigSection<Boolean> disableFlora = ModDependentConfigSection.withDefault(false);

    public boolean disableCarvers() {
        return disableCarvers.get();
    }

    public boolean disableFlora() {
        return disableFlora.get();
    }

    public boolean disableOres() {
        return disableOres.get();
    }

    public boolean disableStructures() {
        return disableStructures.get();
    }

    public boolean disableTrees() {
        return disableTrees.get();
    }

    public LinkedHashMap<String, FunctionTemplate> getFunctions() {
        return functions;
    }

    public String getVersion() {
        return version;
    }

    public boolean isDisableSaplings() {
        return disableSaplings.get();
    }

    public String getID() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public boolean vanillaMobs() {
        return vanillaMobs.get();
    }

    public boolean vanillaCaves() {
        return vanillaCaves.get();
    }

    public boolean vanillaDecorations() {
        return vanillaDecorations.get();
    }

    public boolean vanillaStructures() {
        return vanillaStructures.get();
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
        return locatable.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
    }

    public boolean doBetaCarvers() {
        return betaCarvers;
    }

    public Set<TerraAddon> getAddons() {
        return addons;
    }
}
