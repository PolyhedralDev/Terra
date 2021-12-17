/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.config.pack;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.AbstractConfigLoader;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.tectonic.impl.abstraction.AbstractConfiguration;
import com.dfsek.tectonic.yaml.YamlConfiguration;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.config.*;
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
import com.dfsek.terra.api.util.generic.Construct;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.provider.ChunkGeneratorProvider;
import com.dfsek.terra.config.fileloaders.FolderLoader;
import com.dfsek.terra.config.fileloaders.ZIPLoader;
import com.dfsek.terra.config.loaders.GenericTemplateSupplierLoader;
import com.dfsek.terra.config.loaders.config.BufferedImageLoader;
import com.dfsek.terra.config.preprocessor.*;
import com.dfsek.terra.config.prototype.ProtoConfig;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;
import com.dfsek.terra.registry.ShortcutHolder;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Represents a Terra configuration pack.
 */
public class ConfigPackImpl implements ConfigPack {
    private static final Logger logger = LoggerFactory.getLogger(ConfigPackImpl.class);
    
    private final ConfigPackTemplate template = new ConfigPackTemplate();
    
    private final AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();
    private final ConfigLoader selfLoader = new ConfigLoader();
    private final Platform platform;
    private final Loader loader;
    
    private final Map<BaseAddon, VersionRange> addons;
    
    private final BiomeProvider seededBiomeProvider;
    
    private final Map<Type, CheckedRegistryImpl<?>> registryMap = new HashMap<>();
    private final Map<Type, ShortcutHolder<?>> shortcuts = new HashMap<>();
    
    private final OpenRegistry<ConfigType<?, ?>> configTypeRegistry;
    private final TreeMap<Integer, List<Pair<String, ConfigType<?, ?>>>> configTypes = new TreeMap<>();
    
    public ConfigPackImpl(File folder, Platform platform) {
        this(new FolderLoader(folder.toPath()), Construct.construct(() -> {
            try {
                return new YamlConfiguration(new FileInputStream(new File(folder, "pack.yml")), "pack.yml");
            } catch(FileNotFoundException e) {
                throw new LoadException("No pack.yml file found in " + folder.getAbsolutePath());
            }
        }), platform);
    }
    
