package com.dfsek.terra.carving;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.carving.Worm;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class CarverCache {

    private final LoadingCache<Long, List<Worm.WormPoint>> cache;
    private final UserDefinedCarver carver;

    public CarverCache(World w, TerraPlugin main, UserDefinedCarver carver) {
        this.carver = carver;
        cache = CacheBuilder.newBuilder().maximumSize(main.getTerraConfig().getCarverCacheSize())
                .build(new CacheLoader<Long, List<Worm.WormPoint>>() {
                    @Override
                    public List<Worm.WormPoint> load(@NotNull Long key) {
                        int chunkX = (int) (key >> 32);
                        int chunkZ = (int) key.longValue();
                        BiomeProvider provider = main.getWorld(w).getBiomeProvider();
                        if(CarverCache.this.carver.isChunkCarved(w, chunkX, chunkZ, new FastRandom(MathUtil.getCarverChunkSeed(chunkX, chunkZ, w.getSeed() + CarverCache.this.carver.hashCode())))) {
                            long seed = MathUtil.getCarverChunkSeed(chunkX, chunkZ, w.getSeed());
                            Random r = new FastRandom(seed);
                            Worm carving = CarverCache.this.carver.getWorm(seed, new Vector3((chunkX << 4) + r.nextInt(16), CarverCache.this.carver.getConfig().getHeight().get(r), (chunkZ << 4) + r.nextInt(16)));
                            List<Worm.WormPoint> points = new GlueList<>();
                            for(int i = 0; i < carving.getLength(); i++) {
                                carving.step();
                                TerraBiome biome = provider.getBiome(carving.getRunning().toLocation(w));
                                if(!((UserDefinedBiome) biome).getConfig().getCarvers().containsKey(CarverCache.this.carver)) { // Stop if we enter a biome this carver is not present in
                                    return new GlueList<>();
                                }
                                points.add(carving.getPoint());
                            }
                            return points;
                        }
                        return new GlueList<>();
                    }
                });
    }

    public List<Worm.WormPoint> getPoints(int chunkX, int chunkZ) {
        return cache.getUnchecked(MathUtil.squash(chunkX, chunkZ));
    }
}
