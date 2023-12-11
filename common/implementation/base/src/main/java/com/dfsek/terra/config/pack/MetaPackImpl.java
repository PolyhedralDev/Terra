package com.dfsek.terra.config.pack;

import ca.solostudios.strata.version.Version;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.dfsek.tectonic.api.TypeRegistry;
import com.dfsek.tectonic.api.config.Configuration;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.loader.AbstractConfigLoader;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.config.MetaPack;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.tectonic.ConfigLoadingDelegate;
import com.dfsek.terra.api.util.reflection.ReflectionUtil;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.config.loaders.GenericTemplateSupplierLoader;
import com.dfsek.terra.config.loaders.config.BufferedImageLoader;
import com.dfsek.terra.registry.CheckedRegistryImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MetaPackImpl implements MetaPack {

    private final MetaPackTemplate template = new MetaPackTemplate();

    private final Platform platform;

    private final Path rootPath;

    private final Map<String, ConfigPack> packs = new HashMap<>();

    private final ConfigLoader selfLoader = new ConfigLoader();

    private final Context context = new Context();

    private final RegistryKey key;

    private final Map<Type, CheckedRegistryImpl<?>> registryMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MetaPackImpl.class);

    private final AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();

    public MetaPackImpl(Path path, Platform platform) throws IOException {
        this.rootPath = path;
        this.platform = platform;

        Path packManifestPath = rootPath.resolve("metapack.yml");
        if(Files.notExists(packManifestPath)) throw new IOException("No metapack.yml found in " + path);
        Configuration packManifest = new YamlConfiguration(Files.newInputStream(packManifestPath),
            packManifestPath.getFileName().toString());

        register(selfLoader);
        platform.register(selfLoader);

        register(abstractConfigLoader);
        platform.register(abstractConfigLoader);

        selfLoader.load(template, packManifest);

        String namespace;
        String id;
        if(template.getID().contains(":")) {
            namespace = template.getID().substring(0, template.getID().indexOf(":"));
            id = template.getID().substring(template.getID().indexOf(":") + 1);
        } else {
            id = template.getID();
            namespace = template.getID();
        }

        this.key = RegistryKey.of(namespace, id);

        try {
            template.getPacks().forEach((k, v) -> {
                try {
                    packs.put(k, new ConfigPackImpl(rootPath.resolve(v), platform));
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch(RuntimeException e) {
            throw new IOException(e);
        }
    }


    @Override
    public String getAuthor() {
        return template.getAuthor();
    }

    @Override
    public Version getVersion() {
        return template.getVersion();
    }

    @Override
    public Map<String, ConfigPack> packs() {
        return packs;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public RegistryKey getRegistryKey() {
        return key;
    }

    @Override
    public <T> CheckedRegistry<T> getRegistry(Type type) {
        return (CheckedRegistry<T>) registryMap.get(type);
    }

    @Override
    public <T> CheckedRegistry<T> getCheckedRegistry(Type type) throws IllegalStateException {
        return (CheckedRegistry<T>) registryMap.get(type);
    }

    @Override
    public <T> CheckedRegistry<T> getOrCreateRegistry(TypeKey<T> typeKey) {
        return (CheckedRegistry<T>) registryMap.computeIfAbsent(typeKey.getType(), c -> {
            OpenRegistry<T> registry = new OpenRegistryImpl<>(typeKey);
            selfLoader.registerLoader(c, registry);
            abstractConfigLoader.registerLoader(c, registry);
            logger.debug("Registered loader for registry of class {}", ReflectionUtil.typeToString(c));

            if(typeKey.getType() instanceof ParameterizedType param) {
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
    public <T> MetaPackImpl applyLoader(Type type, TypeLoader<T> loader) {
        abstractConfigLoader.registerLoader(type, loader);
        selfLoader.registerLoader(type, loader);
        return this;
    }

    @Override
    public <T> MetaPackImpl applyLoader(Type type, Supplier<ObjectTemplate<T>> loader) {
        abstractConfigLoader.registerLoader(type, loader);
        selfLoader.registerLoader(type, loader);
        return this;
    }

    @Override
    public void register(TypeRegistry registry) {
        registryMap.forEach(registry::registerLoader);
    }

}
