package com.dfsek.terra.config.base;

import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.Debug;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.builder.biomegrid.BiomeGridBuilder;
import com.dfsek.terra.config.exception.FileMissingException;
import com.dfsek.terra.config.factories.BiomeFactory;
import com.dfsek.terra.config.factories.BiomeGridFactory;
import com.dfsek.terra.config.factories.CarverFactory;
import com.dfsek.terra.config.factories.FloraFactory;
import com.dfsek.terra.config.factories.OreFactory;
import com.dfsek.terra.config.factories.PaletteFactory;
import com.dfsek.terra.config.files.FolderLoader;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.loaders.NoiseBuilderLoader;
import com.dfsek.terra.config.templates.BiomeGridTemplate;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.config.templates.FloraTemplate;
import com.dfsek.terra.config.templates.OreTemplate;
import com.dfsek.terra.config.templates.PaletteTemplate;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.generation.items.ores.Ore;
import com.dfsek.terra.registry.BiomeGridRegistry;
import com.dfsek.terra.registry.BiomeRegistry;
import com.dfsek.terra.registry.CarverRegistry;
import com.dfsek.terra.registry.FloraRegistry;
import com.dfsek.terra.registry.OreRegistry;
import com.dfsek.terra.registry.PaletteRegistry;
import com.dfsek.terra.registry.StructureRegistry;
import com.dfsek.terra.util.ConfigUtil;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.palette.Palette;
import parsii.eval.Scope;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Represents a Terra configuration pack.
 */
public class ConfigPack {
    private final ConfigPackTemplate template = new ConfigPackTemplate();
    private final BiomeRegistry biomeRegistry = new BiomeRegistry();
    private final BiomeGridRegistry biomeGridRegistry = new BiomeGridRegistry(biomeRegistry);
    private final StructureRegistry structureRegistry = new StructureRegistry();
    private final CarverRegistry carverRegistry = new CarverRegistry();
    private final PaletteRegistry paletteRegistry = new PaletteRegistry();
    private final FloraRegistry floraRegistry = new FloraRegistry();
    private final OreRegistry oreRegistry = new OreRegistry();

    private final AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();

    {
        abstractConfigLoader
                .registerLoader(Palette.class, paletteRegistry)
                .registerLoader(Biome.class, biomeRegistry)
                .registerLoader(UserDefinedCarver.class, carverRegistry)
                .registerLoader(Flora.class, floraRegistry)
                .registerLoader(Ore.class, oreRegistry);
        ConfigUtil.registerAllLoaders(abstractConfigLoader);
    }

    private final Scope varScope;


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

        varScope = new Scope();

        for(Map.Entry<String, Double> var : template.getVariables().entrySet()) {
            varScope.create(var.getKey()).setValue(var.getValue());
        }

        List<PaletteTemplate> paletteTemplates = new ArrayList<>();
        new FolderLoader(new File(folder, "palettes").toPath())
                .then(streams -> paletteTemplates.addAll(abstractConfigLoader.load(streams, PaletteTemplate::new)))
                .close();

        PaletteFactory paletteFactory = new PaletteFactory();
        paletteTemplates.forEach(palette -> {
            paletteRegistry.add(palette.getID(), paletteFactory.build(palette));
            Debug.info("Loaded palette " + palette.getID());
        });

        List<OreTemplate> oreTemplates = new ArrayList<>();
        new FolderLoader(new File(folder, "ores").toPath())
                .then(streams -> oreTemplates.addAll(abstractConfigLoader.load(streams, OreTemplate::new)))
                .close();

        OreFactory oreFactory = new OreFactory();
        oreTemplates.forEach(ore -> {
            oreRegistry.add(ore.getID(), oreFactory.build(ore));
            Debug.info("Loaded ore " + ore.getID());
        });


        List<FloraTemplate> floraTemplates = new ArrayList<>();
        new FolderLoader(new File(folder, "flora").toPath())
                .then(streams -> floraTemplates.addAll(abstractConfigLoader.load(streams, FloraTemplate::new)))
                .close();

        FloraFactory floraFactory = new FloraFactory();
        floraTemplates.forEach(flora -> {
            floraRegistry.add(flora.getID(), floraFactory.build(flora));
            Debug.info("Loaded flora " + flora.getID());
        });

        List<StructureTemplate> structureTemplates = new ArrayList<>();
        new FolderLoader(new File(folder, "structures/single").toPath())
                .then(streams -> structureTemplates.addAll(abstractConfigLoader.load(streams, StructureTemplate::new)))
                .close();

        structureTemplates.forEach(structure -> {
            structureRegistry.add(structure.getID(), structure);
            Debug.info("Loaded structure " + structure.getID());
        });

        List<CarverTemplate> carverTemplates = new ArrayList<>();
        new FolderLoader(new File(folder, "carving").toPath())
                .then(streams -> carverTemplates.addAll(abstractConfigLoader.load(streams, CarverTemplate::new)))
                .close();

        CarverFactory carverFactory = new CarverFactory();
        carverTemplates.forEach(carver -> {
            carverRegistry.add(carver.getID(), carverFactory.build(carver));
            Debug.info("Loaded carver " + carver.getID());
        });

        List<BiomeTemplate> biomeTemplates = new ArrayList<>();
        new FolderLoader(new File(folder, "biomes").toPath())
                .then(streams -> biomeTemplates.addAll(abstractConfigLoader.load(streams, () -> new BiomeTemplate(this))))
                .close();

        BiomeFactory biomeFactory = new BiomeFactory(this);
        biomeTemplates.forEach(biome -> {
            biomeRegistry.add(biome.getID(), biomeFactory.build(biome));
            Debug.info("Loaded biome " + biome.getID());
        });

        List<BiomeGridTemplate> biomeGridTemplates = new ArrayList<>();
        new FolderLoader(new File(folder, "grids").toPath())
                .then(streams -> biomeGridTemplates.addAll(abstractConfigLoader.load(streams, BiomeGridTemplate::new)))
                .close();

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

    public Scope getVarScope() {
        return varScope;
    }
}
