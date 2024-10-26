package com.dfsek.terra.bukkit;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.config.pack.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.bukkit.config.PreLoadCompatibilityOptions;


public class BukkitAddon implements BaseAddon {
    private static final Version VERSION = Versions.getVersion(1, 0, 0);

    protected final PlatformImpl terraBukkitPlugin;

    public BukkitAddon(PlatformImpl terraBukkitPlugin) {
        this.terraBukkitPlugin = terraBukkitPlugin;
    }

    @Override
    public void initialize() {
        terraBukkitPlugin.getEventManager()
            .getHandler(FunctionalEventHandler.class)
            .register(this, ConfigPackPreLoadEvent.class)
            .then(event -> event.getPack().getContext().put(event.loadTemplate(new PreLoadCompatibilityOptions())))
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
