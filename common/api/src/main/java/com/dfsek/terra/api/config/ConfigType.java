package com.dfsek.terra.api.config;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.OpenRegistry;

import java.util.function.Supplier;

public interface ConfigType<T extends AbstractableTemplate, R> {
    T getTemplate(ConfigPack pack, TerraPlugin main);

    ConfigFactory<T, R> getFactory();

    Class<R> getTypeClass();

    Supplier<OpenRegistry<R>> registrySupplier();
}
