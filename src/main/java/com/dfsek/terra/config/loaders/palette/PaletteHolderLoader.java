package com.dfsek.terra.config.loaders.palette;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.biome.palette.PaletteHolderBuilder;
import com.dfsek.terra.config.loaders.Types;
import org.bukkit.block.data.BlockData;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PaletteHolderLoader implements TypeLoader<PaletteHolder> {
    @Override
    public PaletteHolder load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        List<Map<String, Integer>> palette = (List<Map<String, Integer>>) o;
        PaletteHolderBuilder builder = new PaletteHolderBuilder();
        for(Map<String, Integer> layer : palette) {
            for(Map.Entry<String, Integer> entry : layer.entrySet()) {
                builder.add(entry.getValue(), (Palette<BlockData>) configLoader.loadType(Types.BLOCK_DATA_PALETTE_TYPE, entry.getKey()));
            }
        }
        return builder.build();
    }
}
