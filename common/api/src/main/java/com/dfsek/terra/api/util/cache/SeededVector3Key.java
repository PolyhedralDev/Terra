package com.dfsek.terra.api.util.cache;


public class SeededVector3Key {
    public int x;
    public int y;
    public int z;
    public long seed;

    public SeededVector3Key(int x, int y, int z, long seed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.seed = seed;
    }

    public void set(int x, int y, int z, long seed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.seed = seed;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SeededVector3Key that) {
            return this.y == that.y && this.z == that.z && this.x == that.x && this.seed == that.seed;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = x;
        code = 31 * code + y;
        code = 31 * code + z;
        return 31 * code + (Long.hashCode(seed));
    }
}