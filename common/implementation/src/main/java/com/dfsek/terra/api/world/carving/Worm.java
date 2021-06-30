package com.dfsek.terra.api.world.carving;

import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;
import net.jafama.FastMath;

import java.util.Random;
import java.util.function.BiConsumer;

public abstract class Worm {
    private final Random r;
    private final Vector3 origin;
    private final Vector3 running;
    private final int length;
    private int topCut = 0;
    private int bottomCut = 0;
    private int[] radius = new int[] {0, 0, 0};

    public Worm(int length, Random r, Vector3 origin) {
        this.r = r;
        this.length = length;
        this.origin = origin;
        this.running = origin;
    }

    public void setBottomCut(int bottomCut) {
        this.bottomCut = bottomCut;
    }

    public void setTopCut(int topCut) {
        this.topCut = topCut;
    }

    public Vector3 getOrigin() {
        return origin;
    }

    public int getLength() {
        return length;
    }

    public Vector3 getRunning() {
        return running;
    }

    public WormPoint getPoint() {
        return new WormPoint(running, radius, topCut, bottomCut);
    }

    public int[] getRadius() {
        return radius;
    }

    public void setRadius(int[] radius) {
        this.radius = radius;
    }

    public Random getRandom() {
        return r;
    }

    public abstract void step();

    public static class WormPoint {
        private final Vector3 origin;
        private final int topCut;
        private final int bottomCut;
        private final int[] rad;

        public WormPoint(Vector3 origin, int[] rad, int topCut, int bottomCut) {
            this.origin = origin;
            this.rad = rad;
            this.topCut = topCut;
            this.bottomCut = bottomCut;
        }

        private static double ellipseEquation(int x, int y, int z, double xr, double yr, double zr) {
            return (FastMath.pow2(x) / FastMath.pow2(xr + 0.5D)) + (FastMath.pow2(y) / FastMath.pow2(yr + 0.5D)) + (FastMath.pow2(z) / FastMath.pow2(zr + 0.5D));
        }

        public Vector3 getOrigin() {
            return origin;
        }

        public int getRadius(int index) {
            return rad[index];
        }

        public void carve(int chunkX, int chunkZ, BiConsumer<Vector3, Carver.CarvingType> consumer, World world) {
            int xRad = getRadius(0);
            int yRad = getRadius(1);
            int zRad = getRadius(2);
            int originX = (chunkX << 4);
            int originZ = (chunkZ << 4);
            for(int x = -xRad - 1; x <= xRad + 1; x++) {
                if(!(FastMath.floorDiv(origin.getBlockX() + x, 16) == chunkX)) continue;
                for(int z = -zRad - 1; z <= zRad + 1; z++) {
                    if(!(FastMath.floorDiv(origin.getBlockZ() + z, 16) == chunkZ)) continue;
                    for(int y = -yRad - 1; y <= yRad + 1; y++) {
                        Vector3 position = origin.clone().add(new Vector3(x, y, z));
                        if(position.getY() < world.getMinHeight() || position.getY() > world.getMaxHeight()) continue;
                        double eq = ellipseEquation(x, y, z, xRad, yRad, zRad);
                        if(eq <= 1 &&
                                y >= -yRad - 1 + bottomCut && y <= yRad + 1 - topCut) {
                            consumer.accept(new Vector3(position.getBlockX() - originX, position.getBlockY(), position.getBlockZ() - originZ), Carver.CarvingType.CENTER);
                        } else if(eq <= 1.5) {
                            Carver.CarvingType type = Carver.CarvingType.WALL;
                            if(y <= -yRad - 1 + bottomCut) {
                                type = Carver.CarvingType.BOTTOM;
                            } else if(y >= yRad + 1 - topCut) {
                                type = Carver.CarvingType.TOP;
                            }
                            consumer.accept(new Vector3(position.getBlockX() - originX, position.getBlockY(), position.getBlockZ() - originZ), type);
                        }
                    }
                }
            }
        }
    }
}
