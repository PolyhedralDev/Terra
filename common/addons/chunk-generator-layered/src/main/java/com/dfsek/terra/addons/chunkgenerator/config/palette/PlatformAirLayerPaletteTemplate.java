package com.dfsek.terra.addons.chunkgenerator.config.palette;

import com.dfsek.terra.addons.chunkgenerator.api.LayerPalette;
import com.dfsek.terra.addons.chunkgenerator.layer.palette.SimpleLayerPalette;
import com.dfsek.terra.addons.chunkgenerator.palette.SingletonPalette;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.block.state.BlockState;


public class PlatformAirLayerPaletteTemplate extends LayerPaletteTemplate {
    
    private BlockState air;
    
    public PlatformAirLayerPaletteTemplate(Platform platform) {
        this.air = platform.getWorldHandle().air();
    }
    
    @Override
    public LayerPalette get() {
        return new SimpleLayerPalette(group, resetsGroup, new SingletonPalette(air));
    }
}
