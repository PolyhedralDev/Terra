package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.structure.ConfiguredStructure;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.world.population.items.TerraStructure;

public class StructureFactory implements ConfigFactory<StructureTemplate, ConfiguredStructure> {
    @Override
    public ConfiguredStructure build(StructureTemplate config, TerraPlugin main) {
        return new TerraStructure(config.getStructures(), config.getY(), config.getSpawn(), config);
    }
}
