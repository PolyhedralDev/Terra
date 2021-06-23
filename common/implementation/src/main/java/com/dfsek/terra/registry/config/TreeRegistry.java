package com.dfsek.terra.registry.config;

import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.registry.OpenRegistry;

public class TreeRegistry extends OpenRegistry<Tree> {
    @Override
    public boolean add(String identifier, Tree value) {
        return super.add(identifier, value);
    }
}
