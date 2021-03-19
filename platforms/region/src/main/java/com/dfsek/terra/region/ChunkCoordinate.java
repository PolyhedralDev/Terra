package com.dfsek.terra.region;

public class ChunkCoordinate {
    private final int x;
    private final int z;

    public ChunkCoordinate(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
