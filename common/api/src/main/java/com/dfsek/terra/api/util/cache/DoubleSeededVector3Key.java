package com.dfsek.terra.api.util.cache;

public class DoubleSeededVector3Key {
    public double x;
    public double y;
    public double z;
    public long seed;

    public DoubleSeededVector3Key(double x, double y, double z, long seed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.seed = seed;
    }

    public void set(double x, double y, double z, long seed) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.seed = seed;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DoubleSeededVector3Key that) {
            return this.y == that.y && this.z == that.z && this.x == that.x && this.seed == that.seed;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = (int) Double.doubleToLongBits(x);
        code = 31 * code + (int) Double.doubleToLongBits(y);
        code = 31 * code + (int) Double.doubleToLongBits(z);
        return 31 * code + (Long.hashCode(seed));
    }
}