package com.dfsek.terra.config.base;

import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.loot.LootTable;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.structures.world.CheckCache;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.api.world.tree.Tree;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.builder.biomegrid.BiomeGridBuilder;
import com.dfsek.terra.config.exception.FileMissingException;
import com.dfsek.terra.config.factories.BiomeFactory;
import com.dfsek.terra.config.factories.BiomeGridFactory;
import com.dfsek.terra.config.factories.CarverFactory;
import com.dfsek.terra.config.factories.FloraFactory;
import com.dfsek.terra.config.factories.OreFactory;
import com.dfsek.terra.config.factories.PaletteFactory;
import com.dfsek.terra.config.factories.StructureFactory;
import com.dfsek.terra.config.factories.TerraFactory;
import com.dfsek.terra.config.factories.TreeFactory;
import com.dfsek.terra.config.files.FolderLoader;
import com.dfsek.terra.config.files.Loader;
import com.dfsek.terra.config.files.ZIPLoader;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.loaders.LootTableLoader;
import com.dfsek.terra.config.templates.AbstractableTemplate;
import com.dfsek.terra.config.templates.BiomeGridTemplate;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.config.templates.FloraTemplate;
import com.dfsek.terra.config.templates.OreTemplate;
import com.dfsek.terra.config.templates.PaletteTemplate;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.config.templates.TreeTemplate;
import com.dfsek.terra.generation.items.TerraStructure;
import com.dfsek.terra.generation.items.ores.Ore;
import com.dfsek.terra.registry.BiomeGridRegistry;
import com.dfsek.terra.registry.BiomeRegistry;
import com.dfsek.terra.registry.CarverRegistry;
import com.dfsek.terra.registry.FloraRegistry;
import com.dfsek.terra.registry.OreRegistry;
import com.dfsek.terra.registry.PaletteRegistry;
import com.dfsek.terra.registry.ScriptRegistry;
import com.dfsek.terra.registry.StructureRegistry;
import com.dfsek.terra.registry.TerraRegistry;
import com.dfsek.terra.registry.TreeRegistry;
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
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Represents a Terra configuration pack.
 */
public class ConfigPack implements LoaderRegistrar {
    private final ConfigPackTemplate template = new ConfigPackTemplate();
    private final BiomeRegistry biomeRegistry = new BiomeRegistry();
    private final BiomeGridRegistry biomeGridRegistry = new BiomeGridRegistry(biomeRegistry);
    private final StructureRegistry structureRegistry = new StructureRegistry();
    private final CarverRegistry carverRegistry = new CarverRegistry();
    private final PaletteRegistry paletteRegistry;
    private final FloraRegistry floraRegistry;
    private final OreRegistry oreRegistry = new OreRegistry();
    private final TreeRegistry treeRegistry;
    private final ScriptRegistry scriptRegistry = new ScriptRegistry();

    private final AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();
    private final ConfigLoader selfLoader = new ConfigLoader();
    private final Scope varScope = new Scope();



    public ConfigPack(File folder, TerraPlugin main) throws ConfigException {
        long l = System.nanoTime();
        floraRegistry = new FloraRegistry(main);
        paletteRegistry = new PaletteRegistry(main);
        treeRegistry = new TreeRegistry(main);
        register(abstractConfigLoader);

        main.register(selfLoader);
        main.register(abstractConfigLoader);

        File pack = new File(folder, "pack.yml");

        try {
            selfLoader.load(template, new FileInputStream(pack));
        } catch(FileNotFoundException e) {
            throw new FileMissingException("No pack.yml file found in " + folder.getAbsolutePath(), e);
        }

        load(new FolderLoader(folder.toPath()), l, main);
    }

    public ConfigPack(ZipFile file, TerraPlugin main) throws ConfigException {
        long l = System.nanoTime();
        floraRegistry = new FloraRegistry(main);
        paletteRegistry = new PaletteRegistry(main);
        treeRegistry = new TreeRegistry(main);
        register(abstractConfigLoader);

        main.register(selfLoader);
        main.register(abstractConfigLoader);

        InputStream stream = null;

        try {
            Enumeration<? extends ZipEntry> entries = file.entries();
            while(entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if(entry.getName().equals("pack.yml")) stream = file.getInputStream(entry);
            }
        } catch(IOException e) {
            throw new LoadException("Unable to load pack.yml from ZIP file", e);
        }
        if(stream == null) throw new FileMissingException("No pack.yml file found in " + file.getName());

        selfLoader.load(template, stream);

        load(new ZIPLoader(file), l, main);
    }

