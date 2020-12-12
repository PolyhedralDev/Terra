package com.dfsek.terra.api.gaea.population;


import com.dfsek.terra.api.generic.world.Chunk;

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
        this.worldID = c.getWorld().getUID();
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
        if(! (obj instanceof com.dfsek.terra.api.gaea.population.ChunkCoordinate)) return false;
        com.dfsek.terra.api.gaea.population.ChunkCoordinate other = (com.dfsek.terra.api.gaea.population.ChunkCoordinate) obj;
        return other.getX() == x && other.getZ() == z;
    }
}
