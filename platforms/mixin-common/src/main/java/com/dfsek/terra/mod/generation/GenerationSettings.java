package com.dfsek.terra.mod.generation;

import com.dfsek.terra.api.util.range.ConstantRange;


public record GenerationSettings(ConstantRange height, Integer sealevel, Boolean mobGeneration, Integer spawnHeight) {
}
