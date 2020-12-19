package com.dfsek.terra.config.loaders.palette;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.biome.palette.PaletteLayer;
import com.dfsek.terra.config.loaders.Types;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PaletteLayerLoader implements TypeLoader<PaletteLayer> {
    @Override
    public PaletteLayer load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        ProbabilityCollection<BlockData> collection = (ProbabilityCollection<BlockData>) configLoader.loadType(Types.BLOCK_DATA_PROBABILITY_COLLECTION_TYPE, map.get("materials"));
        if(collection == null) throw new LoadException("Collection is null: " + map.get("materials"));
        return new PaletteLayer(collection, (Integer) map.get("layers"));
    }
}
