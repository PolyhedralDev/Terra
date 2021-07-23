package com.dfsek.terra.addons.flora;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.api.world.Flora;

import java.util.function.Supplier;

public class FloraConfigType implements ConfigType<FloraTemplate, Flora> {
    private final FloraFactory factory = new FloraFactory();

    public static final TypeKey<Flora> FLORA_TYPE_TOKEN = new TypeKey<>(){};

    @Override
    public FloraTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
        return new FloraTemplate();
    }

    @Override
    public ConfigFactory<FloraTemplate, Flora> getFactory() {
        return factory;
    }

    @Override
    public TypeKey<Flora> getTypeKey() {
        return FLORA_TYPE_TOKEN;
    }

    @Override
    public Supplier<OpenRegistry<Flora>> registrySupplier(ConfigPack pack) {
        return pack.getRegistryFactory()::create;
    }
}
