package com.dfsek.terra.addons.generation.structure.config;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.generation.structure.StructureGenerationStage;
import com.dfsek.terra.addons.generation.structure.impl.StructureLayerGrid;
import com.dfsek.terra.addons.generation.structure.impl.StructureLayerGridImpl;
import com.dfsek.terra.addons.generation.structure.impl.WorldStructureLayerGrid;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;

import java.util.ArrayList;
import java.util.List;


public class StructureGenerationStageTemplate implements ObjectTemplate<GenerationStage> {
    @Value("layers")
    @Default
    List<StructureLayerGridDescription> layers = new ArrayList<>();

    @Override
    public GenerationStage get() {
        StructureLayerGrid layer = new WorldStructureLayerGrid();
        for (StructureLayerGridDescription description : layers) {
            layer = new StructureLayerGridImpl(layer, description.size(), description.padding(), description.structures(), description.distribution());
        }
        return new StructureGenerationStage(layer);
    }
}
