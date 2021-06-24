package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.config.templates.OreTemplate;
import com.dfsek.terra.world.population.items.ores.Ore;
import com.dfsek.terra.world.population.items.ores.VanillaOre;

public class OreFactory implements ConfigFactory<OreTemplate, Ore> {
    @Override
    public Ore build(OreTemplate config, TerraPlugin main) {
        BlockData m = config.getMaterial();
        return new VanillaOre(m, config.getReplaceable(), config.doPhysics(), config.getSize(), main, config.getMaterialOverrides());
    }
}
