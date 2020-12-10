package com.dfsek.terra.registry;

import com.dfsek.terra.api.gaea.world.Flora;
import com.dfsek.terra.api.gaea.world.FloraType;
import com.dfsek.terra.generation.items.flora.BlockFlora;
import org.bukkit.Bukkit;

public class FloraRegistry extends TerraRegistry<Flora> {
    public FloraRegistry() {
        for(FloraType f : FloraType.values()) add(f.toString(), f);
    }

    @Override
    public Flora get(String id) {
        if(id.startsWith("BLOCK:"))
            return new BlockFlora(Bukkit.createBlockData(id.substring(6))); // Return single flora for BLOCK: shortcut.
        return super.get(id);
    }
}
