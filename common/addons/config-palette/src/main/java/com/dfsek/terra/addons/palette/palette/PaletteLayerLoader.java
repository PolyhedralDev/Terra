/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.palette.palette;

import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.api.loader.type.TypeLoader;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


@SuppressWarnings("unchecked")
public class PaletteLayerLoader implements TypeLoader<PaletteLayerHolder> {
    private static final AnnotatedType BLOCK_DATA_PROBABILITY_COLLECTION_TYPE;
    static {
        try {
            BLOCK_DATA_PROBABILITY_COLLECTION_TYPE = PaletteLayerLoader.class.getDeclaredField("blockStateProbabilityCollection")
                                                                             .getAnnotatedType();
        } catch(NoSuchFieldException e) {
            throw new Error("this should never happen. i dont know what you did to make this happen but something is very wrong.", e);
        }
    }
    @SuppressWarnings("unused")
    private ProbabilityCollection<BlockState> blockStateProbabilityCollection;
    
    @Override
    public PaletteLayerHolder load(AnnotatedType type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        ProbabilityCollection<BlockState> collection = (ProbabilityCollection<BlockState>) configLoader.loadType(
                BLOCK_DATA_PROBABILITY_COLLECTION_TYPE, map.get("materials"));
        
        NoiseSampler sampler = null;
        if(map.containsKey("sampler")) {
            sampler = configLoader.loadType(NoiseSampler.class, map.get("sampler"));
        }
        
        if(collection == null) throw new LoadException("Collection is null: " + map.get("materials"));
        return new PaletteLayerHolder(collection, sampler, (Integer) map.get("layers"));
    }
}
