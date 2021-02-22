package com.dfsek.terra.registry.config;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.api.world.palette.SinglePalette;
import com.dfsek.terra.registry.TerraRegistry;

public class PaletteRegistry extends TerraRegistry<Palette<BlockData>> {
    private final TerraPlugin main;
    public PaletteRegistry(TerraPlugin main) {
        this.main = main;
    }


    @Override
    public Palette<BlockData> get(String identifier) {
        if(identifier.startsWith("BLOCK:"))
            return new SinglePalette<>(main.getWorldHandle().createBlockData(identifier.substring(6))); // Return single palette for BLOCK: shortcut.
        return super.get(identifier);
    }
}
