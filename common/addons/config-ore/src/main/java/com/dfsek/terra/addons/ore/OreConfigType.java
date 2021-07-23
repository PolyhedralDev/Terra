package com.dfsek.terra.addons.ore;

import com.dfsek.terra.addons.ore.ores.Ore;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;

import java.util.function.Supplier;

public class OreConfigType implements ConfigType<OreTemplate, Ore> {
    private final OreFactory factory = new OreFactory();
    public static final TypeKey<Ore> ORE_TYPE_TOKEN = new TypeKey<>(){};

    @Override
    public OreTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
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

    @Override
    public Supplier<OpenRegistry<Ore>> registrySupplier(ConfigPack pack) {
        return pack.getRegistryFactory()::create;
    }
}
