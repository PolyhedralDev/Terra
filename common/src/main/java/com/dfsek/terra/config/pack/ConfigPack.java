package com.dfsek.terra.config.pack;

import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.event.events.config.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.structures.loot.LootTable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.dummy.DummyWorld;
import com.dfsek.terra.config.factories.BiomeFactory;
import com.dfsek.terra.config.factories.CarverFactory;
import com.dfsek.terra.config.factories.ConfigFactory;
import com.dfsek.terra.config.factories.FloraFactory;
import com.dfsek.terra.config.factories.OreFactory;
import com.dfsek.terra.config.factories.PaletteFactory;
import com.dfsek.terra.config.factories.StructureFactory;
import com.dfsek.terra.config.factories.TreeFactory;
import com.dfsek.terra.config.fileloaders.FolderLoader;
import com.dfsek.terra.config.fileloaders.Loader;
import com.dfsek.terra.config.fileloaders.ZIPLoader;
import com.dfsek.terra.config.loaders.config.BufferedImageLoader;
import com.dfsek.terra.config.loaders.config.biome.templates.provider.BiomePipelineTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.provider.ImageProviderTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.provider.SingleBiomeProviderTemplate;
import com.dfsek.terra.config.loaders.config.sampler.NoiseSamplerBuilderLoader;
import com.dfsek.terra.config.loaders.config.sampler.templates.ImageSamplerTemplate;
import com.dfsek.terra.config.templates.AbstractableTemplate;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.config.templates.FloraTemplate;
import com.dfsek.terra.config.templates.OreTemplate;
import com.dfsek.terra.config.templates.PaletteTemplate;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.config.templates.TreeTemplate;
import com.dfsek.terra.registry.OpenRegistry;
import com.dfsek.terra.registry.config.BiomeRegistry;
import com.dfsek.terra.registry.config.CarverRegistry;
import com.dfsek.terra.registry.config.FloraRegistry;
import com.dfsek.terra.registry.config.FunctionRegistry;
import com.dfsek.terra.registry.config.LootRegistry;
import com.dfsek.terra.registry.config.NoiseRegistry;
import com.dfsek.terra.registry.config.OreRegistry;
import com.dfsek.terra.registry.config.PaletteRegistry;
import com.dfsek.terra.registry.config.ScriptRegistry;
import com.dfsek.terra.registry.config.StructureRegistry;
import com.dfsek.terra.registry.config.TreeRegistry;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.population.items.TerraStructure;
import com.dfsek.terra.world.population.items.ores.Ore;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Represents a Terra configuration pack.
 */
public class ConfigPack implements LoaderRegistrar {
    private final ConfigPackTemplate template = new ConfigPackTemplate();

    private final BiomeRegistry biomeRegistry = new BiomeRegistry();
    private final StructureRegistry structureRegistry = new StructureRegistry();
    private final PaletteRegistry paletteRegistry;
    private final FloraRegistry floraRegistry;
    private final OreRegistry oreRegistry = new OreRegistry();
    private final TreeRegistry treeRegistry;
    private final ScriptRegistry scriptRegistry = new ScriptRegistry();
    private final LootRegistry lootRegistry = new LootRegistry();

    private final CarverRegistry carverRegistry = new CarverRegistry();

    private final NoiseRegistry noiseRegistry = new NoiseRegistry();
    private final FunctionRegistry functionRegistry = new FunctionRegistry();

    private final AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();
    private final ConfigLoader selfLoader = new ConfigLoader();
    private final Scope varScope = new Scope();

    private final TerraPlugin main;
    private final Loader loader;

    private final BiomeProvider.BiomeProviderBuilder biomeProviderBuilder;


    public ConfigPack(File folder, TerraPlugin main) throws ConfigException {
        try {
            this.loader = new FolderLoader(folder.toPath());
            this.main = main;
            long l = System.nanoTime();
            floraRegistry = new FloraRegistry(main);
            paletteRegistry = new PaletteRegistry(main);
            treeRegistry = new TreeRegistry();
            register(abstractConfigLoader);
            register(selfLoader);

            main.register(selfLoader);
            main.register(abstractConfigLoader);

            File pack = new File(folder, "pack.yml");

            try {
                selfLoader.load(template, new FileInputStream(pack));

                main.logger().info("Loading config pack \"" + template.getID() + "\"");

                load(l, main);
                ConfigPackPostTemplate packPostTemplate = new ConfigPackPostTemplate();
                selfLoader.load(packPostTemplate, new FileInputStream(pack));
                biomeProviderBuilder = packPostTemplate.getProviderBuilder();
                biomeProviderBuilder.build(0); // Build dummy provider to catch errors at load time.
                checkDeadEntries(main);
            } catch(FileNotFoundException e) {
                throw new LoadException("No pack.yml file found in " + folder.getAbsolutePath(), e);
            }
        } catch(Exception e) {
            main.logger().severe("Failed to load config pack from folder \"" + folder.getAbsolutePath() + "\"");
            throw e;
        }
        toWorldConfig(new TerraWorld(new DummyWorld(), this, main)); // Build now to catch any errors immediately.
    }

