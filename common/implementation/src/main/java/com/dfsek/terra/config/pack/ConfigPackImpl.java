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
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.event.events.config.ConfigurationDiscoveryEvent;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.events.config.type.ConfigTypePostLoadEvent;
import com.dfsek.terra.api.event.events.config.type.ConfigTypePreLoadEvent;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.registry.meta.RegistryFactory;
import com.dfsek.terra.api.util.generic.pair.ImmutablePair;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.ChunkGeneratorProvider;
import com.dfsek.terra.api.world.generator.GenerationStageProvider;
import com.dfsek.terra.config.dummy.DummyWorld;
import com.dfsek.terra.config.fileloaders.FolderLoader;
import com.dfsek.terra.config.fileloaders.ZIPLoader;
import com.dfsek.terra.config.loaders.GenericTemplateSupplierLoader;
import com.dfsek.terra.config.loaders.config.BufferedImageLoader;
import com.dfsek.terra.config.preprocessor.MetaListLikePreprocessor;
import com.dfsek.terra.config.preprocessor.MetaValuePreprocessor;
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
import java.lang.reflect.ParameterizedType;
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

    private final BiomeProvider seededBiomeProvider;

    private final Map<Type, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> registryMap = new HashMap<>();

    private final ConfigTypeRegistry configTypeRegistry;


    private final TreeMap<Integer, List<ImmutablePair<String, ConfigType<?, ?>>>> configTypes = new TreeMap<>();

    public ConfigPackImpl(File folder, TerraPlugin main) throws ConfigException {
        try {
            this.loader = new FolderLoader(folder.toPath());
            this.main = main;
            this.configTypeRegistry = createRegistry();
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
                seededBiomeProvider = packPostTemplate.getProviderBuilder();
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
            this.loader = new ZIPLoader(file);
            this.main = main;
            this.configTypeRegistry = createRegistry();
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
                seededBiomeProvider = packPostTemplate.getProviderBuilder();
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

    @SuppressWarnings("unchecked")
    private ConfigTypeRegistry createRegistry() {
        return new ConfigTypeRegistry(main, (id, configType) -> {
            OpenRegistry<?> openRegistry = configType.registrySupplier(this).get();
            if(registryMap.containsKey(configType.getTypeClass().getType())) { // Someone already registered something; we need to copy things to the new registry.
                registryMap.get(configType.getTypeClass().getType()).getLeft().forEach(((OpenRegistry<Object>) openRegistry)::register);
            }
            selfLoader.registerLoader(configType.getTypeClass().getType(), openRegistry);
            abstractConfigLoader.registerLoader(configType.getTypeClass().getType(), openRegistry);
            registryMap.put(configType.getTypeClass().getType(), ImmutablePair.of(openRegistry, new CheckedRegistryImpl<>(openRegistry)));
        });
    }

    private void checkDeadEntries(TerraPlugin main) {
        registryMap.forEach((clazz, pair) -> ((OpenRegistryImpl<?>) pair.getLeft()).getDeadEntries().forEach((id, value) -> main.getDebugLogger().warning("Dead entry in '" + ReflectionUtil.typeToString(clazz) + "' registry: '" + id + "'")));
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

    protected Map<Type, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> getRegistryMap() {
        return registryMap;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void load(long start, TerraPlugin main) throws ConfigException {
        configTypes.values().forEach(list -> list.forEach(pair -> configTypeRegistry.register(pair.getLeft(), pair.getRight())));

        for(Map.Entry<String, Double> var : template.getVariables().entrySet()) {
            varScope.create(var.getKey(), var.getValue());
        }

        Map<String, Configuration> configurations = new HashMap<>();

        main.getEventManager().callEvent(new ConfigurationDiscoveryEvent(this, loader, configurations::put)); // Create all the configs.

        MetaValuePreprocessor valuePreprocessor = new MetaValuePreprocessor(configurations);
        selfLoader.registerPreprocessor(Meta.class, valuePreprocessor);
        abstractConfigLoader.registerPreprocessor(Meta.class, valuePreprocessor);

        MetaListLikePreprocessor listPreprocessor = new MetaListLikePreprocessor(configurations);
        selfLoader.registerPreprocessor(Meta.class, listPreprocessor);
        abstractConfigLoader.registerPreprocessor(Meta.class, listPreprocessor);

        Map<ConfigType<? extends ConfigTemplate, ?>, List<Configuration>> configs = new HashMap<>();

        for(Configuration configuration : configurations.values()) { // Sort the configs
            if(configuration.contains("type")) { // Only sort configs with type key
                ProtoConfig config = new ProtoConfig();
                selfLoader.load(config, configuration);
                configs.computeIfAbsent(config.getType(), configType -> new ArrayList<>()).add(configuration);
            }
        }

        for(ConfigType<?, ?> configType : configTypeRegistry.entries()) { // Load the configs
            CheckedRegistry registry = getCheckedRegistry(configType.getTypeClass());
            main.getEventManager().callEvent(new ConfigTypePreLoadEvent(configType, registry, this));
            for(AbstractConfiguration config : abstractConfigLoader.loadConfigs(configs.getOrDefault(configType, Collections.emptyList()))) {
                try {
                    Object loaded = ((ConfigFactory) configType.getFactory()).build(selfLoader.load(configType.getTemplate(this, main), config), main);
                    registry.register(config.getID(), loaded);
                    main.getEventManager().callEvent(new ConfigurationLoadEvent(this, config, template -> selfLoader.load(template, config), configType, loaded));
                } catch(DuplicateEntryException e) {
                    throw new LoadException("Duplicate registry entry: ", e);
                }
            }
            main.getEventManager().callEvent(new ConfigTypePostLoadEvent(configType, registry, this));
        }

        main.getEventManager().callEvent(new ConfigPackPostLoadEvent(this, template -> selfLoader.load(template, configuration)));
        main.logger().info("Loaded config pack \"" + template.getID() + "\" v" + template.getVersion() + " by " + template.getAuthor() + " in " + (System.nanoTime() - start) / 1000000D + "ms.");
    }

    public ConfigPackTemplate getTemplate() {
        return template;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> CheckedRegistry<T> getRegistry(Type type) {
        return (CheckedRegistry<T>) registryMap.getOrDefault(type, ImmutablePair.ofNull()).getRight();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CheckedRegistry<T> getCheckedRegistry(Type type) throws IllegalStateException {
        return (CheckedRegistry<T>) registryMap.getOrDefault(type, ImmutablePair.ofNull()).getRight();
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
    public BiomeProvider getBiomeProviderBuilder() {
        return seededBiomeProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CheckedRegistry<T> getOrCreateRegistry(Type type) {
        return (CheckedRegistry<T>) registryMap.computeIfAbsent(type, c -> {
            OpenRegistry<T> registry = new OpenRegistryImpl<>();
            selfLoader.registerLoader(c, registry);
            abstractConfigLoader.registerLoader(c, registry);
            main.getDebugLogger().info("Registered loader for registry of class " + ReflectionUtil.typeToString(c));

            if(type instanceof ParameterizedType) {
                ParameterizedType param = (ParameterizedType) type;
                Type base = param.getRawType();
                if(base instanceof Class  // should always be true but we'll check anyways
                        && Supplier.class.isAssignableFrom((Class<?>) base)) { // If it's a supplier
                    Type supplied = param.getActualTypeArguments()[0]; // Grab the supplied type
                    if(supplied instanceof ParameterizedType) {
                        ParameterizedType suppliedParam = (ParameterizedType) supplied;
                        Type suppliedBase = suppliedParam.getRawType();
                        if(suppliedBase instanceof Class // should always be true but we'll check anyways
                                && ObjectTemplate.class.isAssignableFrom((Class<?>) suppliedBase)) {
                            Type templateType = suppliedParam.getActualTypeArguments()[0];
                            GenericTemplateSupplierLoader<?> loader = new GenericTemplateSupplierLoader<>((Registry<Supplier<ObjectTemplate<Supplier<ObjectTemplate<?>>>>>) registry);
                            selfLoader.registerLoader(templateType, loader);
                            abstractConfigLoader.registerLoader(templateType, loader);
                            main.getDebugLogger().info("Registered template loader for registry of class " + ReflectionUtil.typeToString(templateType));
                        }
                    }
                }
            }

            return ImmutablePair.of(registry, new CheckedRegistryImpl<>(registry));
        }).getRight();
    }


    @Override
    public WorldConfigImpl toWorldConfig(TerraWorld world) {
        return new WorldConfigImpl(world, this, main);
    }

    @Override
    public List<GenerationStageProvider> getStages() {
        return template.getStages();
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
