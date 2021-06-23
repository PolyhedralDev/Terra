package com.dfsek.terra.config.pack;

import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.config.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structure.LootTable;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structures.loot.LootTableImpl;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.generic.pair.ImmutablePair;
import com.dfsek.terra.api.util.seeded.NoiseProvider;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.dummy.DummyWorld;
import com.dfsek.terra.config.fileloaders.FolderLoader;
import com.dfsek.terra.config.fileloaders.Loader;
import com.dfsek.terra.config.fileloaders.ZIPLoader;
import com.dfsek.terra.config.loaders.config.BufferedImageLoader;
import com.dfsek.terra.config.loaders.config.biome.templates.provider.BiomePipelineTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.provider.ImageProviderTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.provider.SingleBiomeProviderTemplate;
import com.dfsek.terra.config.loaders.config.sampler.NoiseSamplerBuilderLoader;
import com.dfsek.terra.config.loaders.config.sampler.templates.ImageSamplerTemplate;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.config.prototype.ProtoConfig;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.registry.config.ConfigTypeRegistry;
import com.dfsek.terra.registry.config.FunctionRegistry;
import com.dfsek.terra.registry.config.LootRegistry;
import com.dfsek.terra.registry.config.NoiseRegistry;
import com.dfsek.terra.registry.config.ScriptRegistry;
import com.dfsek.terra.world.TerraWorldImpl;
import com.dfsek.terra.world.population.items.TerraStructure;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Represents a Terra configuration pack.
 */
public class ConfigPackImpl implements ConfigPack {
    private final ConfigPackTemplate template = new ConfigPackTemplate();

    private final AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();
    private final ConfigLoader selfLoader = new ConfigLoader();
    private final Scope varScope = new Scope();
    private final TerraPlugin main;
    private final Loader loader;

    private final Configuration configuration;

    private final BiomeProvider.BiomeProviderBuilder biomeProviderBuilder;


    private final ConfigTypeRegistry configTypeRegistry;
    private final Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> registryMap = newRegistryMap();