    public ConfigPack(ZipFile file, TerraPlugin main) throws ConfigException {
        try {
            this.loader = new ZIPLoader(file);
            this.main = main;
            long l = System.nanoTime();
            floraRegistry = new FloraRegistry(main);
            paletteRegistry = new PaletteRegistry(main);
            treeRegistry = new TreeRegistry();
            register(abstractConfigLoader);
            register(selfLoader);

            main.register(selfLoader);
            main.register(abstractConfigLoader);

            try {
                ZipEntry pack = null;
                Enumeration<? extends ZipEntry> entries = file.entries();
                while(entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if(entry.getName().equals("pack.yml")) pack = entry;
                }

                if(pack == null) throw new LoadException("No pack.yml file found in " + file.getName());

                selfLoader.load(template, file.getInputStream(pack));
                main.logger().info("Loading config pack \"" + template.getID() + "\"");

                load(l, main);

                ConfigPackPostTemplate packPostTemplate = new ConfigPackPostTemplate();

                selfLoader.load(packPostTemplate, file.getInputStream(pack));
                biomeProviderBuilder = packPostTemplate.getProviderBuilder();
                biomeProviderBuilder.build(0); // Build dummy provider to catch errors at load time.
                checkDeadEntries(main);
            } catch(IOException e) {
                throw new LoadException("Unable to load pack.yml from ZIP file", e);
            }
        } catch(Exception e) {
            main.logger().severe("Failed to load config pack from ZIP archive \"" + file.getName() + "\"");
            throw e;
        }

        toWorldConfig(new TerraWorld(new DummyWorld(), this, main)); // Build now to catch any errors immediately.
    }

    public static <C extends AbstractableTemplate, O> void buildAll(ConfigFactory<C, O> factory, OpenRegistry<O> registry, List<C> configTemplates, TerraPlugin main) throws LoadException {
        for(C template : configTemplates) registry.add(template.getID(), factory.build(template, main));
    }

    private void checkDeadEntries(TerraPlugin main) {
        biomeRegistry.getDeadEntries().forEach((id, value) -> main.getDebugLogger().warn("Dead entry in biome registry: '" + id + "'"));
        paletteRegistry.getDeadEntries().forEach((id, value) -> main.getDebugLogger().warn("Dead entry in palette registry: '" + id + "'"));
        floraRegistry.getDeadEntries().forEach((id, value) -> main.getDebugLogger().warn("Dead entry in flora registry: '" + id + "'"));
        carverRegistry.getDeadEntries().forEach((id, value) -> main.getDebugLogger().warn("Dead entry in carver registry: '" + id + "'"));
        treeRegistry.getDeadEntries().forEach((id, value) -> main.getDebugLogger().warn("Dead entry in tree registry: '" + id + "'"));
        oreRegistry.getDeadEntries().forEach((id, value) -> main.getDebugLogger().warn("Dead entry in ore registry: '" + id + "'"));
    }


    private void load(long start, TerraPlugin main) throws ConfigException {
        main.getEventManager().callEvent(new ConfigPackPreLoadEvent(this));

        for(Map.Entry<String, Double> var : template.getVariables().entrySet()) {
            varScope.create(var.getKey(), var.getValue());
        }

        loader.open("structures/data", ".tesf").thenEntries(entries -> {
            for(Map.Entry<String, InputStream> entry : entries) {
                try(InputStream stream = entry.getValue()) {
                    StructureScript structureScript = new StructureScript(stream, main, scriptRegistry, lootRegistry, functionRegistry);
                    scriptRegistry.add(structureScript.getId(), structureScript);
                } catch(com.dfsek.terra.api.structures.parser.exceptions.ParseException | IOException e) {
                    throw new LoadException("Unable to load script \"" + entry.getKey() + "\"", e);
                }
            }
        }).close().open("structures/loot", ".json").thenEntries(entries -> {
            for(Map.Entry<String, InputStream> entry : entries) {
                try {
                    lootRegistry.add(entry.getKey(), new LootTable(IOUtils.toString(entry.getValue(), StandardCharsets.UTF_8), main));
                } catch(ParseException | IOException | NullPointerException e) {
                    throw new LoadException("Unable to load loot table \"" + entry.getKey() + "\"", e);
                }
            }
        }).close();

        loader
                .open("carving", ".yml").then(configs -> buildAll(new CarverFactory(this), carverRegistry, abstractConfigLoader.loadConfigs(configs, CarverTemplate::new), main)).close()
                .open("palettes", ".yml").then(configs -> buildAll(new PaletteFactory(), paletteRegistry, abstractConfigLoader.loadConfigs(configs, PaletteTemplate::new), main)).close()
                .open("ores", ".yml").then(configs -> buildAll(new OreFactory(), oreRegistry, abstractConfigLoader.loadConfigs(configs, OreTemplate::new), main)).close()
                .open("structures/trees", ".yml").then(configs -> buildAll(new TreeFactory(), treeRegistry, abstractConfigLoader.loadConfigs(configs, TreeTemplate::new), main)).close()
                .open("structures/structures", ".yml").then(configs -> buildAll(new StructureFactory(), structureRegistry, abstractConfigLoader.loadConfigs(configs, StructureTemplate::new), main)).close()
                .open("flora", ".yml").then(configs -> buildAll(new FloraFactory(), floraRegistry, abstractConfigLoader.loadConfigs(configs, FloraTemplate::new), main)).close()
                .open("biomes", ".yml").then(configs -> buildAll(new BiomeFactory(this), biomeRegistry, abstractConfigLoader.loadConfigs(configs, () -> new BiomeTemplate(this, main)), main)).close();

        main.getEventManager().callEvent(new ConfigPackPostLoadEvent(this));
        main.logger().info("Loaded config pack \"" + template.getID() + "\" v" + template.getVersion() + " by " + template.getAuthor() + " in " + (System.nanoTime() - start) / 1000000D + "ms.");
    }

