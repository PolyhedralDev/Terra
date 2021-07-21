package com.dfsek.terra.addons.feature.distributor.util;

public class Point {
    private final int x;
    private final int z;

    private final int hash;

    public Point(int x, int z) {
        this.x = x;
        this.z = z;
        this.hash = 31 * x + z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Point)) return false;
        Point that = (Point) obj;
        return this.x == that.x && this.z == that.z;
    }
}
