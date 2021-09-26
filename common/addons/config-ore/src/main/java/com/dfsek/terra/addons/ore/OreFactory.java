package com.dfsek.terra.addons.ore;

import com.dfsek.terra.addons.ore.ores.Ore;
import com.dfsek.terra.addons.ore.ores.VanillaOre;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigFactory;


public class OreFactory implements ConfigFactory<OreTemplate, Ore> {
    @Override
    public Ore build(OreTemplate config, Platform platform) {
        BlockState m = config.getMaterial();
        return new VanillaOre(m, config.getReplaceable(), config.doPhysics(), config.getSize(), platform, config.getMaterialOverrides());
    }
}
