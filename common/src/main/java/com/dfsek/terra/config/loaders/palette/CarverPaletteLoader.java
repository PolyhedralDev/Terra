package com.dfsek.terra.config.loaders.palette;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.carving.CarverPalette;
import com.dfsek.terra.config.loaders.Types;
import com.dfsek.terra.util.MaterialSet;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CarverPaletteLoader implements TypeLoader<CarverPalette> {


    @Override
    public CarverPalette load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Configuration configuration = new Configuration((Map<String, Object>) o);
        CarverPalette palette = new CarverPalette((MaterialSet) configLoader.loadType(MaterialSet.class, configuration.get("replace")), (Boolean) configuration.get("replace-blacklist"));

        for(Map<String, Object> map : (List<Map<String, Object>>) configuration.get("layers")) {
            ProbabilityCollection<BlockData> layer = (ProbabilityCollection<BlockData>) configLoader.loadType(Types.BLOCK_DATA_PROBABILITY_COLLECTION_TYPE, map.get("materials"));
            palette.add(layer, (Integer) map.get("y"));
        }
        palette.build();
        return palette;
    }
}
