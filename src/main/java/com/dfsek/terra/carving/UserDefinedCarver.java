package com.dfsek.terra.carving;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import net.jafama.FastMath;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.world.carving.Carver;
import org.polydev.gaea.world.carving.Worm;

import java.util.Random;

public class UserDefinedCarver extends Carver {
    private final double[] start; // 0, 1, 2 = x, y, z.
    private final double[] mutate; // 0, 1, 2 = x, y, z. 3 = radius.
    private final double[] radiusMultiplier;
    private final Range length;
    private final Range radius;
    private final int hash;
    private final int topCut;
    private final int bottomCut;
    private double step = 2;
    private Range recalc = new Range(8, 10);
    private double recalcMagnitude = 3;
    private final CarverTemplate config;

    public UserDefinedCarver(Range height, Range radius, Range length, double[] start, double[] mutate, double[] radiusMultiplier, int hash, int topCut, int bottomCut, CarverTemplate config) {
        super(height.getMin(), height.getMax());
        this.radius = radius;
        this.length = length;
        this.start = start;
        this.mutate = mutate;
        this.radiusMultiplier = radiusMultiplier;
        this.hash = hash;
        this.topCut = topCut;
        this.bottomCut = bottomCut;
        this.config = config;
    }

    @Override
    public Worm getWorm(long l, Vector vector) {
        Random r = new FastRandom(l + hash);
        return new UserDefinedWorm(length.get(r) / 2, r, vector, radius.getMax(), topCut, bottomCut);
    }

    public void setStep(double step) {
        this.step = step;
    }

    public void setRecalc(Range recalc) {
        this.recalc = recalc;
    }

    public void setRecalcMagnitude(double recalcMagnitude) {
        this.recalcMagnitude = recalcMagnitude;
    }

    @Override
    public boolean isChunkCarved(World w, int chunkX, int chunkZ, Random random) {
        BiomeTemplate conf = ((UserDefinedBiome) TerraWorld.getWorld(w).getGrid().getBiome(chunkX << 4, chunkZ << 4, GenerationPhase.POPULATE)).getConfig();
        if(conf.getCarvers().get(this) != null) {
            return new FastRandom(random.nextLong() + hash).nextInt(100) < conf.getCarvers().get(this);
        }
        return false;
    }

    public CarverTemplate getConfig() {
        return config;
    }

    private class UserDefinedWorm extends Worm {
        private final Vector direction;
        private final int maxRad;
        private double runningRadius;
        private int steps;
        private int nextDirection = 0;
        private double[] currentRotation = new double[3];

        public UserDefinedWorm(int length, Random r, Vector origin, int maxRad, int topCut, int bottomCut) {
            super(length, r, origin);
            super.setTopCut(topCut);
            super.setBottomCut(bottomCut);
            runningRadius = radius.get(r);
            this.maxRad = maxRad;
            direction = new Vector((r.nextDouble() - 0.5D) * start[0], (r.nextDouble() - 0.5D) * start[1], (r.nextDouble() - 0.5D) * start[2]).normalize().multiply(step);
        }

        @Override
        public void step() {
            if(steps == nextDirection) {
                direction.rotateAroundX(FastMath.toRadians((getRandom().nextGaussian()) * mutate[0] * recalcMagnitude));
                direction.rotateAroundY(FastMath.toRadians((getRandom().nextGaussian()) * mutate[1] * recalcMagnitude));
                direction.rotateAroundZ(FastMath.toRadians((getRandom().nextGaussian()) * mutate[2] * recalcMagnitude));
                currentRotation = new double[] {(getRandom().nextGaussian()) * mutate[0],
                        (getRandom().nextGaussian()) * mutate[1],
                        (getRandom().nextGaussian()) * mutate[2]};
                nextDirection += recalc.get(getRandom());
            }
            steps++;
            setRadius(new int[] {(int) (runningRadius * radiusMultiplier[0]), (int) (runningRadius * radiusMultiplier[1]), (int) (runningRadius * radiusMultiplier[2])});
            runningRadius += (getRandom().nextDouble() - 0.5) * mutate[3];
            runningRadius = FastMath.max(FastMath.min(runningRadius, maxRad), 1);
            direction.rotateAroundX(FastMath.toRadians(currentRotation[0] * mutate[0]));
            direction.rotateAroundY(FastMath.toRadians(currentRotation[1] * mutate[1]));
            direction.rotateAroundZ(FastMath.toRadians(currentRotation[2] * mutate[2]));
            getRunning().add(direction);
        }
    }
}
