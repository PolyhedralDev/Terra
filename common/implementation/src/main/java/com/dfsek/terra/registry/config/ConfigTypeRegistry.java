package com.dfsek.terra.registry.config;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.config.pack.ConfigPackImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ConfigTypeRegistry extends OpenRegistryImpl<ConfigType<?, ?>> {
    private final BiConsumer<String, ConfigType<?, ?>> callback;

    public ConfigTypeRegistry(ConfigPackImpl pack, TerraPlugin main, BiConsumer<String, ConfigType<?, ?>> callback) {
        super(new LinkedHashMap<>()); // Ordered
        this.callback = callback;
        add("PACK", new PackBuilder());
    }

    @Override
    public boolean add(String identifier, Entry<ConfigType<?, ?>> value) {
        callback.accept(identifier, value.getValue());
        return super.add(identifier, value);
    }

    private static final class PackBuilder implements ConfigType<ConfigTemplate, ConfigPack> {

        @Override
        public ConfigTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
            return new ConfigTemplate() {
            };
        }

        @Override
        public void callback(ConfigPack pack, TerraPlugin main, ConfigTemplate loadedConfig) {

        }

        @Override
        public Class<ConfigPack> getTypeClass() {
            return ConfigPack.class;
        }

        @Override
        public Supplier<OpenRegistry<ConfigPack>> registrySupplier() {
            return OpenRegistryImpl::new;
        }
    }

    private static final class ConfigBuilder<T extends AbstractableTemplate, O> implements ConfigType<T, O> {
        private final ConfigFactory<T, O> factory;
        private final Supplier<T> provider;
        private final Class<O> clazz;
        private final Supplier<OpenRegistry<O>> registrySupplier;

        private ConfigBuilder(ConfigFactory<T, O> factory, Supplier<T> provider, Class<O> clazz, Supplier<OpenRegistry<O>> registrySupplier) {
            this.factory = factory;
            this.provider = provider;
            this.clazz = clazz;
            this.registrySupplier = registrySupplier;
        }

        @Override
        public T getTemplate(ConfigPack pack, TerraPlugin main) {
            return provider.get();
        }

        @SuppressWarnings("deprecation")
        @Override
        public void callback(ConfigPack pack, TerraPlugin main, T loadedConfig) throws LoadException {
            pack.getRegistry(clazz).addUnchecked(loadedConfig.getID(), factory.build(loadedConfig, main));
        }

        @Override
        public Class<O> getTypeClass() {
            return clazz;
        }

        @Override
        public Supplier<OpenRegistry<O>> registrySupplier() {
            return registrySupplier;
        }
    }
}
