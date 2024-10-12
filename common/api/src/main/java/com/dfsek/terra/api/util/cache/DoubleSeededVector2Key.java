package com.dfsek.terra.api.util.cache;

public class DoubleSeededVector2Key {

    public double x;
    public double z;
    public long seed;

    public DoubleSeededVector2Key(double x, double z, long seed) {
        this.x = x;
        this.z = z;
        this.seed = seed;
    }

    public void set(double x, double z, long seed) {
        this.x = x;
        this.z = z;
        this.seed = seed;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DoubleSeededVector2Key that) {
            return this.z == that.z && this.x == that.x && this.seed == that.seed;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = (int) Double.doubleToLongBits(x);
        code = 31 * code + (int) Double.doubleToLongBits(z);
        return 31 * code + (Long.hashCode(seed));
    }
}