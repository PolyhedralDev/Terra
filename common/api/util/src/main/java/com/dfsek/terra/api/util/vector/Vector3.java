/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.vector;

import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.util.MathUtil;


public class Vector3 implements Cloneable {
    private double x;
    private double y;
    private double z;
    
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3 multiply(double m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }
    
    public Vector3 add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vector3 add(Vector3 other) {
        this.x += other.getX();
        this.y += other.getY();
        this.z += other.getZ();
        return this;
    }
    
    public Vector3 add(Vector2 other) {
        this.x += other.getX();
        this.z += other.getZ();
        return this;
    }
    
    public double lengthSquared() {
        return x * x + y * y + z * z;
    }
    
    public double length() {
        return FastMath.sqrt(lengthSquared());
    }
    
    public double inverseLength() {
        return FastMath.invSqrtQuick(lengthSquared());
    }
    
    /**
     * Rotates the vector around the x axis.
     * <p>
     * This piece of math is based on the standard rotation matrix for vectors
     * in three dimensional space. This matrix can be found here:
     * <a href="https://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">Rotation
     * Matrix</a>.
     *
     * @param angle the angle to rotate the vector about. This angle is passed
     *              in radians
     *
     * @return the same vector
     */
    @NotNull
    public Vector3 rotateAroundX(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        
        double y = angleCos * getY() - angleSin * getZ();
        double z = angleSin * getY() + angleCos * getZ();
        return setY(y).setZ(z);
    }
    
    /**
     * Rotates the vector around the y axis.
     * <p>
     * This piece of math is based on the standard rotation matrix for vectors
     * in three dimensional space. This matrix can be found here:
     * <a href="https://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">Rotation
     * Matrix</a>.
     *
     * @param angle the angle to rotate the vector about. This angle is passed
     *              in radians
     *
     * @return the same vector
     */
    @NotNull
    public Vector3 rotateAroundY(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        
        double x = angleCos * getX() + angleSin * getZ();
        double z = -angleSin * getX() + angleCos * getZ();
        return setX(x).setZ(z);
    }
    
    /**
     * Rotates the vector around the z axis
     * <p>
     * This piece of math is based on the standard rotation matrix for vectors
     * in three dimensional space. This matrix can be found here:
     * <a href="https://en.wikipedia.org/wiki/Rotation_matrix#Basic_rotations">Rotation
     * Matrix</a>.
     *
     * @param angle the angle to rotate the vector about. This angle is passed
     *              in radians
     *
     * @return the same vector
     */
    @NotNull
    public Vector3 rotateAroundZ(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        
        double x = angleCos * getX() - angleSin * getY();
        double y = angleSin * getX() + angleCos * getY();
        return setX(x).setY(y);
    }
    
    /**
     * Get the distance between this vector and another. The value of this
     * method is not cached and uses a costly square-root function, so do not
     * repeatedly call this method to get the vector's magnitude. NaN will be
     * returned if the inner result of the sqrt() function overflows, which
     * will be caused if the distance is too long.
     *
     * @param o The other vector
     *
     * @return the distance
     */
    public double distance(@NotNull Vector3 o) {
        return FastMath.sqrt(FastMath.pow2(x - o.getX()) + FastMath.pow2(y - o.getY()) + FastMath.pow2(z - o.getZ()));
    }
    
    /**
     * Get the squared distance between this vector and another.
     *
     * @param o The other vector
     *
     * @return the distance
     */
    public double distanceSquared(@NotNull Vector3 o) {
        return FastMath.pow2(x - o.getX()) + FastMath.pow2(y - o.getY()) + FastMath.pow2(z - o.getZ());
    }
    
    /**
     * Rotates the vector around a given arbitrary axis in 3 dimensional space.
     *
     * <p>
     * Rotation will follow the general Right-Hand-Rule, which means rotation
     * will be counterclockwise when the axis is pointing towards the observer.
     * <p>
     * This method will always make sure the provided axis is a unit vector, to
     * not modify the length of the vector when rotating.
     *
     * @param axis  the axis to rotate the vector around. If the passed vector is
     *              not of length 1, it gets copied and normalized before using it for the
     *              rotation. Please use {@link Vector3#normalize()} on the instance before
     *              passing it to this method
     * @param angle the angle to rotate the vector around the axis
     *
     * @return the same vector
     *
     * @throws IllegalArgumentException if the provided axis vector instance is
     *                                  null
     */
    @NotNull
    public Vector3 rotateAroundAxis(@NotNull Vector3 axis, double angle) throws IllegalArgumentException {
        return rotateAroundNonUnitAxis(axis.isNormalized() ? axis : axis.clone().normalize(), angle);
    }
    
