package com.dfsek.terra.config.factories;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.generation.items.TerraStructure;

public class StructureFactory implements TerraFactory<StructureTemplate, TerraStructure> {
    @Override
    public TerraStructure build(StructureTemplate config, TerraPlugin main) throws LoadException {
        return new TerraStructure(null, config.getBound(), config.getY(), config.getSpawn(), config.getLoot(), config);
    }
}
