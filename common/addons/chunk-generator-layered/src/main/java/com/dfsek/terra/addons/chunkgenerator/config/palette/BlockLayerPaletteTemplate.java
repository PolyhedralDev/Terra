package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.chunkgenerator.layer.palette.BlockLayerPalette;
import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.api.block.state.BlockState;


public class BlockLayerPaletteTemplate implements ObjectTemplate<LayerPalette> {
    
    @Value("block")
    private BlockState block;
    
    @Override
    public BlockLayerPalette get() {
        return new BlockLayerPalette(block);
    }
}
