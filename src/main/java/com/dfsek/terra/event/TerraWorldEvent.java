package com.dfsek.terra.event;

import com.dfsek.terra.TerraWorld;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class TerraWorldEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final TerraWorld world;
    private final Location location;

    public TerraWorldEvent(TerraWorld tw, Location l) {
        this.world = tw;
        this.location = l.clone();
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public TerraWorld getWorld() {
        return world;
    }

    public Location getLocation() {
        return location;
    }
}
