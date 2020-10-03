package com.dfsek.terra.structure;

import org.bukkit.util.Vector;

import java.io.Serializable;

public class GaeaStructureInfo implements Serializable {
    public static final long serialVersionUID = -175639605885943678L;
    private final int sizeX;
    private final int sizeY;
    private final int sizeZ;
    private final int centerX;
    private final int centerZ;
    private final int centerY;
    public GaeaStructureInfo(int sizeX, int sizeY, int sizeZ, Vector center) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.centerX = center.getBlockX();
        this.centerZ = center.getBlockZ();
        this.centerY = center.getBlockY();
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

    public int getCenterY() {
        return centerY;
    }

    public int getCenterZ() {
        return centerZ;
    }

    public double getMaxHorizontal() {
        return Math.sqrt(Math.pow(sizeX, 2) + Math.pow(sizeZ, 2));
    }
}
