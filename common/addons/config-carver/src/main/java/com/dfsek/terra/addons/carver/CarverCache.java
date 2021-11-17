package com.dfsek.terra.addons.carver;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.dfsek.terra.addons.carver.carving.Worm;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class CarverCache {
    
    private final LoadingCache<Long, List<Worm.WormPoint>> cache;
    private final UserDefinedCarver carver;
    
    public CarverCache(World w, Platform platform, UserDefinedCarver carver) {
        this.carver = carver;
        cache = CacheBuilder.newBuilder().maximumSize(platform.getTerraConfig().getCarverCacheSize())
                            .build(new CacheLoader<>() {
                                @Override
                                public List<Worm.WormPoint> load(@NotNull Long key) {
                                    int chunkX = (int) (key >> 32);
                                    int chunkZ = (int) key.longValue();
                                    BiomeProvider provider = w.getBiomeProvider();
                                    if(CarverCache.this.carver.isChunkCarved(w, chunkX, chunkZ, new Random(
                                            PopulationUtil.getCarverChunkSeed(chunkX, chunkZ,
                                                                              w.getSeed() + CarverCache.this.carver.hashCode())))) {
                                        long seed = PopulationUtil.getCarverChunkSeed(chunkX, chunkZ, w.getSeed());
                                        Random r = new Random(seed);
                                        Worm carving = CarverCache.this.carver.getWorm(seed, new Vector3((chunkX << 4) + r.nextInt(16),
                                                                                                         CarverCache.this.carver.getConfig()
                                                                                                                                .getHeight()
                                                                                                                                .get(r),
                                                                                                         (chunkZ << 4) + r.nextInt(16)));
                                        List<Worm.WormPoint> points = new ArrayList<>();
                                        for(int i = 0; i < carving.getLength(); i++) {
                                            carving.step();
                                            TerraBiome biome = provider.getBiome(carving.getRunning(), w.getSeed());
                                /*
                                if(!((UserDefinedBiome) biome).getConfig().getCarvers().containsKey(CarverCache.this.carver)) { // Stop
                                if we enter a biome this carver is not present in
                                    return Collections.emptyList();
                                }

                                 */
                                            points.add(carving.getPoint());
                                        }
                                        return points;
                                    }
                                    return Collections.emptyList();
                                }
                            });
    }
    
    public List<Worm.WormPoint> getPoints(int chunkX, int chunkZ) {
        return cache.getUnchecked(MathUtil.squash(chunkX, chunkZ));
    }
}
