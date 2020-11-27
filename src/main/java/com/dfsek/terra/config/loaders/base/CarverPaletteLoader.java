package com.dfsek.terra.config.loaders.base;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.carving.CarverPalette;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.ProbabilityCollection;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class CarverPaletteLoader implements TypeLoader<CarverPalette> {
    @Override
    public CarverPalette load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Configuration configuration = new Configuration((Map<String, Object>) o);
        CarverPalette palette = new CarverPalette((Set<Material>) configLoader.loadType(Set.class, configuration.get("replace")), (Boolean) configuration.get("replace-blacklist"));

        for(Map<String, Object> map : (List<Map<String, Object>>) configuration.get("layers")) {
            ProbabilityCollection<BlockData> layer = (ProbabilityCollection<BlockData>) configLoader.loadType(ProbabilityCollection.class, map.get("materials"));
            palette.add(layer, (Integer) map.get("y"));
        }
        return palette;
    }
}