    public ConfigPackImpl(File folder, TerraPlugin main) throws ConfigException {
        try {
            this.configTypeRegistry = new ConfigTypeRegistry(this, main, (id, configType) -> {
                OpenRegistry<?> openRegistry = configType.registrySupplier().get();
                registryMap.put(configType.getTypeClass(), ImmutablePair.of(openRegistry, new CheckedRegistryImpl<>(openRegistry)));
            });
            this.loader = new FolderLoader(folder.toPath());
            this.main = main;
            long l = System.nanoTime();

            register(abstractConfigLoader);
            register(selfLoader);

            main.register(selfLoader);
            main.register(abstractConfigLoader);

            File pack = new File(folder, "pack.yml");

            try {
                configuration = new Configuration(new FileInputStream(pack));
                selfLoader.load(template, configuration);

                main.logger().info("Loading config pack \"" + template.getID() + "\"");

                main.getEventManager().callEvent(new ConfigPackPreLoadEvent(this, template -> selfLoader.load(template, configuration)));

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
        toWorldConfig(new TerraWorldImpl(new DummyWorld(), this, main)); // Build now to catch any errors immediately.
    }

    public ConfigPackImpl(ZipFile file, TerraPlugin main) throws ConfigException {
        try {
            this.configTypeRegistry = new ConfigTypeRegistry(this, main, (id, configType) -> {
                OpenRegistry<?> openRegistry = configType.registrySupplier().get();
                registryMap.put(configType.getTypeClass(), ImmutablePair.of(openRegistry, new CheckedRegistryImpl<>(openRegistry)));
            });
            this.loader = new ZIPLoader(file);
            this.main = main;
            long l = System.nanoTime();

            register(selfLoader);
            main.register(selfLoader);

            try {
                ZipEntry pack = null;
                Enumeration<? extends ZipEntry> entries = file.entries();
                while(entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if(entry.getName().equals("pack.yml")) pack = entry;
                }

                if(pack == null) throw new LoadException("No pack.yml file found in " + file.getName());

                configuration = new Configuration(file.getInputStream(pack));
                selfLoader.load(template, configuration);
                main.logger().info("Loading config pack \"" + template.getID() + "\"");

                main.getEventManager().callEvent(new ConfigPackPreLoadEvent(this, template -> selfLoader.load(template, configuration)));

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

        toWorldConfig(new TerraWorldImpl(new DummyWorld(), this, main)); // Build now to catch any errors immediately.
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> newRegistryMap() {
        Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> map = new HashMap<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>>() {
            private static final long serialVersionUID = 4015855819914064466L;

            @Override
            public ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>> put(Class<?> key, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>> value) {
                selfLoader.registerLoader(key, value.getLeft());
                abstractConfigLoader.registerLoader(key, value.getLeft());
                return super.put(key, value);
            }
        };

        putPair(map, NoiseProvider.class, new NoiseRegistry());
        putPair(map, FunctionBuilder.class, (OpenRegistry<FunctionBuilder>) (Object) new FunctionRegistry());
        putPair(map, LootTable.class, new LootRegistry());
        putPair(map, Structure.class, new ScriptRegistry());

        return map;
    }

    private <R> void putPair(Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> map, Class<R> key, OpenRegistry<R> l) {
        map.put(key, ImmutablePair.of(l, new CheckedRegistryImpl<>(l)));
    }

    private void checkDeadEntries(TerraPlugin main) {
        registryMap.forEach((clazz, pair) -> ((OpenRegistryImpl<?>) pair.getLeft()).getDeadEntries().forEach((id, value) -> main.getDebugLogger().warning("Dead entry in '" + clazz + "' registry: '" + id + "'")));
    }

    protected Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> getRegistryMap() {
        return registryMap;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void load(long start, TerraPlugin main) throws ConfigException {
        for(Map.Entry<String, Double> var : template.getVariables().entrySet()) {
            varScope.create(var.getKey(), var.getValue());
        }

        loader.open("", ".tesf").thenEntries(entries -> {
            for(Map.Entry<String, InputStream> entry : entries) {
                try(InputStream stream = entry.getValue()) {
                    Structure structure = new StructureScript(stream, main, getRegistry(Structure.class), getRegistry(LootTable.class), (Registry<FunctionBuilder<?>>) (Object) getRegistry(FunctionBuilder.class));
                    getOpenRegistry(Structure.class).add(structure.getId(), structure);
                } catch(com.dfsek.terra.api.structures.parser.exceptions.ParseException | IOException e) {
                    throw new LoadException("Unable to load script \"" + entry.getKey() + "\"", e);
                }
            }
        }).close().open("structures/loot", ".json").thenEntries(entries -> {
            for(Map.Entry<String, InputStream> entry : entries) {
                try {
                    getOpenRegistry(LootTableImpl.class).add(entry.getKey(), new LootTableImpl(IOUtils.toString(entry.getValue(), StandardCharsets.UTF_8), main));
                } catch(ParseException | IOException | NullPointerException e) {
                    throw new LoadException("Unable to load loot table \"" + entry.getKey() + "\"", e);
                }
            }
        }).close();

        List<Configuration> configurations = new ArrayList<>();

        loader.open("", ".yml").thenEntries(entries -> entries.forEach(stream -> configurations.add(new Configuration(stream.getValue(), stream.getKey()))));

        Map<ConfigType<? extends ConfigTemplate, ?>, List<Configuration>> configs = new HashMap<>();

        for(Configuration configuration : configurations) {
            ProtoConfig config = new ProtoConfig();
            selfLoader.load(config, configuration);
            configs.computeIfAbsent(config.getType(), configType -> new ArrayList<>()).add(configuration);
        }

        for(ConfigType<?, ?> configType : configTypeRegistry.entries()) {
            for(ConfigTemplate config : abstractConfigLoader.loadConfigs(configs.getOrDefault(configType, Collections.emptyList()), () -> configType.getTemplate(this, main))) {
                ((ConfigType) configType).callback(this, main, config);
            }
        }

        main.getEventManager().callEvent(new ConfigPackPostLoadEvent(this, template -> selfLoader.load(template, configuration)));
        main.logger().info("Loaded config pack \"" + template.getID() + "\" v" + template.getVersion() + " by " + template.getAuthor() + " in " + (System.nanoTime() - start) / 1000000D + "ms.");
    }


    public Set<TerraStructure> getStructures() {
        return new HashSet<>(getRegistry(TerraStructure.class).entries());
    }

    public ConfigPackTemplate getTemplate() {
        return template;
    }

    public Scope getVarScope() {
        return varScope;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> CheckedRegistry<T> getRegistry(Class<T> clazz) {
        return (CheckedRegistry<T>) registryMap.getOrDefault(clazz, ImmutablePair.ofNull()).getRight();
    }

    @SuppressWarnings("unchecked")
    protected <T> OpenRegistry<T> getOpenRegistry(Class<T> clazz) {
        return (OpenRegistry<T>) registryMap.getOrDefault(clazz, ImmutablePair.ofNull()).getLeft();
    }


    @Override
    public void register(TypeRegistry registry) {
        registry
                .registerLoader(ConfigType.class, configTypeRegistry)
                .registerLoader(BufferedImage.class, new BufferedImageLoader(loader))
                .registerLoader(SingleBiomeProviderTemplate.class, SingleBiomeProviderTemplate::new)
                .registerLoader(BiomePipelineTemplate.class, () -> new BiomePipelineTemplate(main))
                .registerLoader(ImageProviderTemplate.class, () -> new ImageProviderTemplate(getRegistry(BiomeBuilder.class)))
                .registerLoader(ImageSamplerTemplate.class, () -> new ImageProviderTemplate(getRegistry(BiomeBuilder.class)))
                .registerLoader(NoiseSeeded.class, new NoiseSamplerBuilderLoader(getOpenRegistry(NoiseProvider.class)));
    }

    @Override
    public BiomeProvider.BiomeProviderBuilder getBiomeProviderBuilder() {
        return biomeProviderBuilder;
    }


    @Override
    public WorldConfigImpl toWorldConfig(TerraWorld world) {
        return new WorldConfigImpl(world, this, main);
    }

    @Override
    public CheckedRegistry<ConfigType<?, ?>> getConfigTypeRegistry() {
        return new CheckedRegistryImpl<>(configTypeRegistry) {
            @Override
            @SuppressWarnings("deprecation")
            public void addUnchecked(String identifier, ConfigType<?, ?> value) {
                if(contains(identifier)) throw new UnsupportedOperationException("Cannot override values in ConfigTypeRegistry!");
            }
        };
    }

    @Override
    public Set<TerraAddon> addons() {
        return template.getAddons();
    }

    @Override
    public String getID() {
        return template.getID();
    }

    @Override
    public String getAuthor() {
        return template.getAuthor();
    }

    @Override
    public String getVersion() {
        return template.getVersion();
    }
}
