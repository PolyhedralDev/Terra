package com.dfsek.terra.api.vector;

/**
 * oh yeah
 */
public interface Vector2 extends Cloneable {
    /**
     * Get X component
     *
     * @return X component
     */
    double getX();

    Vector2 clone();

    Vector2 setX(double x);

    /**
     * Get Z component
     *
     * @return Z component
     */
    double getZ();

    Vector2 setZ(double z);

    /**
     * Multiply X and Z components by a value.
     *
     * @param m Value to multiply
     * @return Mutated vector, for chaining.
     */
    Vector2 multiply(double m);

    /**
     * Add this vector to another.
     *
     * @param other Vector to add
     * @return Mutated vector, for chaining.
     */
    Vector2 add(Vector2 other);

    /**
     * Subtract a vector from this vector,
     *
     * @param other Vector to subtract
     * @return Mutated vector, for chaining.
     */
    Vector2 subtract(Vector2 other);

    /**
     * Normalize this vector to length 1
     *
     * @return Mutated vector, for chaining.
     */
    Vector2 normalize();

    /**
     * Divide X and Z components by a value.
     *
     * @param d Divisor
     * @return Mutated vector, for chaining.
     */
    Vector2 divide(double d);

    /**
     * Get the length of this Vector
     *
     * @return length
     */
    double length();

    /**
     * Get the squared length of this Vector
     *
     * @return squared length
     */
    double lengthSquared();

    /**
     * Get the distance from this vector to another.
     *
     * @param other Another vector
     * @return Distance between vectors
     */
    double distance(Vector2 other);

    /**
     * Get the squared distance between 2 vectors.
     *
     * @param other Another vector
     * @return Squared distance
     */
    double distanceSquared(Vector2 other);

    Vector2 add(double x, double z);

    int getBlockX();

    int getBlockZ();
}
