package com.dfsek.terra.config.base;

import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.Debug;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.builder.BiomeGridBuilder;
import com.dfsek.terra.config.exception.FileMissingException;
import com.dfsek.terra.config.factories.BiomeFactory;
import com.dfsek.terra.config.factories.BiomeGridFactory;
import com.dfsek.terra.config.factories.CarverFactory;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.loaders.NoiseBuilderLoader;
import com.dfsek.terra.config.templates.BiomeGridTemplate;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.util.ConfigUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Represents a Terra configuration pack.
 */
public class ConfigPack {
    private final ConfigPackTemplate template = new ConfigPackTemplate();
    private final Map<String, UserDefinedBiome> biomes = new HashMap<>();
    private final Map<String, BiomeGridBuilder> biomeGrids = new HashMap<>();
    private final Map<String, StructureTemplate> structures = new HashMap<>();
    private final Map<String, UserDefinedCarver> carvers = new HashMap<>();


    public ConfigPack(File folder) throws ConfigException {
        long l = System.nanoTime();

        File pack = new File(folder, "pack.yml");

        ConfigLoader loader = new ConfigLoader();
        loader.registerLoader(NoiseBuilder.class, new NoiseBuilderLoader());
        try {
            loader.load(template, new FileInputStream(pack));
        } catch(FileNotFoundException e) {
            throw new FileMissingException("No pack.yml file found in " + folder.getAbsolutePath(), e);
        }


        AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();

        List<StructureTemplate> structureTemplates = abstractConfigLoader.load(ConfigUtil.loadFromPath(new File(folder, "structures/single").toPath()), StructureTemplate::new);
        structureTemplates.forEach(structure -> {
            structures.put(structure.getID(), structure);
            Debug.info("Loaded structure " + structure.getID());
        });

        List<CarverTemplate> carverTemplates = abstractConfigLoader.load(ConfigUtil.loadFromPath(new File(folder, "carving").toPath()), CarverTemplate::new);
        CarverFactory carverFactory = new CarverFactory();
        carverTemplates.forEach(carver -> {
            carvers.put(carver.getID(), carverFactory.build(carver));
            Debug.info("Loaded carver " + carver.getID());
        });

        List<BiomeTemplate> biomeTemplates = abstractConfigLoader.load(ConfigUtil.loadFromPath(new File(folder, "biomes").toPath()), () -> new BiomeTemplate(this));
        BiomeFactory biomeFactory = new BiomeFactory();
        biomeTemplates.forEach(biome -> {
            biomes.put(biome.getID(), biomeFactory.build(biome));
            Debug.info("Loaded biome " + biome.getID());
        });

        List<BiomeGridTemplate> biomeGridTemplates = abstractConfigLoader.load(ConfigUtil.loadFromPath(new File(folder, "grids").toPath()), BiomeGridTemplate::new);
        BiomeGridFactory biomeGridFactory = new BiomeGridFactory();
        biomeGridTemplates.forEach(grid -> {
            biomeGrids.put(grid.getID(), biomeGridFactory.build(grid));
            Debug.info("Loaded BiomeGrid " + grid.getID());
        });

        LangUtil.log("config-pack.loaded", Level.INFO, template.getID(), String.valueOf((System.nanoTime() - l) / 1000000D));
    }

    public UserDefinedBiome getBiome(String id) {
        return biomes.get(id);
    }

    public BiomeGridBuilder getBiomeGrid(String id) {
        return biomeGrids.get(id);
    }

    public List<String> getBiomeIDs() {
        List<String> biomeIDs = new ArrayList<>();
        biomes.forEach((id, biome) -> biomeIDs.add(id));
        return biomeIDs;
    }

    public StructureTemplate getStructure(String id) {
        return structures.get(id);
    }

    public Set<StructureTemplate> getStructures() {
        return new HashSet<>(structures.values());
    }

    public Collection<UserDefinedCarver> getCarvers() {
        return carvers.values();
    }

    public UserDefinedCarver getCarver(String id) {
        return carvers.get(id);
    }

    public List<String> getStructureIDs() {
        List<String> ids = new ArrayList<>();
        structures.forEach((id, structure) -> ids.add(id));
        return ids;
    }

    public ConfigPackTemplate getTemplate() {
        return template;
    }
}
