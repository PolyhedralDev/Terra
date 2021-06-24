package com.dfsek.terra.vector;

import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;

import java.util.Objects;

public class LocationImpl implements Location {
    private World world;
    private Vector3 vector;
    private double pitch;
    private double yaw;

    public LocationImpl(World w, double x, double y, double z) {
        this.world = w;
        this.vector = new Vector3Impl(x, y, z);
    }

    public LocationImpl(World w, Vector3 vector) {
        this.world = w;
        this.vector = vector;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public Vector3 getVector() {
        return vector;
    }

    @Override
    public void setVector(Vector3 vector) {
        this.vector = vector;
    }

    @Override
    public Location clone() {
        try {
            LocationImpl other = (LocationImpl) super.clone();
            other.setVector(other.getVector().clone());
            return other;
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public int getBlockX() {
        return vector.getBlockX();
    }

    @Override
    public int getBlockY() {
        return vector.getBlockY();
    }

    @Override
    public int getBlockZ() {
        return vector.getBlockZ();
    }

    @Override
    public double getY() {
        return vector.getY();
    }

    @Override
    public Location setY(double y) {
        vector.setY(y);
        return this;
    }

    @Override
    public double getX() {
        return vector.getX();
    }

    @Override
    public Location setX(double x) {
        vector.setX(x);
        return this;
    }

    @Override
    public double getZ() {
        return vector.getZ();
    }

    @Override
    public LocationImpl setZ(double z) {
        vector.setZ(z);
        return this;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Location add(double x, double y, double z) {
        vector.add(x, y, z);
        return this;
    }

    @Override
    public Location subtract(int x, int y, int z) {
        vector.subtract(x, y, z);
        return this;
    }

    @Override
    public Location add(Vector3 add) {
        vector.add(add);
        return this;
    }

    @Override
    public Location add(Location add) {
        vector.add(add.toVector());
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof LocationImpl)) {
            return false;
        }
        final LocationImpl other = (LocationImpl) obj;

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

    @Override
    public double getPitch() {
        return pitch;
    }

    @Override
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    @Override
    public double getYaw() {
        return yaw;
    }

    @Override
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

    @Override
    public Vector3 toVector() {
        return vector.clone();
    }

    @Override
    public String toString() {
        return "[" + world + ": (" + getX() + ", " + getY() + ", " + getZ() + ")]";
    }

    @Override
    public LocationImpl multiply(double v) {
        vector.multiply(v);
        return this;
    }
}
