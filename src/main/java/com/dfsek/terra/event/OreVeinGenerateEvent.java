package com.dfsek.terra.event;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.config.genconfig.OreConfig;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class OreVeinGenerateEvent extends TerraWorldEvent implements Cancellable {
    private boolean cancelled;
    private OreConfig config;

    public OreVeinGenerateEvent(TerraWorld tw, Location l, OreConfig config) {
        super(tw, l);
        this.config = config;
    }

    public OreConfig getConfig() {
        return config;
    }

    public void setConfig(OreConfig config) {
        this.config = config;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
