package com.dfsek.terra.mod.generation;

import com.dfsek.terra.api.util.ConstantRange;
import com.dfsek.terra.api.util.Range;


public record GenerationSettings(ConstantRange height, Integer sealevel, Boolean mobGeneration) {
}
