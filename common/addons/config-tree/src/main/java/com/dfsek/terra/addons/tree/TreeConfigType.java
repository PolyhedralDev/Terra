package com.dfsek.terra.addons.tree;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.config.ConfigType;
import com.dfsek.terra.api.registry.OpenRegistry;
import com.dfsek.terra.api.util.TypeToken;
import com.dfsek.terra.api.world.Tree;

import java.util.function.Supplier;

public class TreeConfigType implements ConfigType<TreeTemplate, Tree> {
    private final TreeFactory factory = new TreeFactory();
    private final ConfigPack pack;

    public static final TypeToken<Tree> TREE_TYPE_TOKEN = new TypeToken<>(){};
    public TreeConfigType(ConfigPack pack) {
        this.pack = pack;
    }

    @Override
    public TreeTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
        return new TreeTemplate();
    }

    @Override
    public ConfigFactory<TreeTemplate, Tree> getFactory() {
        return factory;
    }

    @Override
    public TypeToken<Tree> getTypeClass() {
        return TREE_TYPE_TOKEN;
    }

    @Override
    public Supplier<OpenRegistry<Tree>> registrySupplier() {
        return pack.getRegistryFactory()::create;
    }
}
