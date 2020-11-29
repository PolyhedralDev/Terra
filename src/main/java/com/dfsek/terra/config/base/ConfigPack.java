package com.dfsek.terra.config.base;

import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
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
import com.dfsek.terra.config.files.Loader;
import com.dfsek.terra.config.files.ZIPLoader;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.templates.BiomeGridTemplate;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.config.templates.FloraTemplate;
import com.dfsek.terra.config.templates.OreTemplate;
import com.dfsek.terra.config.templates.PaletteTemplate;
import com.dfsek.terra.config.templates.StructureTemplate;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
    private final ConfigLoader loader = new ConfigLoader();
    private final Scope varScope = new Scope();

    {
        abstractConfigLoader
                .registerLoader(Palette.class, paletteRegistry)
                .registerLoader(Biome.class, biomeRegistry)
                .registerLoader(UserDefinedCarver.class, carverRegistry)
                .registerLoader(Flora.class, floraRegistry)
                .registerLoader(Ore.class, oreRegistry);
        ConfigUtil.registerAllLoaders(abstractConfigLoader);
        ConfigUtil.registerAllLoaders(loader);
    }

    public ConfigPack(File folder) throws ConfigException {
        long l = System.nanoTime();

        File pack = new File(folder, "pack.yml");

        try {
            loader.load(template, new FileInputStream(pack));
        } catch(FileNotFoundException e) {
            throw new FileMissingException("No pack.yml file found in " + folder.getAbsolutePath(), e);
        }

        load(new FolderLoader(folder.toPath()));

        LangUtil.log("config-pack.loaded", Level.INFO, template.getID(), String.valueOf((System.nanoTime() - l) / 1000000D));
    }

    public ConfigPack(ZipFile file) throws ConfigException {
        long l = System.nanoTime();

        InputStream stream = null;
        Enumeration<? extends ZipEntry> entries = file.entries();

        while(entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if(entry.getName().equals("pack.yml")) {
                try {
                    stream = file.getInputStream(entry);
                } catch(IOException e) {
                    throw new LoadException("Unable to load pack.yml from ZIP file", e);
                }
            }
        }
        if(stream == null) throw new FileMissingException("No pack.yml file found in " + file.getName());

        loader.load(template, stream);

        load(new ZIPLoader(file));
        LangUtil.log("config-pack.loaded", Level.INFO, template.getID(), String.valueOf((System.nanoTime() - l) / 1000000D));
    }

    private void load(Loader loader) throws ConfigException {
        for(Map.Entry<String, Double> var : template.getVariables().entrySet()) {
            varScope.create(var.getKey()).setValue(var.getValue());
        }

        loader.open("palettes")
                .then(streams -> {
                    PaletteFactory paletteFactory = new PaletteFactory();
                    abstractConfigLoader.load(streams, PaletteTemplate::new).forEach(palette -> {
                        paletteRegistry.add(palette.getID(), paletteFactory.build(palette));
                        Debug.info("Loaded palette " + palette.getID());
                    });
                })
                .close()
                .open("ores")
                .then(streams -> {
                    OreFactory oreFactory = new OreFactory();
                    abstractConfigLoader.load(streams, OreTemplate::new).forEach(ore -> {
                        oreRegistry.add(ore.getID(), oreFactory.build(ore));
                        Debug.info("Loaded ore " + ore.getID());
                    });
                })
                .close()
                .open("flora")
                .then(streams -> {
                    FloraFactory floraFactory = new FloraFactory();
                    abstractConfigLoader.load(streams, FloraTemplate::new).forEach(flora -> {
                        floraRegistry.add(flora.getID(), floraFactory.build(flora));
                        Debug.info("Loaded flora " + flora.getID());
                    });
                })
                .close()
                .open("structures/single")
                .then(streams -> abstractConfigLoader.load(streams, StructureTemplate::new).forEach(structure -> {
                    structureRegistry.add(structure.getID(), structure);
                    Debug.info("Loaded structure " + structure.getID());
                }))
                .close()
                .open("carving")
                .then(streams -> {
                    CarverFactory carverFactory = new CarverFactory(this);
                    abstractConfigLoader.load(streams, CarverTemplate::new).forEach(carver -> {
                        try {
                            carverRegistry.add(carver.getID(), carverFactory.build(carver));
                        } catch(LoadException e) {
                            throw new RuntimeException(e);
                        }
                        Debug.info("Loaded carver " + carver.getID());
                    });
                })
                .close()
                .open("biomes")
                .then(streams -> {
                    BiomeFactory biomeFactory = new BiomeFactory(this);
                    abstractConfigLoader.load(streams, () -> new BiomeTemplate(this)).forEach(biome -> {
                        biomeRegistry.add(biome.getID(), biomeFactory.build(biome));
                        Debug.info("Loaded biome " + biome.getID());
                    });
                })
                .close()
                .open("grids")
                .then(streams -> {
                    BiomeGridFactory biomeGridFactory = new BiomeGridFactory();
                    abstractConfigLoader.load(streams, BiomeGridTemplate::new).forEach(grid -> {
                        biomeGridRegistry.add(grid.getID(), biomeGridFactory.build(grid));
                        Debug.info("Loaded BiomeGrid " + grid.getID());
                    });
                })
                .close();
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
