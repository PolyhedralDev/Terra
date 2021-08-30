package com.dfsek.terra.addons.ore;

import com.dfsek.terra.addons.ore.ores.Ore;
import com.dfsek.terra.addons.ore.ores.VanillaOre;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigFactory;


public class OreFactory implements ConfigFactory<OreTemplate, Ore> {
    @Override
    public Ore build(OreTemplate config, TerraPlugin main) {
        BlockState m = config.getMaterial();
        return new VanillaOre(m, config.getReplaceable(), config.doPhysics(), config.getSize(), main, config.getMaterialOverrides());
    }
}
