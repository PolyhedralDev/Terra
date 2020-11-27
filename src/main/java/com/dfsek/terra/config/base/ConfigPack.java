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
import com.dfsek.terra.config.factories.PaletteFactory;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.loaders.NoiseBuilderLoader;
import com.dfsek.terra.config.templates.BiomeGridTemplate;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.config.templates.PaletteTemplate;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.registry.BiomeGridRegistry;
import com.dfsek.terra.registry.BiomeRegistry;
import com.dfsek.terra.registry.CarverRegistry;
import com.dfsek.terra.registry.PaletteRegistry;
import com.dfsek.terra.registry.StructureRegistry;
import com.dfsek.terra.util.ConfigUtil;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.world.palette.Palette;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Represents a Terra configuration pack.
 */
public class ConfigPack {
    private final ConfigPackTemplate template = new ConfigPackTemplate();
    private final BiomeRegistry biomeRegistry = new BiomeRegistry();
    private final BiomeGridRegistry biomeGridRegistry = new BiomeGridRegistry();
    private final StructureRegistry structureRegistry = new StructureRegistry();
    private final CarverRegistry carverRegistry = new CarverRegistry();
    private final PaletteRegistry paletteRegistry = new PaletteRegistry();


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
        abstractConfigLoader
                .registerLoader(Palette.class, paletteRegistry)
                .registerLoader(Biome.class, biomeRegistry);
        ConfigUtil.registerAllLoaders(abstractConfigLoader);

        List<PaletteTemplate> paletteTemplates = abstractConfigLoader.load(ConfigUtil.loadFromPath(new File(folder, "palettes").toPath()), PaletteTemplate::new);
        PaletteFactory paletteFactory = new PaletteFactory();
        paletteTemplates.forEach(palette -> {
            paletteRegistry.add(palette.getID(), paletteFactory.build(palette));
            Debug.info("Loaded palette " + palette.getID());
        });

        List<StructureTemplate> structureTemplates = abstractConfigLoader.load(ConfigUtil.loadFromPath(new File(folder, "structures/single").toPath()), StructureTemplate::new);
        structureTemplates.forEach(structure -> {
            structureRegistry.add(structure.getID(), structure);
            Debug.info("Loaded structure " + structure.getID());
        });

        List<CarverTemplate> carverTemplates = abstractConfigLoader.load(ConfigUtil.loadFromPath(new File(folder, "carving").toPath()), CarverTemplate::new);
        CarverFactory carverFactory = new CarverFactory();
        carverTemplates.forEach(carver -> {
            carverRegistry.add(carver.getID(), carverFactory.build(carver));
            Debug.info("Loaded carver " + carver.getID());
        });

        List<BiomeTemplate> biomeTemplates = abstractConfigLoader.load(ConfigUtil.loadFromPath(new File(folder, "biomes").toPath()), () -> new BiomeTemplate(this));
        BiomeFactory biomeFactory = new BiomeFactory();
        biomeTemplates.forEach(biome -> {
            biomeRegistry.add(biome.getID(), biomeFactory.build(biome));
            Debug.info("Loaded biome " + biome.getID());
        });

        List<BiomeGridTemplate> biomeGridTemplates = abstractConfigLoader.load(ConfigUtil.loadFromPath(new File(folder, "grids").toPath()), BiomeGridTemplate::new);
        BiomeGridFactory biomeGridFactory = new BiomeGridFactory();
        biomeGridTemplates.forEach(grid -> {
            biomeGridRegistry.add(grid.getID(), biomeGridFactory.build(grid));
            Debug.info("Loaded BiomeGrid " + grid.getID());
        });

        LangUtil.log("config-pack.loaded", Level.INFO, template.getID(), String.valueOf((System.nanoTime() - l) / 1000000D));
    }

    public UserDefinedBiome getBiome(String id) {
        return biomeRegistry.get(id);
    }

    public BiomeGridBuilder getBiomeGrid(String id) {
        return biomeGridRegistry.get(id);
    }

    public List<String> getBiomeIDs() {
        List<String> biomeIDs = new ArrayList<>();
        biomeRegistry.forEach(biome -> biomeIDs.add(biome.getID()));
        return biomeIDs;
    }

    public StructureTemplate getStructure(String id) {
        return structureRegistry.get(id);
    }

    public Set<StructureTemplate> getStructures() {
        return structureRegistry.entries();
    }

    public Collection<UserDefinedCarver> getCarvers() {
        return carverRegistry.entries();
    }

    public UserDefinedCarver getCarver(String id) {
        return carverRegistry.get(id);
    }

    public List<String> getStructureIDs() {
        List<String> ids = new ArrayList<>();
        structureRegistry.forEach(structure -> ids.add(structure.getID()));
        return ids;
    }

    public ConfigPackTemplate getTemplate() {
        return template;
    }
}