    public ConfigPackImpl(ZipFile file, Platform platform) {
        this(new ZIPLoader(file), Construct.construct(() -> {
            ZipEntry pack = null;
            Enumeration<? extends ZipEntry> entries = file.entries();
            while(entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if(entry.getName().equals("pack.yml")) pack = entry;
            }
            
            if(pack == null) throw new LoadException("No pack.yml file found in " + file.getName());
            
            try {
                return new YamlConfiguration(file.getInputStream(pack), "pack.yml");
            } catch(IOException e) {
                throw new LoadException("Unable to load pack.yml from ZIP file", e);
            }
        }), platform);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private ConfigPackImpl(Loader loader, Configuration packManifest, Platform platform) {
        long start = System.nanoTime();
        
        this.loader = loader;
        this.platform = platform;
        this.configTypeRegistry = createConfigRegistry();
        
        register(selfLoader);
        platform.register(selfLoader);
        
        register(abstractConfigLoader);
        platform.register(abstractConfigLoader);
        
        ConfigPackAddonsTemplate addonsTemplate = new ConfigPackAddonsTemplate();
        selfLoader.load(addonsTemplate, packManifest);
        this.addons = addonsTemplate.getAddons();
        
        Map<String, Configuration> configurations = discoverConfigurations();
        registerMeta(configurations);
        
        platform.getEventManager().callEvent(
                new ConfigPackPreLoadEvent(this, template -> selfLoader.load(template, packManifest)));
        
        selfLoader.load(template, packManifest);
        
        logger.info("Loading config pack \"{}\"", template.getID());
        
        configTypes.values().forEach(list -> list.forEach(pair -> configTypeRegistry.register(pair.getLeft(), pair.getRight())));
        
        Map<ConfigType<? extends ConfigTemplate, ?>, List<Configuration>> configs = new HashMap<>();
        
        for(Configuration configuration : configurations.values()) { // Sort the configs
            if(configuration.contains("type")) { // Only sort configs with type key
                ProtoConfig config = new ProtoConfig();
                selfLoader.load(config, configuration);
                configs.computeIfAbsent(config.getType(), configType -> new ArrayList<>()).add(configuration);
            }
        }
        
        for(ConfigType<?, ?> configType : configTypeRegistry.entries()) { // Load the configs
            CheckedRegistry registry = getCheckedRegistry(configType.getTypeKey());
            platform.getEventManager().callEvent(new ConfigTypePreLoadEvent(configType, registry, this));
            for(AbstractConfiguration config : abstractConfigLoader.loadConfigs(
                    configs.getOrDefault(configType, Collections.emptyList()))) {
                try {
                    Object loaded = ((ConfigFactory) configType.getFactory()).build(
                            selfLoader.load(configType.getTemplate(this, platform), config), platform);
                    registry.register(config.getID(), loaded);
                    platform.getEventManager().callEvent(
                            new ConfigurationLoadEvent(this, config, template -> selfLoader.load(template, config), configType, loaded));
                } catch(DuplicateEntryException e) {
                    throw new LoadException("Duplicate registry entry: ", e);
                }
            }
            platform.getEventManager().callEvent(new ConfigTypePostLoadEvent(configType, registry, this));
        }
        
        platform.getEventManager().callEvent(new ConfigPackPostLoadEvent(this, template -> selfLoader.load(template, packManifest)));
        logger.info("Loaded config pack \"{}\" v{} by {} in {}ms.",
                    template.getID(), template.getVersion(), template.getAuthor(), (System.nanoTime() - start) / 1000000.0D);
        
        
        ConfigPackPostTemplate packPostTemplate = new ConfigPackPostTemplate();
        selfLoader.load(packPostTemplate, packManifest);
        seededBiomeProvider = packPostTemplate.getProviderBuilder();
        checkDeadEntries();
    }
    
    private Map<String, Configuration> discoverConfigurations() {
        Map<String, Configuration> configurations = new HashMap<>();
        platform.getEventManager().callEvent(new ConfigurationDiscoveryEvent(this, loader,
                                                                             (s, c) -> configurations.put(s.replace("\\", "/"),
                                                                                                          c))); // Create all the configs.
        return configurations;
    }
    
    private void registerMeta(Map<String, Configuration> configurations) {
        MetaStringPreprocessor stringPreprocessor = new MetaStringPreprocessor(configurations);
        selfLoader.registerPreprocessor(Meta.class, stringPreprocessor);
        abstractConfigLoader.registerPreprocessor(Meta.class, stringPreprocessor);
        
        MetaListLikePreprocessor listPreprocessor = new MetaListLikePreprocessor(configurations);
        selfLoader.registerPreprocessor(Meta.class, listPreprocessor);
        abstractConfigLoader.registerPreprocessor(Meta.class, listPreprocessor);
        
        MetaMapPreprocessor mapPreprocessor = new MetaMapPreprocessor(configurations);
        selfLoader.registerPreprocessor(Meta.class, mapPreprocessor);
        abstractConfigLoader.registerPreprocessor(Meta.class, mapPreprocessor);
        
        MetaValuePreprocessor valuePreprocessor = new MetaValuePreprocessor(configurations);
        selfLoader.registerPreprocessor(Meta.class, valuePreprocessor);
        abstractConfigLoader.registerPreprocessor(Meta.class, valuePreprocessor);
        
        MetaNumberPreprocessor numberPreprocessor = new MetaNumberPreprocessor(configurations);
        selfLoader.registerPreprocessor(Meta.class, numberPreprocessor);
        abstractConfigLoader.registerPreprocessor(Meta.class, numberPreprocessor);
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
    
    @Override
    public void register(TypeRegistry registry) {
        registry.registerLoader(ConfigType.class, configTypeRegistry)
                .registerLoader(BufferedImage.class, new BufferedImageLoader(loader));
        registryMap.forEach(registry::registerLoader);
        shortcuts.forEach(registry::registerLoader); // overwrite with delegated shortcuts if present
    }
    
    @Override
    public ConfigPack registerConfigType(ConfigType<?, ?> type, String id, int priority) {
        Set<String> contained = new HashSet<>();
        configTypes.forEach((p, configs) -> configs.forEach(pair -> {
            if(contained.contains(pair.getLeft())) throw new IllegalArgumentException("Duplicate config ID: " + id);
            contained.add(id);
        }));
        configTypes.computeIfAbsent(priority, p -> new ArrayList<>()).add(Pair.of(id, type));
        return this;
    }
    
    @Override
    public Map<BaseAddon, VersionRange> addons() {
        return addons;
    }
    
    @Override
    public BiomeProvider getBiomeProvider() {
        return seededBiomeProvider;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> CheckedRegistry<T> getOrCreateRegistry(Type type) {
        return (CheckedRegistry<T>) registryMap.computeIfAbsent(type, c -> {
            OpenRegistry<T> registry = new OpenRegistryImpl<>();
            selfLoader.registerLoader(c, registry);
            abstractConfigLoader.registerLoader(c, registry);
            logger.debug("Registered loader for registry of class {}", ReflectionUtil.typeToString(c));
            
            if(type instanceof ParameterizedType param) {
                Type base = param.getRawType();
                if(base instanceof Class  // should always be true but we'll check anyways
                   && Supplier.class.isAssignableFrom((Class<?>) base)) { // If it's a supplier
                    Type supplied = param.getActualTypeArguments()[0]; // Grab the supplied type
                    if(supplied instanceof ParameterizedType suppliedParam) {
                        Type suppliedBase = suppliedParam.getRawType();
                        if(suppliedBase instanceof Class // should always be true but we'll check anyways
                           && ObjectTemplate.class.isAssignableFrom((Class<?>) suppliedBase)) {
                            Type templateType = suppliedParam.getActualTypeArguments()[0];
                            GenericTemplateSupplierLoader<?> loader = new GenericTemplateSupplierLoader<>(
                                    (Registry<Supplier<ObjectTemplate<Supplier<ObjectTemplate<?>>>>>) registry);
                            selfLoader.registerLoader(templateType, loader);
                            abstractConfigLoader.registerLoader(templateType, loader);
                            logger.debug("Registered template loader for registry of class {}", ReflectionUtil.typeToString(templateType));
                        }
                    }
                }
            }
            
            return new CheckedRegistryImpl<>(registry);
        });
    }
    
    @Override
    public List<GenerationStage> getStages() {
        return template.getStages();
    }
    
    @Override
    public Loader getLoader() {
        return loader;
    }
    
    @Override
    public String getAuthor() {
        return template.getAuthor();
    }
    
    @Override
    public Version getVersion() {
        return template.getVersion();
    }
    
    @SuppressWarnings("unchecked,rawtypes")
    @Override
    public <T> ConfigPack registerShortcut(Type clazz, String shortcut, ShortcutLoader<T> loader) {
        ShortcutHolder<?> holder = shortcuts
                .computeIfAbsent(clazz, c -> new ShortcutHolder<>(getOrCreateRegistry(clazz)))
                .register(shortcut, (ShortcutLoader) loader);
        selfLoader.registerLoader(clazz, holder);
        abstractConfigLoader.registerLoader(clazz, holder);
        return this;
    }
    
    @Override
    public ChunkGeneratorProvider getGeneratorProvider() {
        return template.getGeneratorProvider();
    }
    
    private OpenRegistry<ConfigType<?, ?>> createConfigRegistry() {
        return new OpenRegistryImpl<>(new LinkedHashMap<>()) {
            @Override
            public boolean register(@NotNull String identifier, @NotNull ConfigType<?, ?> value) {
                if(!registryMap
                        .containsKey(value.getTypeKey()
                                          .getType())) {
                    OpenRegistry<?> openRegistry = new OpenRegistryImpl<>();
                    selfLoader.registerLoader(value.getTypeKey().getType(), openRegistry);
                    abstractConfigLoader.registerLoader(value.getTypeKey().getType(), openRegistry);
                    registryMap.put(value.getTypeKey().getType(), new CheckedRegistryImpl<>(openRegistry));
                }
                return super.register(identifier, value);
            }
        };
    }
    
    private void checkDeadEntries() {
        registryMap.forEach((clazz, pair) -> ((OpenRegistryImpl<?>) pair.getRegistry())
                .getDeadEntries()
                .forEach((id, value) -> logger.debug("Dead entry in '{}' registry: '{}'", ReflectionUtil.typeToString(clazz), id)));
    }
    
    public ConfigPackTemplate getTemplate() {
        return template;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> CheckedRegistry<T> getRegistry(Type type) {
        return (CheckedRegistry<T>) registryMap.get(type);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> CheckedRegistry<T> getCheckedRegistry(Type type) throws IllegalStateException {
        return (CheckedRegistry<T>) registryMap.get(type);
    }
    
    @Override
    public String getID() {
        return template.getID();
    }
}
