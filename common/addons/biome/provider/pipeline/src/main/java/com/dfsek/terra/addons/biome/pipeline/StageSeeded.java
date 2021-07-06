package com.dfsek.terra.addons.biome.pipeline;

import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.api.util.seeded.SeededBuilder;

@FunctionalInterface
public interface StageSeeded extends SeededBuilder<Stage> {
}
