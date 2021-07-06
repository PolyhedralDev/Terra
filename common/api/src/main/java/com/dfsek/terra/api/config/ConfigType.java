package com.dfsek.terra.api.config;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.OpenRegistry;

import java.util.function.Supplier;

public interface ConfigType<T extends ConfigTemplate, R> {
    T getTemplate(ConfigPack pack, TerraPlugin main);

    void callback(ConfigPack pack, TerraPlugin main, T loadedConfig) throws LoadException;

    Class<R> getTypeClass();

    Supplier<OpenRegistry<R>> registrySupplier();
}
