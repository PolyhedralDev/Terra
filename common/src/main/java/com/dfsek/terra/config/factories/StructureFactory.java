package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.world.population.items.TerraStructure;

public class StructureFactory implements ConfigFactory<StructureTemplate, TerraStructure> {
    @Override
    public TerraStructure build(StructureTemplate config, TerraPlugin main) {
        return new TerraStructure(config.getStructures(), config.getY(), config.getSpawn(), config);
    }
}
