package com.dfsek.terra.carving;

import com.dfsek.terra.api.gaea.biome.Biome;
import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import com.dfsek.terra.api.gaea.math.MathUtil;
import com.dfsek.terra.api.gaea.util.FastRandom;
import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.api.gaea.world.carving.Worm;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.vector.Vector3;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CarverCache {

    private final World w;
    private final Map<Long, List<Worm.WormPoint>> carvers = new HashMap<>();
    private final TerraPlugin main;

    public CarverCache(World w, TerraPlugin main) {
        this.w = w;
        this.main = main;
    }

    public List<Worm.WormPoint> getPoints(int chunkX, int chunkZ, UserDefinedCarver carver) {
        if(carvers.size() > main.getTerraConfig().getCacheSize() * 2) carvers.clear();
        return carvers.computeIfAbsent((((long) chunkX) << 32) | (chunkZ & 0xffffffffL), key -> {
            TerraBiomeGrid grid = main.getWorld(w).getGrid();
            if(carver.isChunkCarved(w, chunkX, chunkZ, new FastRandom(MathUtil.getCarverChunkSeed(chunkX, chunkZ, w.getSeed() + carver.hashCode())))) {
                long seed = MathUtil.getCarverChunkSeed(chunkX, chunkZ, w.getSeed());
                carver.getSeedVar().setValue(seed);
                Random r = new FastRandom(seed);
                Worm carving = carver.getWorm(seed, new Vector3((chunkX << 4) + r.nextInt(16), carver.getConfig().getHeight().get(r), (chunkZ << 4) + r.nextInt(16)));
                List<Worm.WormPoint> points = new GlueList<>();
                for(int i = 0; i < carving.getLength(); i++) {
                    carving.step();
                    Biome biome = grid.getBiome(carving.getRunning().toLocation(w), GenerationPhase.POPULATE);
                    if(!((UserDefinedBiome) biome).getConfig().getCarvers().containsKey(carver)) { // Stop if we enter a biome this carver is not present in
                        return new GlueList<>();
                    }
                    points.add(carving.getPoint());
                }
                return points;
            }
            return new GlueList<>();
        });
    }
}
