package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.config.ConfigPack;

public interface BlockPopulatorProvider {
    TerraBlockPopulator newInstance(ConfigPack pack);
}
