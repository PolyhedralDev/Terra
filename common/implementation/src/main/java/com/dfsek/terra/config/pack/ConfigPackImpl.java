package com.dfsek.terra.config.pack;

import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.abstraction.AbstractConfiguration;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.config.Loader;
import com.dfsek.terra.api.event.events.config.ConfigLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.registry.meta.RegistryFactory;
import com.dfsek.terra.api.util.generic.pair.ImmutablePair;
import com.dfsek.terra.api.util.seeded.BiomeProviderBuilder;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.generator.ChunkGeneratorProvider;
import com.dfsek.terra.config.dummy.DummyWorld;
import com.dfsek.terra.config.fileloaders.FolderLoader;
import com.dfsek.terra.config.fileloaders.ZIPLoader;
import com.dfsek.terra.config.loaders.config.BufferedImageLoader;
import com.dfsek.terra.config.prototype.ProtoConfig;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.registry.RegistryFactoryImpl;
import com.dfsek.terra.registry.config.ConfigTypeRegistry;
import com.dfsek.terra.world.TerraWorldImpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Represents a Terra configuration pack.
 */
public class ConfigPackImpl implements ConfigPack {
    private final ConfigPackTemplate template = new ConfigPackTemplate();

    private final RegistryFactory registryFactory = new RegistryFactoryImpl();

    private final AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();
    private final ConfigLoader selfLoader = new ConfigLoader();
    private final Scope varScope = new Scope();
    private final TerraPlugin main;
    private final Loader loader;

    private final Configuration configuration;

    private final Set<TerraAddon> addons;

    private final BiomeProviderBuilder biomeProviderBuilder;


    private final ConfigTypeRegistry configTypeRegistry;
    private final Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> registryMap = new HashMap<>();

    private final TreeMap<Integer, List<ImmutablePair<String, ConfigType<?, ?>>>> configTypes = new TreeMap<>();

