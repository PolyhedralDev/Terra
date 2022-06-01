/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.vector;

import net.jafama.FastMath;

import com.dfsek.terra.api.util.MathUtil;


/**
 * oh yeah
 */
public class Vector2 {
    protected double x;
    protected double z;
    
    /**
     * Create a vector with a given X and Z component
     *
     * @param x X component
     * @param z Z component
     */
    private Vector2(double x, double z) {
        this.x = x;
        this.z = z;
    }
    
    public static Vector2 of(double x, double z) {
        return new Vector2(x, z);
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
     *
     * @return Distance between vectors
     */
    public double distance(Vector2 other) {
        return FastMath.sqrt(distanceSquared(other));
    }
    
    /**
     * Get the squared distance between 2 vectors.
     *
     * @param other Another vector
     *
     * @return Squared distance
     */
    public double distanceSquared(Vector2 other) {
        double dx = other.getX() - x;
        double dz = other.getZ() - z;
        return dx * dx + dz * dz;
    }
    
    public Vector3 extrude(double y) {
        return Vector3.of(this.x, y, this.z);
    }
    
    /**
     * Get X component
     *
     * @return X component
     */
    public double getX() {
        return x;
    }
    
    
    /**
     * Get Z component
     *
     * @return Z component
     */
    public double getZ() {
        return z;
    }
    
    
    public int getBlockX() {
        return FastMath.floorToInt(x);
    }
    
    public int getBlockZ() {
        return FastMath.floorToInt(z);
    }
    
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + Double.hashCode(x);
        hash = 31 * hash + Double.hashCode(z);
        return hash;
    }
    
    public boolean equals(Object obj) {
        if(!(obj instanceof Vector2 other)) return false;
        return MathUtil.equals(this.x, other.x) && MathUtil.equals(this.z, other.z);
    }
    
    public Mutable mutable() {
        return new Mutable(x, z);
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + z + ")";
    }
    
    
    public static class Mutable extends Vector2 {
        
        private Mutable(double x, double z) {
            super(x, z);
        }
        
        public double getX() {
            return x;
        }
        
        public Mutable setX(double x) {
            this.x = x;
            return this;
        }
        
        public double getZ() {
            return z;
        }
        
        public Mutable setZ(double z) {
            this.z = z;
            return this;
        }
        
        public Vector2 immutable() {
            return Vector2.of(x, z);
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
        
        public Mutable add(double x, double z) {
            this.x += x;
            this.z += z;
            return this;
        }
        
        /**
         * Multiply X and Z components by a value.
         *
         * @param m Value to multiply
         *
         * @return Mutated vector, for chaining.
         */
        public Mutable multiply(double m) {
            x *= m;
            z *= m;
            return this;
        }
        
        /**
         * Add this vector to another.
         *
         * @param other Vector to add
         *
         * @return Mutated vector, for chaining.
         */
        public Mutable add(Vector2 other) {
            x += other.getX();
            z += other.getZ();
            return this;
        }
        
        /**
         * Subtract a vector from this vector,
         *
         * @param other Vector to subtract
         *
         * @return Mutated vector, for chaining.
         */
        public Mutable subtract(Vector2 other) {
            x -= other.getX();
            z -= other.getZ();
            return this;
        }
        
        /**
         * Normalize this vector to length 1
         *
         * @return Mutated vector, for chaining.
         */
        public Mutable normalize() {
            divide(length());
            return this;
        }
        
        /**
         * Divide X and Z components by a value.
         *
         * @param d Divisor
         *
         * @return Mutated vector, for chaining.
         */
        public Mutable divide(double d) {
            x /= d;
            z /= d;
            return this;
        }
    }
}
