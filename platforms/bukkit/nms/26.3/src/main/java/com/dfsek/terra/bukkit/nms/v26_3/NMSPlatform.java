package com.dfsek.terra.bukkit.nms.v26_3;

import com.dfsek.terra.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.bukkit.nms.NMSPlatformBase;


public final class NMSPlatform extends NMSPlatformBase {
    public NMSPlatform(TerraBukkitPlugin plugin) {
        super(plugin, new Bindings());
    }
}
