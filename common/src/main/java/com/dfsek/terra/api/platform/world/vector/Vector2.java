package com.dfsek.terra.api.platform.world.vector;

import net.jafama.FastMath;

/**
 * oh yeah
 */
@SuppressWarnings("unused")
public class Vector2 implements Cloneable {
    private double x;
    private double z;

    /**
     * Create a vector with a given X and Z component
     *
     * @param x X component
     * @param z Z component
     */
    public Vector2(double x, double z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Get X component
     *
     * @return X component
     */
    public double getX() {
        return x;
    }

    public Vector2 setX(double x) {
        this.x = x;
        return this;
    }

    /**
     * Get Z component
     *
     * @return Z component
     */
    public double getZ() {
        return z;
    }

    public Vector2 setZ(double z) {
        this.z = z;
        return this;
    }

    /**
     * Multiply X and Z components by a value.
     *
     * @param m Value to multiply
     * @return Mutated vector, for chaining.
     */
    public Vector2 multiply(double m) {
        x *= m;
        z *= m;
        return this;
    }

    /**
     * Add this vector to another.
     *
     * @param other Vector to add
     * @return Mutated vector, for chaining.
     */
    public Vector2 add(Vector2 other) {
        x += other.x;
        z += other.z;
        return this;
    }

    /**
     * Subtract a vector from this vector,
     *
     * @param other Vector to subtract
     * @return Mutated vector, for chaining.
     */
    public Vector2 subtract(Vector2 other) {
        x -= other.x;
        z -= other.z;
        return this;
    }

    /**
     * Normalize this vector to length 1
     *
     * @return Mutated vector, for chaining.
     */
    public Vector2 normalize() {
        divide(length());
        return this;
    }

    /**
     * Divide X and Z components by a value.
     *
     * @param d Divisor
     * @return Mutated vector, for chaining.
     */
    public Vector2 divide(double d) {
        x /= d;
        z /= d;
        return this;
    }

    /**
     * Get the length of this Vector
     *
     * @return length
     */
    public double length() {
        return FastMath.sqrt(lengthSquared());
    }

    /**
     * Get the squared length of this Vector
     *
     * @return squared length
     */
    public double lengthSquared() {
        return x * x + z * z;
    }

    /**
     * Get the distance from this vector to another.
     *
     * @param other Another vector
     * @return Distance between vectors
     */
    public double distance(Vector2 other) {
        return FastMath.sqrt(distanceSquared(other));
    }

    /**
     * Get the squared distance between 2 vectors.
     *
     * @param other Another vector
     * @return Squared distance
     */
    public double distanceSquared(Vector2 other) {
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
        if(!(obj instanceof Vector2)) {
            return false;
        }
        Vector2 other = (Vector2) obj;
        return other.x == this.x && other.z == this.z;
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
    public String toString() {
        return "(" + x + ", " + z + ")";
    }
}
