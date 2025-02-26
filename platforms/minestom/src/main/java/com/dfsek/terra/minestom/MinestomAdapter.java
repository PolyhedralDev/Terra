package com.dfsek.terra.minestom;

import com.dfsek.terra.api.util.vector.Vector3;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;


public class MinestomAdapter {
    public static Vector3 adapt(Point point) {
        return Vector3.of(point.x(), point.y(), point.z());
    }

    public static Pos adapt(Vector3 vector) {
        return new Pos(vector.getX(), vector.getY(), vector.getZ());
    }
}
