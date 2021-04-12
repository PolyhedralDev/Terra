package com.dfsek.terra.registry.config;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.api.world.palette.SinglePalette;
import com.dfsek.terra.registry.OpenRegistry;

public class PaletteRegistry extends OpenRegistry<Palette> {
    private final TerraPlugin main;

    public PaletteRegistry(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public Palette get(String identifier) {
        if(identifier.startsWith("BLOCK:"))
            return new SinglePalette(main.getWorldHandle().createBlockData(identifier.substring(6))); // Return single palette for BLOCK: shortcut.
        return super.get(identifier);
    }
}