    /**
     * Rotates the vector around a given arbitrary axis in 3 dimensional space.
     *
     * <p>
     * Rotation will follow the general Right-Hand-Rule, which means rotation
     * will be counterclockwise when the axis is pointing towards the observer.
     * <p>
     * Note that the vector length will change accordingly to the axis vector
     * length. If the provided axis is not a unit vector, the rotated vector
     * will not have its previous length. The scaled length of the resulting
     * vector will be related to the axis vector.
     *
     * @param axis  the axis to rotate the vector around.
     * @param angle the angle to rotate the vector around the axis
     *
     * @return the same vector
     *
     * @throws IllegalArgumentException if the provided axis vector instance is
     *                                  null
     */
    @NotNull
    public Vector3 rotateAroundNonUnitAxis(@NotNull Vector3 axis, double angle) throws IllegalArgumentException {
        double x = getX(), y = getY(), z = getZ();
        double x2 = axis.getX(), y2 = axis.getY(), z2 = axis.getZ();
        
        double cosTheta = Math.cos(angle);
        double sinTheta = Math.sin(angle);
        double dotProduct = this.dot(axis);
        
        double xPrime = x2 * dotProduct * (1d - cosTheta)
                        + x * cosTheta
                        + (-z2 * y + y2 * z) * sinTheta;
        double yPrime = y2 * dotProduct * (1d - cosTheta)
                        + y * cosTheta
                        + (z2 * x - x2 * z) * sinTheta;
        double zPrime = z2 * dotProduct * (1d - cosTheta)
                        + z * cosTheta
                        + (-y2 * x + x2 * y) * sinTheta;
        
        return setX(xPrime).setY(yPrime).setZ(zPrime);
    }
    
    /**
     * Calculates the dot product of this vector with another. The dot product
     * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
     *
     * @param other The other vector
     *
     * @return dot product
     */
    public double dot(@NotNull Vector3 other) {
        return x * other.getX() + y * other.getY() + z * other.getZ();
    }
    
    public Vector3 normalize() {
        return this.multiply(this.inverseLength());
    }
    
    public Vector3 subtract(int x, int y, int z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }
    
    public Vector3 subtract(Vector3 end) {
        x -= end.getX();
        y -= end.getY();
        z -= end.getZ();
        return this;
    }
    
    public double getZ() {
        return z;
    }
    
    public Vector3 setZ(double z) {
        this.z = z;
        return this;
    }
    
    public double getX() {
        return x;
    }
    
    public Vector3 setX(double x) {
        this.x = x;
        return this;
    }
    
    public double getY() {
        return y;
    }
    
    public Vector3 setY(double y) {
        this.y = y;
        return this;
    }
    
    public int getBlockX() {
        return FastMath.floorToInt(x);
    }
    
    public int getBlockY() {
        return FastMath.floorToInt(y);
    }
    
    public int getBlockZ() {
        return FastMath.floorToInt(z);
    }
    
    /**
     * Returns if a vector is normalized
     *
     * @return whether the vector is normalised
     */
    public boolean isNormalized() {
        return MathUtil.equals(this.lengthSquared(), 1);
    }
    
    /**
     * Returns a hash code for this vector
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 79 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        return hash;
    }
    
    /**
     * Checks to see if two objects are equal.
     * <p>
     * Only two Vectors can ever return true. This method uses a fuzzy match
     * to account for floating point errors. The epsilon can be retrieved
     * with epsilon.
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Vector3)) return false;
        Vector3 other = (Vector3) obj;
        return MathUtil.equals(x, other.getX()) && MathUtil.equals(y, other.getY()) && MathUtil.equals(z, other.getZ());
    }
    
    public Vector3 clone() {
        try {
            return (Vector3) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
    
    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }
}
