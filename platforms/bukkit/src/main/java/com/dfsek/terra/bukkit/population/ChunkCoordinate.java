package com.dfsek.terra.bukkit.population;


import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.bukkit.world.BukkitWorld;

import java.io.Serializable;
import java.util.UUID;

public class ChunkCoordinate implements Serializable {
    public static final long serialVersionUID = 7102462856296750285L;
    private final int x;
    private final int z;
    private final UUID worldID;

    public ChunkCoordinate(int x, int z, UUID worldID) {
        this.x = x;
        this.z = z;
        this.worldID = worldID;
    }

    public ChunkCoordinate(Chunk c) {
        this.x = c.getX();
        this.z = c.getZ();
        this.worldID = ((BukkitWorld) c.getWorld()).getUID();
    }

    public UUID getWorldID() {
        return worldID;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return x * 31 + z;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ChunkCoordinate)) return false;
        ChunkCoordinate other = (ChunkCoordinate) obj;
        return other.getX() == x && other.getZ() == z;
    }
}
