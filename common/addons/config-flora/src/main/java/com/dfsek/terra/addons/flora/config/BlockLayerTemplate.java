/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.flora.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.flora.flora.gen.BlockLayer;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class BlockLayerTemplate implements ObjectTemplate<BlockLayer> {
    @Value("layers")
    private @Meta int layers;

    @Value("materials")
    private @Meta ProbabilityCollection<BlockState> data;

    @Override
    public BlockLayer get() {
        return new BlockLayer(layers, data);
    }
}
