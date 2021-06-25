package com.dfsek.terra.api.vector;

import com.dfsek.terra.api.world.World;
import org.jetbrains.annotations.NotNull;

public interface Vector3 extends Cloneable {
    double getZ();

    Vector3 setZ(double z);

    double getX();

    Vector3 setX(double x);

    double getY();

    Vector3 setY(double y);

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    Vector3 multiply(double m);

    Vector3 add(double x, double y, double z);

    Vector3 add(Vector3 other);

    Vector3 add(Vector2 other);

    double lengthSquared();

    double length();

    double inverseLength();

    /**
     * Returns if a vector is normalized
     *
     * @return whether the vector is normalised
     */
    boolean isNormalized();

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
     * @return the same vector
     */
    @NotNull Vector3 rotateAroundX(double angle);

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
     * @return the same vector
     */
    @NotNull Vector3 rotateAroundY(double angle);

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
     * @return the same vector
     */
    @NotNull Vector3 rotateAroundZ(double angle);

    /**
     * Get the distance between this vector and another. The value of this
     * method is not cached and uses a costly square-root function, so do not
     * repeatedly call this method to get the vector's magnitude. NaN will be
     * returned if the inner result of the sqrt() function overflows, which
     * will be caused if the distance is too long.
     *
     * @param o The other vector
     * @return the distance
     */
    double distance(@NotNull Vector3 o);

    /**
     * Get the squared distance between this vector and another.
     *
     * @param o The other vector
     * @return the distance
     */
    double distanceSquared(@NotNull Vector3 o);

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
     * @return the same vector
     * @throws IllegalArgumentException if the provided axis vector instance is
     *                                  null
     */
    @NotNull Vector3 rotateAroundAxis(@NotNull Vector3 axis, double angle) throws IllegalArgumentException;

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
     * @return the same vector
     * @throws IllegalArgumentException if the provided axis vector instance is
     *                                  null
     */
    @NotNull Vector3 rotateAroundNonUnitAxis(@NotNull Vector3 axis, double angle) throws IllegalArgumentException;

    /**
     * Calculates the dot product of this vector with another. The dot product
     * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
     *
     * @param other The other vector
     * @return dot product
     */
    double dot(@NotNull Vector3 other);

    Location toLocation(World world);

    Vector3 normalize();

    Vector3 subtract(int x, int y, int z);

    Vector3 subtract(Vector3 end);

    Vector3 clone();
}
