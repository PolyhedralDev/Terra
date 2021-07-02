package com.dfsek.terra.addons.palette;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.palette.PaletteImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;

public class PaletteRegistry extends OpenRegistryImpl<Palette> {
    private final TerraPlugin main;

    public PaletteRegistry(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public Palette get(String identifier) {
        if(identifier.startsWith("BLOCK:"))
            return new PaletteImpl.Singleton(main.getWorldHandle().createBlockData(identifier.substring(6))); // Return single palette for BLOCK: shortcut.
        return super.get(identifier);
    }
}
