package com.dfsek.terra.config.loaders.palette;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.palette.holder.PaletteLayerHolder;
import com.dfsek.terra.config.loaders.Types;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PaletteLayerLoader implements TypeLoader<PaletteLayerHolder> {
    @Override
    public PaletteLayerHolder load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        ProbabilityCollection<BlockData> collection = (ProbabilityCollection<BlockData>) configLoader.loadType(Types.BLOCK_DATA_PROBABILITY_COLLECTION_TYPE, map.get("materials"));

        NoiseSampler sampler = null;
        if(map.containsKey("noise")) {
            sampler = ((NoiseSeeded) configLoader.loadType(NoiseSeeded.class, map.get("noise"))).apply(2403L);
        }

        if(collection == null) throw new LoadException("Collection is null: " + map.get("materials"));
        return new PaletteLayerHolder(collection, sampler, (Integer) map.get("layers"));
    }
}
