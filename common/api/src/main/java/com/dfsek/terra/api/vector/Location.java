package com.dfsek.terra.api.vector;

import com.dfsek.terra.api.world.World;

public interface Location extends Cloneable {
    void setWorld(World world);

    Vector3 getVector();

    void setVector(Vector3 vector);

    Location clone();

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    double getY();

    Location setY(double y);

    double getX();

    Location setX(double x);

    double getZ();

    Location setZ(double z);

    World getWorld();

    Location add(double x, double y, double z);

    Location subtract(int x, int y, int z);

    Location add(Vector3 add);

    Location add(Location add);

    double getPitch();

    void setPitch(double pitch);

    double getYaw();

    void setYaw(double yaw);

    Vector3 toVector();

    Location multiply(double v);
}
