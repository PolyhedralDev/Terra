package com.dfsek.terra;

import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.BiomeConfig;
import com.dfsek.terra.config.CarverConfig;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.world.carving.Carver;
import org.polydev.gaea.world.carving.Worm;

import java.util.Random;

public class UserDefinedCarver extends Carver {
    private final CarverConfig config;
    public UserDefinedCarver(CarverConfig config) {
        super(config.getHeight().getMin(), config.getHeight().getMax());
        this.config = config;
    }

    @Override
    public Worm getWorm(long l, Vector vector) {
        Random r = new Random(l);
        return new UserDefinedWorm(config.getLength().get(r), r, vector, 6);
    }

    public CarverConfig getConfig() {
        return config;
    }

    @Override
    public boolean isChunkCarved(World w, int chunkX, int chunkZ, Random random) {
        UserDefinedBiome b = (UserDefinedBiome) TerraBiomeGrid.fromWorld(w).getBiome(chunkX << 4, chunkZ << 4);
        return random.nextInt(100) < BiomeConfig.fromBiome(b).getCarverChance(config);
    }

    private static class UserDefinedWorm extends Worm {
        private final Vector direction;
        private final int maxRad;
        private double runningRadius;
        public UserDefinedWorm(int length, Random r, Vector origin, int maxRad) {
            super(length, r, origin);
            runningRadius = (r.nextDouble()/2+0.5)*4;
            this.maxRad = maxRad;
            direction = new Vector(r.nextDouble()-0.5D, (r.nextDouble()-0.5D)/4, r.nextDouble()-0.5D).normalize();
        }

        @Override
        public void step() {
            setRadius(new int[] {(int) runningRadius, (int) runningRadius, (int) runningRadius});
            runningRadius += (getRandom().nextDouble()-0.5)/8;
            runningRadius = Math.min(runningRadius, maxRad);
            direction.rotateAroundX(Math.toRadians(getRandom().nextDouble()*2));
            direction.rotateAroundY(Math.toRadians(getRandom().nextDouble()*6));
            direction.rotateAroundZ(Math.toRadians(getRandom().nextDouble()*2));
            getRunning().add(direction);
        }
    }
}
