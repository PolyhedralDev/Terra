package com.dfsek.terra.config.builder;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.world.TerraWorld;

public interface ConfigBuilder<O> {
    O build(TerraWorld world, TerraPlugin main);
}
