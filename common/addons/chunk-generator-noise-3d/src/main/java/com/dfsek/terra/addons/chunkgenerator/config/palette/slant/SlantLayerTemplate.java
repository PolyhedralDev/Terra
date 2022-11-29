package com.dfsek.terra.addons.chunkgenerator.config.palette.slant;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import java.util.List;
import java.util.Map;

import com.dfsek.terra.addons.chunkgenerator.palette.PaletteHolder;
import com.dfsek.terra.addons.chunkgenerator.palette.slant.SlantHolder;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.chunk.generation.util.Palette;


public class SlantLayerTemplate implements ObjectTemplate<SlantHolder.Layer> {
    
    @Value("threshold")
    private @Meta double threshold;
    
    @Value("palette")
    private @Meta List<@Meta Map<@Meta Palette, @Meta Integer>> palettes;
    
    @Override
    public SlantHolder.Layer get() {
        return new SlantHolder.Layer(PaletteHolder.of(palettes), threshold);
    }
}