    public ConfigPackImpl(File folder, TerraPlugin main) throws ConfigException {
        try {
            this.configTypeRegistry = new ConfigTypeRegistry(main, (id, configType) -> {
                OpenRegistry<?> openRegistry = configType.registrySupplier().get();
                selfLoader.registerLoader(configType.getTypeClass(), openRegistry);
                abstractConfigLoader.registerLoader(configType.getTypeClass(), openRegistry);
                registryMap.put(configType.getTypeClass(), ImmutablePair.of(openRegistry, new CheckedRegistryImpl<>(openRegistry)));
            });
            this.loader = new FolderLoader(folder.toPath());
            this.main = main;
            long l = System.nanoTime();

            register(abstractConfigLoader);
            main.register(abstractConfigLoader);

            register(selfLoader);
            main.register(selfLoader);

            File pack = new File(folder, "pack.yml");

            try {
                this.configuration = new YamlConfiguration(new FileInputStream(pack), "pack.yml");

                ConfigPackAddonsTemplate addonsTemplate = new ConfigPackAddonsTemplate();
                selfLoader.load(addonsTemplate, configuration);
                this.addons = addonsTemplate.getAddons();

                main.getEventManager().callEvent(new ConfigPackPreLoadEvent(this, template -> selfLoader.load(template, configuration)));

                selfLoader.load(template, configuration);

                main.logger().info("Loading config pack \"" + template.getID() + "\"");
                load(l, main);

                ConfigPackPostTemplate packPostTemplate = new ConfigPackPostTemplate();
                selfLoader.load(packPostTemplate, configuration);
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
            this.configTypeRegistry = new ConfigTypeRegistry(main, (id, configType) -> {
                OpenRegistry<?> openRegistry = configType.registrySupplier().get();
                selfLoader.registerLoader(configType.getTypeClass(), openRegistry);
                abstractConfigLoader.registerLoader(configType.getTypeClass(), openRegistry);
                registryMap.put(configType.getTypeClass(), ImmutablePair.of(openRegistry, new CheckedRegistryImpl<>(openRegistry)));
            });
            this.loader = new ZIPLoader(file);
            this.main = main;
            long l = System.nanoTime();

            register(selfLoader);
            main.register(selfLoader);

            register(abstractConfigLoader);
            main.register(abstractConfigLoader);

            try {
                ZipEntry pack = null;
                Enumeration<? extends ZipEntry> entries = file.entries();
                while(entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if(entry.getName().equals("pack.yml")) pack = entry;
                }

                if(pack == null) throw new LoadException("No pack.yml file found in " + file.getName());

                this.configuration = new YamlConfiguration(file.getInputStream(pack), "pack.yml");

                ConfigPackAddonsTemplate addonsTemplate = new ConfigPackAddonsTemplate();
                selfLoader.load(addonsTemplate, configuration);
                this.addons = addonsTemplate.getAddons();

                main.getEventManager().callEvent(new ConfigPackPreLoadEvent(this, template -> selfLoader.load(template, configuration)));


                selfLoader.load(template, configuration);
                main.logger().info("Loading config pack \"" + template.getID() + "\"");

                load(l, main);

                ConfigPackPostTemplate packPostTemplate = new ConfigPackPostTemplate();

                selfLoader.load(packPostTemplate, configuration);
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

    private void checkDeadEntries(TerraPlugin main) {
        registryMap.forEach((clazz, pair) -> ((OpenRegistryImpl<?>) pair.getLeft()).getDeadEntries().forEach((id, value) -> main.getDebugLogger().warning("Dead entry in '" + clazz + "' registry: '" + id + "'")));
    }

    @Override
    public <T> ConfigPackImpl applyLoader(Type type, TypeLoader<T> loader) {
        abstractConfigLoader.registerLoader(type, loader);
        selfLoader.registerLoader(type, loader);
        return this;
    }

    @Override
    public <T> ConfigPackImpl applyLoader(Type type, Supplier<ObjectTemplate<T>> loader) {
        abstractConfigLoader.registerLoader(type, loader);
        selfLoader.registerLoader(type, loader);
        return this;
    }

    protected Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> getRegistryMap() {
        return registryMap;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void load(long start, TerraPlugin main) throws ConfigException {
        configTypes.values().forEach(list -> list.forEach(pair -> configTypeRegistry.register(pair.getLeft(), pair.getRight())));

        for(Map.Entry<String, Double> var : template.getVariables().entrySet()) {
            varScope.create(var.getKey(), var.getValue());
        }

        List<Configuration> configurations = new ArrayList<>();

        main.getEventManager().callEvent(new ConfigurationLoadEvent(this, loader, configurations::add));

        Map<ConfigType<? extends ConfigTemplate, ?>, List<Configuration>> configs = new HashMap<>();

        for(Configuration configuration : configurations) {
            ProtoConfig config = new ProtoConfig();
            selfLoader.load(config, configuration);
            configs.computeIfAbsent(config.getType(), configType -> new ArrayList<>()).add(configuration);
        }

        for(ConfigType<?, ?> configType : configTypeRegistry.entries()) {
            CheckedRegistry registry = getCheckedRegistry(configType.getTypeClass());
            for(AbstractConfiguration config : abstractConfigLoader.loadConfigs(configs.getOrDefault(configType, Collections.emptyList()))) {
                try {
                    Object loaded = ((ConfigFactory) configType.getFactory()).build(selfLoader.load(configType.getTemplate(this, main), config), main);
                    registry.register(config.getID(), loaded);
                    main.getEventManager().callEvent(new ConfigLoadEvent(this, config, template -> selfLoader.load(template, configuration), configType, loaded));
                } catch(DuplicateEntryException e) {
                    throw new LoadException("Duplicate registry entry: ", e);
                }
            }
        }

        main.getEventManager().callEvent(new ConfigPackPostLoadEvent(this, template -> selfLoader.load(template, configuration)));
        main.logger().info("Loaded config pack \"" + template.getID() + "\" v" + template.getVersion() + " by " + template.getAuthor() + " in " + (System.nanoTime() - start) / 1000000D + "ms.");
    }

    public ConfigPackTemplate getTemplate() {
        return template;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> CheckedRegistry<T> getRegistry(Class<T> clazz) {
        return (CheckedRegistry<T>) registryMap.getOrDefault(clazz, ImmutablePair.ofNull()).getRight();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CheckedRegistry<T> getCheckedRegistry(Class<T> clazz) throws IllegalStateException {
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
                .registerLoader(BufferedImage.class, new BufferedImageLoader(loader));
        registryMap.forEach((clazz, reg) -> registry.registerLoader(clazz, reg.getLeft()));
    }

    @Override
    public BiomeProviderBuilder getBiomeProviderBuilder() {
        return biomeProviderBuilder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CheckedRegistry<T> getOrCreateRegistry(Class<T> clazz) {
        return (CheckedRegistry<T>) registryMap.computeIfAbsent(clazz, c -> {
            OpenRegistry<T> registry = new OpenRegistryImpl<>();
            selfLoader.registerLoader(c, registry);
            abstractConfigLoader.registerLoader(c, registry);
            main.getDebugLogger().info("Registered loader for registry of class " + c);
            return ImmutablePair.of(registry, new CheckedRegistryImpl<>(registry));
        }).getRight();
    }


    @Override
    public WorldConfigImpl toWorldConfig(TerraWorld world) {
        return new WorldConfigImpl(world, this, main);
    }

    @Override
    public void registerConfigType(ConfigType<?, ?> type, String id, int priority) {
        Set<String> contained = new HashSet<>();
        configTypes.forEach((p, configs) -> configs.forEach(pair -> {
            if(contained.contains(pair.getLeft())) throw new IllegalArgumentException("Duplicate config ID: " + id);
            contained.add(id);
        }));
        configTypes.computeIfAbsent(priority, p -> new ArrayList<>()).add(ImmutablePair.of(id, type));
    }

    @Override
    public Loader getLoader() {
        return loader;
    }

    @Override
    public Set<TerraAddon> addons() {
        return addons;
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

    @Override
    public boolean vanillaMobs() {
        return template.vanillaMobs();
    }

    @Override
    public boolean vanillaStructures() {
        return template.vanillaStructures();
    }

    @Override
    public boolean vanillaCaves() {
        return template.vanillaCaves();
    }

    @Override
    public boolean disableStructures() {
        return template.disableStructures();
    }

    @Override
    public Map<String, String> getLocatable() {
        return template.getLocatable();
    }

    @Override
    public boolean doBetaCarvers() {
        return template.doBetaCarvers();
    }

    @Override
    public boolean vanillaFlora() {
        return template.vanillaDecorations();
    }

    @Override
    public RegistryFactory getRegistryFactory() {
        return registryFactory;
    }

    @Override
    public ChunkGeneratorProvider getGeneratorProvider() {
        return template.getGeneratorProvider();
    }
}
