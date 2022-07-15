package com.dfsek.terra.bukkit;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;

import com.dfsek.terra.api.addon.BaseAddon;


public class BukkitAddon implements BaseAddon {
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    
    protected final PlatformImpl terraBukkitPlugin;
    
    public BukkitAddon(PlatformImpl terraBukkitPlugin) {
        this.terraBukkitPlugin = terraBukkitPlugin;
    }
    
    
    @Override
    public Version getVersion() {
        return VERSION;
    }
    
    @Override
    public String getID() {
        return "terra-bukkit";
    }
}
