package com.dfsek.terra.structure;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class InitializationException extends Exception {
    private final Location worldLoc;

    public InitializationException(String message, @Nullable Location worldLoc) {
        super(message);
        this.worldLoc = worldLoc == null ? null : worldLoc.clone();
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " (at location: " + worldLoc.toString() + ")";
    }

    @Nullable
    public Location getWorldLoc() {
        return worldLoc;
    }
}
