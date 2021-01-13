package com.dfsek.terra.carving;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.carving.Worm;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.biome.UserDefinedBiome;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CarverCache {

    private final World w;
    private final Map<Long, List<Worm.WormPoint>> carvers;
    private final TerraPlugin main;

    public CarverCache(World w, TerraPlugin main) {
        this.w = w;
        this.main = main;
        carvers = Collections.synchronizedMap(new LinkedHashMap<Long, List<Worm.WormPoint>>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return this.size() > main.getTerraConfig().getCarverCacheSize();
            }
        });
    }

    public List<Worm.WormPoint> getPoints(int chunkX, int chunkZ, UserDefinedCarver carver) {
        synchronized(carvers) {
            return carvers.computeIfAbsent(MathUtil.squash(chunkX, chunkZ), key -> {
                BiomeProvider provider = main.getWorld(w).getBiomeProvider();
                if(carver.isChunkCarved(w, chunkX, chunkZ, new FastRandom(MathUtil.getCarverChunkSeed(chunkX, chunkZ, w.getSeed() + carver.hashCode())))) {
                    long seed = MathUtil.getCarverChunkSeed(chunkX, chunkZ, w.getSeed());
                    carver.getSeedVar().setValue(seed);
                    Random r = new FastRandom(seed);
                    Worm carving = carver.getWorm(seed, new Vector3((chunkX << 4) + r.nextInt(16), carver.getConfig().getHeight().get(r), (chunkZ << 4) + r.nextInt(16)));
                    List<Worm.WormPoint> points = new GlueList<>();
                    for(int i = 0; i < carving.getLength(); i++) {
                        carving.step();
                        TerraBiome biome = provider.getBiome(carving.getRunning().toLocation(w));
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
}
