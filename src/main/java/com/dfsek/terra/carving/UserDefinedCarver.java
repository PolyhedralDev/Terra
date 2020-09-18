package com.dfsek.terra.carving;

import com.dfsek.terra.MaxMin;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.BiomeConfig;
import com.dfsek.terra.config.CarverConfig;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.world.BlockPalette;
import org.polydev.gaea.world.carving.Carver;
import org.polydev.gaea.world.carving.Worm;

import java.util.Random;

public class UserDefinedCarver extends Carver {
    private BlockPalette inner;
    private BlockPalette walls;
    private final double[] start; // 0, 1, 2 = x, y, z.
    private final double[] mutate; // 0, 1, 2 = x, y, z. 3 = radius.
    private final double[] radiusMultiplier;
    private final MaxMin length;
    private final MaxMin radius;
    public UserDefinedCarver(MaxMin height, MaxMin radius, MaxMin length,  double[] start, double[] mutate, double[] radiusMultiplier) {
        super(height.getMin(), height.getMax());
        this.radius = radius;
        this.length = length;
        this.start = start;
        this.mutate = mutate;
        this.radiusMultiplier = radiusMultiplier;
    }

    @Override
    public Worm getWorm(long l, Vector vector) {
        Random r = new Random(l);
        return new UserDefinedWorm(length.get(r), r, vector, radius.getMax());
    }

    @Override
    public boolean isChunkCarved(World w, int chunkX, int chunkZ, Random random) {
        UserDefinedBiome b = (UserDefinedBiome) TerraBiomeGrid.fromWorld(w).getBiome(chunkX << 4, chunkZ << 4);
        return random.nextInt(100) < BiomeConfig.fromBiome(b).getCarverChance(this);
    }

    private class UserDefinedWorm extends Worm {
        private final Vector direction;
        private final int maxRad;
        private double runningRadius;
        public UserDefinedWorm(int length, Random r, Vector origin, int maxRad) {
            super(length, r, origin);
            runningRadius = radius.get(r);
            this.maxRad = maxRad;
            direction = new Vector((r.nextDouble()-0.5D)*start[0], (r.nextDouble()-0.5D)*start[1], (r.nextDouble()-0.5D)*start[2]).normalize();
        }

        @Override
        public void step() {
            setRadius(new int[] {(int) (runningRadius*radiusMultiplier[0]), (int) (runningRadius*radiusMultiplier[1]), (int) (runningRadius*radiusMultiplier[2])});
            runningRadius += (getRandom().nextDouble()-0.5)*mutate[3];
            runningRadius = Math.min(runningRadius, maxRad);
            direction.rotateAroundX(Math.toRadians(getRandom().nextDouble()*mutate[0]));
            direction.rotateAroundY(Math.toRadians(getRandom().nextDouble()*mutate[1]));
            direction.rotateAroundZ(Math.toRadians(getRandom().nextDouble()*mutate[2]));
            getRunning().add(direction);
        }
    }
}
