package com.dfsek.terra.structure;

import com.dfsek.terra.procgen.math.Vector2;
import org.bukkit.util.Vector;

import java.io.Serializable;

public class StructureInfo implements Serializable {
    public static final long serialVersionUID = -175639605885943678L;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final int centerX;
    private final int centerZ;
    public StructureInfo(int sizeX, int sizeY, int sizeZ, Vector2 center) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.centerX = (int) center.getX();
        this.centerZ = (int) center.getZ();
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public double getMaxHorizontal() {
        return Math.sqrt(Math.pow(sizeX, 2) + Math.pow(sizeZ, 2));
    }
}
