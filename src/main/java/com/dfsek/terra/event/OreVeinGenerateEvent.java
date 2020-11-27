package com.dfsek.terra.event;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.generation.items.ores.Ore;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class OreVeinGenerateEvent extends TerraWorldEvent implements Cancellable {
    private boolean cancelled;
    private Ore ore;

    public OreVeinGenerateEvent(TerraWorld tw, Location l, Ore ore) {
        super(tw, l);
        this.ore = ore;
    }

    public Ore getConfig() {
        return ore;
    }

    public void setOre(Ore ore) {
        this.ore = ore;
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
