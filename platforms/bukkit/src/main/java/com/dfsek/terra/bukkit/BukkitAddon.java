package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addon.TerraAddon;
import com.dfsek.terra.api.addon.annotations.Addon;
import com.dfsek.terra.api.addon.annotations.Author;
import com.dfsek.terra.api.addon.annotations.Version;


@Addon("Terra-Bukkit")
@Version("1.0.0")
@Author("Terra")
final class BukkitAddon extends TerraAddon {
    private final TerraPlugin main;
    
    public BukkitAddon(TerraPlugin main) {
        this.main = main;
    }
    
    @Override
    public void initialize() {
    
    }
}
