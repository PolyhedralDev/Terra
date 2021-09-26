package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;


@Addon("Terra-Bukkit")
@Version("1.0.0")
@Author("Terra")
final class BukkitAddon extends TerraAddon {
    private final Platform platform;
    
    public BukkitAddon(Platform platform) {
        this.platform = platform;
    }
    
    @Override
    public void initialize() {
    
    }
}
