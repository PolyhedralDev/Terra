package com.dfsek.terra.api.world.carving;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.FastRandom;
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

    public void carve(int chunkX, int chunkZ, World w, BiConsumer<Vector3, CarvingType> consumer) {
        for(int x = chunkX - carvingRadius; x <= chunkX + carvingRadius; x++) {
            for(int z = chunkZ - carvingRadius; z <= chunkZ + carvingRadius; z++) {
                if(isChunkCarved(w, x, z, new FastRandom(MathUtil.hashToLong(this.getClass().getName() + "_" + x + "&" + z)))) {
                    long seed = MathUtil.getCarverChunkSeed(x, z, w.getSeed());
                    Random r = new FastRandom(seed);
                    Worm carving = getWorm(seed, new Vector3((x << 4) + r.nextInt(16), r.nextInt(maxY - minY + 1) + minY, (z << 4) + r.nextInt(16)));
                    Vector3 origin = carving.getOrigin();
                    for(int i = 0; i < carving.getLength(); i++) {
                        carving.step();
                        if(carving.getRunning().clone().setY(0).distanceSquared(origin.clone().setY(0)) > sixtyFourSq)
                            break;
                        if(FastMath.floorDiv(origin.getBlockX(), 16) != chunkX && FastMath.floorDiv(origin.getBlockZ(), 16) != chunkZ)
                            continue;
                        carving.getPoint().carve(chunkX, chunkZ, consumer);
                    }
                }
            }
        }
    }

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
