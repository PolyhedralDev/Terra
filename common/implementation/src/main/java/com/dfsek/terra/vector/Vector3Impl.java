package com.dfsek.terra.vector;

import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

/**
 * 3D Mutable Vector
 */
public class Vector3Impl implements Vector3 {
    private double x;
    private double y;
    private double z;

    public Vector3Impl(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public Vector3 setZ(double z) {
        this.z = z;
        return this;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public Vector3 setX(double x) {
        this.x = x;
        return this;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public Vector3 setY(double y) {
        this.y = y;
        return this;
    }

    @Override
    public int getBlockX() {
        return FastMath.floorToInt(x);
    }

    @Override
    public int getBlockY() {
        return FastMath.floorToInt(y);
    }

    @Override
    public int getBlockZ() {
        return FastMath.floorToInt(z);
    }

    @Override
    public Vector3 multiply(double m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }

    @Override
    public Vector3 add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public Vector3 add(Vector3 other) {
        this.x += other.getX();
        this.y += other.getY();
        this.z += other.getZ();
        return this;
    }

    @Override
    public Vector3 add(Vector2 other) {
        this.x += other.getX();
        this.z += other.getZ();
        return this;
    }

    @Override
    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    @Override
    public Vector3Impl clone() {
        try {
            return (Vector3Impl) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public double length() {
        return FastMath.sqrt(lengthSquared());
    }

    @Override
    public double inverseLength() {
        return FastMath.invSqrtQuick(lengthSquared());
    }

    @Override
    public boolean isNormalized() {
        return MathUtil.equals(this.lengthSquared(), 1);
    }

    @Override
    @NotNull
    public Vector3 rotateAroundX(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double y = angleCos * getY() - angleSin * getZ();
        double z = angleSin * getY() + angleCos * getZ();
        return setY(y).setZ(z);
    }

    @Override
    @NotNull
    public Vector3 rotateAroundY(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double x = angleCos * getX() + angleSin * getZ();
        double z = -angleSin * getX() + angleCos * getZ();
        return setX(x).setZ(z);
    }

    @Override
    @NotNull
    public Vector3 rotateAroundZ(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);

        double x = angleCos * getX() - angleSin * getY();
        double y = angleSin * getX() + angleCos * getY();
        return setX(x).setY(y);
    }

    @Override
    public double distance(@NotNull Vector3 o) {
        return FastMath.sqrt(FastMath.pow2(x - o.getX()) + FastMath.pow2(y - o.getY()) + FastMath.pow2(z - o.getZ()));
    }

    @Override
    public double distanceSquared(@NotNull Vector3 o) {
        return FastMath.pow2(x - o.getX()) + FastMath.pow2(y - o.getY()) + FastMath.pow2(z - o.getZ());
    }

    @Override
    @NotNull
    public Vector3 rotateAroundAxis(@NotNull Vector3 axis, double angle) throws IllegalArgumentException {
        return rotateAroundNonUnitAxis(axis.isNormalized() ? axis : axis.clone().normalize(), angle);
    }

    @Override
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

    @Override
    public double dot(@NotNull Vector3 other) {
        return x * other.getX() + y * other.getY() + z * other.getZ();
    }

    @Override
    public Location toLocation(World world) {
        return new LocationImpl(world, this.clone());
    }

    @Override
    public Vector3 normalize() {
        return this.multiply(this.inverseLength());
    }

    @Override
    public Vector3 subtract(int x, int y, int z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    @Override
    public Vector3 subtract(Vector3 end) {
        x -= end.getX();
        y -= end.getY();
        z -= end.getZ();
        return this;
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

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }

}