    public TerraStructure getStructure(String id) {
        return structureRegistry.get(id);
    }

    public Set<TerraStructure> getStructures() {
        return structureRegistry.entries();
    }

    public List<String> getStructureIDs() {
        return structureRegistry.entries().stream().map(terraStructure -> terraStructure.getTemplate().getID()).collect(Collectors.toList());
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
                .registerLoader(BiomeBuilder.class, biomeRegistry)
                .registerLoader(Flora.class, floraRegistry)
                .registerLoader(Ore.class, oreRegistry)
                .registerLoader(Tree.class, treeRegistry)
                .registerLoader(StructureScript.class, scriptRegistry)
                .registerLoader(TerraStructure.class, structureRegistry)
                .registerLoader(LootTable.class, lootRegistry)
                .registerLoader(UserDefinedCarver.class, carverRegistry)
                .registerLoader(BufferedImage.class, new BufferedImageLoader(loader))
                .registerLoader(NoiseSeeded.class, new NoiseSamplerBuilderLoader(noiseRegistry))
                .registerLoader(SingleBiomeProviderTemplate.class, SingleBiomeProviderTemplate::new)
                .registerLoader(BiomePipelineTemplate.class, () -> new BiomePipelineTemplate(main))
                .registerLoader(ImageProviderTemplate.class, () -> new ImageProviderTemplate(biomeRegistry))
                .registerLoader(ImageSamplerTemplate.class, () -> new ImageProviderTemplate(biomeRegistry));
    }

    public Set<UserDefinedCarver> getCarvers() {
        return carverRegistry.entries();
    }

    public BiomeProvider.BiomeProviderBuilder getBiomeProviderBuilder() {
        return biomeProviderBuilder;
    }

    public CheckedRegistry<StructureScript> getScriptRegistry() {
        return new CheckedRegistry<>(scriptRegistry);
    }

    public CheckedRegistry<BiomeBuilder> getBiomeRegistry() {
        return new CheckedRegistry<>(biomeRegistry);
    }

    public CheckedRegistry<Tree> getTreeRegistry() {
        return new CheckedRegistry<>(treeRegistry);
    }

    public CheckedRegistry<FunctionBuilder<?>> getFunctionRegistry() {
        return new CheckedRegistry<>(functionRegistry);
    }

    public CheckedRegistry<Supplier<ObjectTemplate<NoiseSeeded>>> getNormalizerRegistry() {
        return new CheckedRegistry<>(noiseRegistry);
    }

    public CheckedRegistry<UserDefinedCarver> getCarverRegistry() {
        return new CheckedRegistry<>(carverRegistry);
    }

    public CheckedRegistry<Flora> getFloraRegistry() {
        return new CheckedRegistry<>(floraRegistry);
    }

    public CheckedRegistry<LootTable> getLootRegistry() {
        return new CheckedRegistry<>(lootRegistry);
    }

    public CheckedRegistry<Ore> getOreRegistry() {
        return new CheckedRegistry<>(oreRegistry);
    }

    public CheckedRegistry<Palette<BlockData>> getPaletteRegistry() {
        return new CheckedRegistry<>(paletteRegistry);
    }

    public CheckedRegistry<TerraStructure> getStructureRegistry() {
        return new CheckedRegistry<>(structureRegistry);
    }

    public WorldConfig toWorldConfig(TerraWorld world){
        return new WorldConfig(world, this, main);
    }
}
