package com.dfsek.terra.addons.ore;

import java.util.function.Supplier;

import com.dfsek.terra.addons.ore.ores.Ore;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class OreConfigType implements ConfigType<OreTemplate, Ore> {
    public static final TypeKey<Ore> ORE_TYPE_TOKEN = new TypeKey<>() {
    };
    private final OreFactory factory = new OreFactory();
    
    @Override
    public Supplier<OpenRegistry<Ore>> registrySupplier(ConfigPack pack) {
        return pack.getRegistryFactory()::create;
    }
    
    @Override
    public OreTemplate getTemplate(ConfigPack pack, Platform platform) {
        return new OreTemplate();
    }
    
    @Override
    public ConfigFactory<OreTemplate, Ore> getFactory() {
        return factory;
    }
    
    @Override
    public TypeKey<Ore> getTypeKey() {
        return ORE_TYPE_TOKEN;
    }
}
