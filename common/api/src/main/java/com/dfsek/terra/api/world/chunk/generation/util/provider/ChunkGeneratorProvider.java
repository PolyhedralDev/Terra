/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.chunk.generation.util.provider;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;


public interface ChunkGeneratorProvider {
    ChunkGenerator newInstance(ConfigPack pack);
}
