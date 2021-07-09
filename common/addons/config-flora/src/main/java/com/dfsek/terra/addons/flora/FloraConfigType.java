package com.dfsek.terra.addons.flora;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.world.Flora;

import java.util.function.Supplier;

public class FloraConfigType implements ConfigType<FloraTemplate, Flora> {
    private final FloraFactory factory = new FloraFactory();
    private final ConfigPack pack;

    public FloraConfigType(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public FloraTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
        return new FloraTemplate();
    }

    @Override
    public ConfigFactory<FloraTemplate, Flora> getFactory() {
        return factory;
    }

    @Override
    public Class<Flora> getTypeClass() {
        return Flora.class;
    }

    @Override
    public Supplier<OpenRegistry<Flora>> registrySupplier() {
        return pack.getRegistryFactory()::create;
    }
}
