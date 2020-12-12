package com.dfsek.terra.config.builder.biomegrid;

import com.dfsek.terra.api.gaea.biome.BiomeGrid;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.config.base.ConfigPack;

public interface BiomeGridBuilder {
    BiomeGrid build(World world, ConfigPack config);
}
