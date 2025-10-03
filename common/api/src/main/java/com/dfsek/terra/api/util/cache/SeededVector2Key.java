package com.dfsek.terra.api.util.cache;


public class SeededVector2Key {
    public int x;
    public int z;
    public long seed;

    public SeededVector2Key(int x, int z, long seed) {
        this.x = x;
        this.z = z;
        this.seed = seed;
    }

    public void set(int x, int z, long seed) {
        this.x = x;
        this.z = z;
        this.seed = seed;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SeededVector2Key that) {
            return this.z == that.z && this.x == that.x && this.seed == that.seed;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = x;
        code = 31 * code + z;
        return 31 * code + (Long.hashCode(seed));
    }
}
