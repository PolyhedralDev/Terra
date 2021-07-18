package com.dfsek.terra.addons.ore;

import com.dfsek.terra.addons.ore.ores.Ore;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.TypeToken;

import java.util.function.Supplier;

public class OreConfigType implements ConfigType<OreTemplate, Ore> {
    private final OreFactory factory = new OreFactory();
    private final ConfigPack pack;
    public static final TypeToken<Ore> ORE_TYPE_TOKEN = new TypeToken<>(){};

    public OreConfigType(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public OreTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
        return new OreTemplate();
    }

    @Override
    public ConfigFactory<OreTemplate, Ore> getFactory() {
        return factory;
    }

    @Override
    public TypeToken<Ore> getTypeClass() {
        return ORE_TYPE_TOKEN;
    }

    @Override
    public Supplier<OpenRegistry<Ore>> registrySupplier() {
        return pack.getRegistryFactory()::create;
    }
}
