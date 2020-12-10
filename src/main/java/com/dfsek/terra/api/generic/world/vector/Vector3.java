package com.dfsek.terra.api.generic.world.vector;

import com.dfsek.terra.api.generic.world.World;
import net.jafama.FastMath;

public class Vector3 implements Cloneable {
    private double x;
    private double y;
    private double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Vector3 multiply(int m) {
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

    public double lengthSq() {
        return x * x + y * y + z * z;
    }

    public double length() {
        return FastMath.sqrt(lengthSq());
    }

    @Override
    public Vector3 clone() {
        try {
            return (Vector3) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public Location toLocation(World world) {
        return new Location(world, this.clone());
    }

}
