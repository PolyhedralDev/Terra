package com.dfsek.terra.api.config;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;

import java.util.function.Supplier;

public interface ConfigType<T extends AbstractableTemplate, R> {
    T getTemplate(ConfigPack pack, TerraPlugin main);

    ConfigFactory<T, R> getFactory();

    TypeKey<R> getTypeKey();

    Supplier<OpenRegistry<R>> registrySupplier(ConfigPack pack);
}