    private void load(Loader loader, long start, TerraPlugin main) throws ConfigException {
        for(Map.Entry<String, Double> var : template.getVariables().entrySet()) {
            varScope.create(var.getKey()).setValue(var.getValue());
        }
        abstractConfigLoader
                .registerLoader(LootTable.class, new LootTableLoader(loader, main)); // These loaders need access to the Loader instance to get files.

        CheckCache checkCache = new CheckCache(main);
        loader.open("structures/data", ".tesf").then(streams -> streams.forEach(stream -> {
            StructureScript structureScript = new StructureScript(stream, main, scriptRegistry, checkCache);
            scriptRegistry.add(structureScript.getId(), structureScript);
        })).close();

        loader
                .open("palettes", ".yml").then(streams -> buildAll(new PaletteFactory(), paletteRegistry, abstractConfigLoader.load(streams, PaletteTemplate::new), main)).close()
                .open("ores", ".yml").then(streams -> buildAll(new OreFactory(), oreRegistry, abstractConfigLoader.load(streams, OreTemplate::new), main)).close()
                .open("structures/trees", ".yml").then(streams -> buildAll(new TreeFactory(), treeRegistry, abstractConfigLoader.load(streams, TreeTemplate::new), main)).close()
                .open("structures/structures", ".yml").then(streams -> buildAll(new StructureFactory(), structureRegistry, abstractConfigLoader.load(streams, StructureTemplate::new), main)).close()
                .open("flora", ".yml").then(streams -> buildAll(new FloraFactory(), floraRegistry, abstractConfigLoader.load(streams, FloraTemplate::new), main)).close()
                .open("carving", ".yml").then(streams -> buildAll(new CarverFactory(this), carverRegistry, abstractConfigLoader.load(streams, CarverTemplate::new), main)).close()
                .open("biomes", ".yml").then(streams -> buildAll(new BiomeFactory(this), biomeRegistry, abstractConfigLoader.load(streams, () -> new BiomeTemplate(this, main)), main)).close()
                .open("grids", ".yml").then(streams -> buildAll(new BiomeGridFactory(), biomeGridRegistry, abstractConfigLoader.load(streams, BiomeGridTemplate::new), main)).close();


        for(UserDefinedBiome b : biomeRegistry.entries()) {
            try {
                Objects.requireNonNull(b.getErode()); // Throws NPE if it cannot load erosion biomes.
            } catch(NullPointerException e) {
                throw new LoadException("Invalid erosion biome defined in biome \"" + b.getID() + "\"", e);
            }
        }

        for(String gridName : template.getGrids()) {
            if(!biomeGridRegistry.contains(gridName)) throw new LoadException("No such BiomeGrid \"" + gridName + "\"");
        }

        if(template.getGridType().equals(TerraBiomeGrid.Type.RADIAL) && !biomeGridRegistry.contains(template.getRadialInternalGrid())) {
            throw new LoadException("No such BiomeGrid \"" + template.getRadialInternalGrid() + "\"");
        }

        LangUtil.log("config-pack.loaded", Level.INFO, template.getID(), String.valueOf((System.nanoTime() - start) / 1000000D), template.getAuthor(), template.getVersion());
    }

    private <C extends AbstractableTemplate, O> void buildAll(TerraFactory<C, O> factory, TerraRegistry<O> registry, List<C> configTemplates, TerraPlugin main) throws LoadException {
        for(C template : configTemplates) registry.add(template.getID(), factory.build(template, main));
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

    public TerraStructure getStructure(String id) {
        return structureRegistry.get(id);
    }

    public Set<TerraStructure> getStructures() {
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
        structureRegistry.forEach(structure -> ids.add(structure.getTemplate().getID()));
        return ids;
    }

    public TreeRegistry getTreeRegistry() {
        return treeRegistry;
    }

    public ConfigPackTemplate getTemplate() {
        return template;
    }

    public Scope getVarScope() {
        return varScope;
    }


    @Override
    public void register(TypeRegistry registry) {
        registry
                .registerLoader(Palette.class, paletteRegistry)
                .registerLoader(Biome.class, biomeRegistry)
                .registerLoader(UserDefinedCarver.class, carverRegistry)
                .registerLoader(Flora.class, floraRegistry)
                .registerLoader(Ore.class, oreRegistry)
                .registerLoader(Tree.class, treeRegistry)
                .registerLoader(StructureScript.class, scriptRegistry)
                .registerLoader(TerraStructure.class, structureRegistry);
    }

    public ScriptRegistry getScriptRegistry() {
        return scriptRegistry;
    }

    public BiomeRegistry getBiomeRegistry() {
        return biomeRegistry;
    }
}
