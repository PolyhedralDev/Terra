package com.dfsek.terra.addons.palette.palette;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

import java.lang.reflect.AnnotatedType;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PaletteLayerLoader implements TypeLoader<PaletteLayerHolder> {
    private static final AnnotatedType BLOCK_DATA_PROBABILITY_COLLECTION_TYPE;

    static {
        try {
            BLOCK_DATA_PROBABILITY_COLLECTION_TYPE = PaletteLayerLoader.class.getDeclaredField("blockStateProbabilityCollection").getAnnotatedType();
        } catch(NoSuchFieldException e) {
            throw new Error("this should never happen. i dont know what you did to make this happen but something is very wrong.", e);
        }
    }

    @SuppressWarnings("unused")
    private ProbabilityCollection<BlockState> blockStateProbabilityCollection;

    @Override
    public PaletteLayerHolder load(AnnotatedType type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        ProbabilityCollection<BlockState> collection = (ProbabilityCollection<BlockState>) configLoader.loadType(BLOCK_DATA_PROBABILITY_COLLECTION_TYPE, map.get("materials"));

        NoiseSampler sampler = null;
        if(map.containsKey("noise")) {
            sampler = configLoader.loadType(NoiseSampler.class, map.get("noise"));
        }

        if(collection == null) throw new LoadException("Collection is null: " + map.get("materials"));
        return new PaletteLayerHolder(collection, sampler, (Integer) map.get("layers"));
    }
}
