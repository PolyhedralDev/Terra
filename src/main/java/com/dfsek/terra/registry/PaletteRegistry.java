package com.dfsek.terra.registry;

import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.implementations.bukkit.world.block.BukkitBlockData;
import com.dfsek.terra.biome.palette.SinglePalette;
import org.bukkit.Bukkit;

public class PaletteRegistry extends TerraRegistry<Palette<BlockData>> {
    @Override
    public Palette<BlockData> get(String id) {
        if(id.startsWith("BLOCK:"))
            return new SinglePalette<>(new BukkitBlockData(Bukkit.createBlockData(id.substring(6)))); // Return single palette for BLOCK: shortcut.
        return super.get(id);
    }
}
