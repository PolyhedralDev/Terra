package com.dfsek.terra.bukkit;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.ConfigurationLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.config.VanillaBiomeProperties;


public class BukkitAddon implements BaseAddon {
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    
    private final PlatformImpl terraBukkitPlugin;
    
    public BukkitAddon(PlatformImpl terraBukkitPlugin) {
        this.terraBukkitPlugin = terraBukkitPlugin;
    }
    
    @Override
    public void initialize() {
        terraBukkitPlugin.getEventManager()
                         .getHandler(FunctionalEventHandler.class)
                         .register(this, ConfigurationLoadEvent.class)
                         .then(event -> {
                             if(event.is(Biome.class)) {
                                 event.getLoadedObject(Biome.class).getContext().put(event.load(new VanillaBiomeProperties()));
                             }
                         })
                         .global();
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
