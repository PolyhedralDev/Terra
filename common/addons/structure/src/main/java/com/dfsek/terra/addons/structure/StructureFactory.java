package com.dfsek.terra.addons.structure;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.structure.ConfiguredStructure;
import com.dfsek.terra.config.templates.StructureTemplate;

public class StructureFactory implements ConfigFactory<StructureTemplate, ConfiguredStructure> {
    @Override
    public ConfiguredStructure build(StructureTemplate config, TerraPlugin main) {
        return new TerraStructure(config.getStructures(), config.getY(), config.getSpawn());
    }
}
