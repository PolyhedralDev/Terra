package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.config.ConfigPack;


public interface GenerationStageProvider {
    GenerationStage newInstance(ConfigPack pack);
}
