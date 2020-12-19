package com.dfsek.terra.registry;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.block.BlockData;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.biome.palette.SinglePalette;

public class PaletteRegistry extends TerraRegistry<Palette<BlockData>> {
    private final TerraPlugin main;
    public PaletteRegistry(TerraPlugin main) {
        this.main = main;
    }


    @Override
    public Palette<BlockData> get(String id) {
        if(id.startsWith("BLOCK:"))
            return new SinglePalette<>(main.getWorldHandle().createBlockData(id.substring(6))); // Return single palette for BLOCK: shortcut.
        return super.get(id);
    }
}
