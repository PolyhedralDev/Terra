/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world.generator;

import com.dfsek.terra.api.util.math.Sampler;


public interface SamplerCache {
    Sampler get(int x, int z);
    
    void clear();
    
    Sampler getChunk(int cx, int cz);
}
