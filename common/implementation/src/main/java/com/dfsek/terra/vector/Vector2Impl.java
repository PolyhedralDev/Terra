package com.dfsek.terra.vector;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.vector.Vector2;
import net.jafama.FastMath;

/**
 * oh yeah
 */
public class Vector2Impl implements Vector2 {
    private double x;
    private double z;

    /**
     * Create a vector with a given X and Z component
     *
     * @param x X component
     * @param z Z component
     */
    public Vector2Impl(double x, double z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public Vector2 setX(double x) {
        this.x = x;
        return this;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public Vector2 setZ(double z) {
        this.z = z;
        return this;
    }

    @Override
    public Vector2 multiply(double m) {
        x *= m;
        z *= m;
        return this;
    }

    @Override
    public Vector2 add(Vector2Impl other) {
        x += other.x;
        z += other.z;
        return this;
    }

    @Override
    public Vector2 subtract(Vector2Impl other) {
        x -= other.x;
        z -= other.z;
        return this;
    }

    @Override
    public Vector2 normalize() {
        divide(length());
        return this;
    }

    @Override
    public Vector2 divide(double d) {
        x /= d;
        z /= d;
        return this;
    }

    @Override
    public double length() {
        return FastMath.sqrt(lengthSquared());
    }

    @Override
    public double lengthSquared() {
        return x * x + z * z;
    }

    @Override
    public double distance(Vector2Impl other) {
        return FastMath.sqrt(distanceSquared(other));
    }

    @Override
    public double distanceSquared(Vector2Impl other) {
        double dx = other.x - x;
        double dz = other.z - z;
        return dx * dx + dz * dz;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Double.hashCode(x);
        hash = 31 * hash + Double.hashCode(z);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Vector2Impl)) return false;
        Vector2Impl other = (Vector2Impl) obj;
        return MathUtil.equals(this.x, other.x) && MathUtil.equals(this.z, other.z);
    }

    @Override
    public Vector2 clone() {
        try {
            return (Vector2) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public Vector2 add(double x, double z) {
        this.x += x;
        this.z += z;
        return this;
    }

    @Override
    public int getBlockX() {
        return FastMath.floorToInt(x);
    }

    @Override
    public int getBlockZ() {
        return FastMath.floorToInt(z);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
    }
}
