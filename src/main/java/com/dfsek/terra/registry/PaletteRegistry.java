package com.dfsek.terra.registry;

import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.biome.palette.SinglePalette;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

public class PaletteRegistry extends TerraRegistry<Palette<BlockData>> {
    @Override
    public Palette<BlockData> get(String id) {
        if(id.startsWith("BLOCK:"))
            return new SinglePalette<>(Bukkit.createBlockData(id.substring(6))); // Return single palette for BLOCK: shortcut.
        return super.get(id);
    }
}
