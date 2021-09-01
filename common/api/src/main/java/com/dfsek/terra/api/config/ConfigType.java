package com.dfsek.terra.api.config;

import java.util.function.Supplier;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;


public interface ConfigType<T extends AbstractableTemplate, R> {
    Supplier<OpenRegistry<R>> registrySupplier(ConfigPack pack);
    
    T getTemplate(ConfigPack pack, TerraPlugin main);
    
    ConfigFactory<T, R> getFactory();
    
    TypeKey<R> getTypeKey();
}
