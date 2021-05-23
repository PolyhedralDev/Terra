package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.platform.modloader.Mod;
import org.bukkit.plugin.Plugin;

public class BukkitPlugin implements Mod {
    private final Plugin plugin;

    public BukkitPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getID() {
        return plugin.getName();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }
}
