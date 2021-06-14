package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.util.MapUtil;
import com.dfsek.terra.api.util.generic.Lazy;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class ConfigPackTemplate implements ConfigTemplate {
    @Value("id")
    private String id;

    @Value("version")
    @Default
    private String version = "0.1.0";

    @Value("author")
    @Default
    private String author = "Anon Y. Mous";

    @Value("noise")
    private Map<String, MetaValue<NoiseSeeded>> noiseBuilderMap;
    private final Lazy<Map<String, NoiseSeeded>> lazyNoiseBuilderMap = new Lazy<>(() -> MapUtil.remapValues(MetaValue::get, noiseBuilderMap));

    @Value("addons")
    @Default
    private Set<TerraAddon> addons = new HashSet<>();

    @Value("variables")
    @Default
    private Map<String, MetaValue<Double>> variables = new HashMap<>();
    private final Lazy<Map<String, Double>> lazyVariables = new Lazy<>(() -> MapUtil.remapValues(MetaValue::get, variables));

    @Value("beta.carving")
    @Default
    private MetaValue<Boolean> betaCarvers = MetaValue.of(false);

    @Value("functions")
    @Default
    private LinkedHashMap<String, MetaValue<FunctionTemplate>> functions = new LinkedHashMap<>();
    private final Lazy<LinkedHashMap<String, FunctionTemplate>> lazyFunctions = new Lazy<>(() -> MapUtil.remap(Function.identity(), MetaValue::get, functions, LinkedHashMap::new));

    @Value("structures.locatable")
    @Default
    private Map<String, MetaValue<String>> locatable = new HashMap<>();
    private final Lazy<Map<String, String>> lazyLocatable = new Lazy<>(() -> MapUtil.remapValues(MetaValue::get, locatable));

    @Value("blend.terrain.elevation")
    @Default
    private MetaValue<Integer> elevationBlend = MetaValue.of(4);

    @Value("vanilla.mobs")
    @Default
    private MetaValue<Boolean> vanillaMobs = MetaValue.of(true);

    @Value("vanilla.caves")
    @Default
    private MetaValue<Boolean> vanillaCaves = MetaValue.of(false);

    @Value("vanilla.decorations")
    @Default
    private MetaValue<Boolean> vanillaDecorations = MetaValue.of(false);

    @Value("vanilla.structures")
    @Default
    private MetaValue<Boolean> vanillaStructures = MetaValue.of(false);

    @Value("disable.sapling")
    @Default
    private MetaValue<Boolean> disableSaplings = MetaValue.of(false);

    @Value("disable.carvers")
    @Default
    private MetaValue<Boolean> disableCarvers = MetaValue.of(false);

    @Value("disable.structures")
    @Default
    private MetaValue<Boolean> disableStructures = MetaValue.of(false);

    @Value("disable.ores")
    @Default
    private MetaValue<Boolean> disableOres = MetaValue.of(false);

    @Value("disable.trees")
    @Default
    private MetaValue<Boolean> disableTrees = MetaValue.of(false);

    @Value("disable.flora")
    @Default
    private MetaValue<Boolean> disableFlora = MetaValue.of(false);

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
        return lazyFunctions.get();
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
        return lazyNoiseBuilderMap.get();
    }

    public Map<String, Double> getVariables() {
        return lazyVariables.get();
    }

    public int getElevationBlend() {
        return elevationBlend.get();
    }

    public Map<String, String> getLocatable() {
        return lazyLocatable.get();
    }

    public boolean doBetaCarvers() {
        return betaCarvers.get();
    }

    public Set<TerraAddon> getAddons() {
        return addons;
    }
}
