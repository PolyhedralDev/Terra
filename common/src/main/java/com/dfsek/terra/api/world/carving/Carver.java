package com.dfsek.terra.api.world.carving;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.world.World;
import net.jafama.FastMath;

import java.util.Random;
import java.util.function.BiConsumer;

public abstract class Carver {
    private final int minY;
    private final int maxY;
    private final double sixtyFourSq = FastMath.pow(64, 2);
    private int carvingRadius = 4;

    public Carver(int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
    }

    public abstract void carve(int chunkX, int chunkZ, World w, BiConsumer<Vector3, CarvingType> consumer);

    public int getCarvingRadius() {
        return carvingRadius;
    }

    public void setCarvingRadius(int carvingRadius) {
        this.carvingRadius = carvingRadius;
    }

    public abstract Worm getWorm(long seed, Vector3 l);

    public abstract boolean isChunkCarved(World w, int chunkX, int chunkZ, Random r);

    public enum CarvingType {
        CENTER, WALL, TOP, BOTTOM
    }
}
