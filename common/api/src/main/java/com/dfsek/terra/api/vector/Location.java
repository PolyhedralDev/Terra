package com.dfsek.terra.api.vector;

import com.dfsek.terra.api.block.Block;
import com.dfsek.terra.api.world.World;

import java.util.Objects;

@Deprecated
public class Location implements Cloneable {
    private World world;
    private Vector3 vector;
    private double pitch;
    private double yaw;

    public Location(World w, double x, double y, double z) {
        this.world = w;
        this.vector = new Vector3Impl(x, y, z);
    }

    public Location(World w, Vector3 vector) {
        this.world = w;
        this.vector = vector;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Vector3 getVector() {
        return vector;
    }

    public void setVector(Vector3 vector) {
        this.vector = vector;
    }

    @Override
    public Location clone() {
        try {
            Location other = (Location) super.clone();
            other.setVector(other.getVector().clone());
            return other;
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public int getBlockX() {
        return vector.getBlockX();
    }

    public int getBlockY() {
        return vector.getBlockY();
    }

    public int getBlockZ() {
        return vector.getBlockZ();
    }

    public double getY() {
        return vector.getY();
    }

    public Location setY(double y) {
        vector.setY(y);
        return this;
    }

    public double getX() {
        return vector.getX();
    }

    public Location setX(double x) {
        vector.setX(x);
        return this;
    }

    public double getZ() {
        return vector.getZ();
    }

    public Location setZ(double z) {
        vector.setZ(z);
        return this;
    }

    public World getWorld() {
        return world;
    }

    public Location add(double x, double y, double z) {
        vector.add(x, y, z);
        return this;
    }

    public Block getBlock() {
        return world.getBlockAt(this);
    }

    public Location subtract(int x, int y, int z) {
        vector.subtract(x, y, z);
        return this;
    }

    public Location add(Vector3 add) {
        vector.add(add);
        return this;
    }

    public Location add(Location add) {
        vector.add(add.toVector());
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Location)) {
            return false;
        }
        final Location other = (Location) obj;

        World world = this.world;
        World otherWorld = other.world;
        if(!Objects.equals(world, otherWorld)) {
            return false;
        }
        if(Double.doubleToLongBits(this.vector.getX()) != Double.doubleToLongBits(other.vector.getX())) {
            return false;
        }
        if(Double.doubleToLongBits(this.vector.getY()) != Double.doubleToLongBits(other.vector.getY())) {
            return false;
        }
        return Double.doubleToLongBits(this.vector.getZ()) == Double.doubleToLongBits(other.vector.getZ());
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        World world = (this.world == null) ? null : this.world;
        hash = 19 * hash + (world != null ? world.hashCode() : 0);
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.vector.getX()) ^ (Double.doubleToLongBits(this.vector.getX()) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.vector.getY()) ^ (Double.doubleToLongBits(this.vector.getY()) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.vector.getZ()) ^ (Double.doubleToLongBits(this.vector.getZ()) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.pitch) ^ Double.doubleToLongBits(this.pitch) >>> 32);
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.yaw) ^ Double.doubleToLongBits(this.yaw) >>> 32);
        return hash;
    }

    public Vector3 toVector() {
        return vector.clone();
    }

    @Override
    public String toString() {
        return "[" + world + ": (" + getX() + ", " + getY() + ", " + getZ() + ")]";
    }

    public Location multiply(double v) {
        vector.multiply(v);
        return this;
    }
}
