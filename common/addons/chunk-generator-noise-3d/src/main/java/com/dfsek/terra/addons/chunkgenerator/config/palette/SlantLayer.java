package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolderBuilder;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class SlantLayer implements ObjectTemplate<SlantLayer> {
    @Value("threshold")
    private @Meta double threshold;
    
    @Value("palette")
    private @Meta List<@Meta Map<@Meta Palette, @Meta Integer>> palettes;
    
    @Override
    public SlantLayer get() {
        return this;
    }
    
    public double getThreshold() {
        return threshold;
    }
    
    public PaletteHolder getPalette() {
        PaletteHolderBuilder builder = new PaletteHolderBuilder();
        for(Map<Palette, Integer> layer : palettes) {
            for(Entry<Palette, Integer> entry : layer.entrySet()) {
                builder.add(entry.getValue(), entry.getKey());
            }
        }
        
        return builder.build();
    }
}
