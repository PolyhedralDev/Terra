package com.dfsek.terra.generation.math;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SamplerCache {
    private final Map<Long, Container> containerMap;
    private final TerraPlugin main;

    public SamplerCache(TerraPlugin main) {
        containerMap = Collections.synchronizedMap(new HashMap<>());
        this.main = main;
    }

    public Sampler get(World world, int x, int z) {
        synchronized(containerMap) {
            return containerMap.computeIfAbsent(world.getSeed(), seed -> new Container(world)).get(x, z);
        }
    }

    public Sampler getChunk(World world, int chunkX, int chunkZ) {
        synchronized(containerMap) {
            return containerMap.computeIfAbsent(world.getSeed(), seed -> new Container(world)).getChunk(chunkX, chunkZ);
        }
    }

    public void clear() {
        containerMap.clear();
    }

    private class Container {
        private final TerraWorld terraWorld;
        private final LoadingCache<Long, Sampler> cache;

        private Container(World world) {
            cache = CacheBuilder.newBuilder().maximumSize(main.getTerraConfig().getSamplerCache())
                    .build(new CacheLoader<Long, Sampler>() {
                        @Override
                        public Sampler load(@NotNull Long key) {
                            int cx = (int) (key >> 32);
                            int cz = (int) key.longValue();
                            return new Sampler(cx, cz, terraWorld.getBiomeProvider(), world, terraWorld.getConfig().getTemplate().getElevationBlend(), terraWorld.getConfig().getTemplate().getBaseBlend());
                        }
                    });
            terraWorld = main.getWorld(world);
        }

        public Sampler get(int x, int z) {
            int cx = FastMath.floorDiv(x, 16);
            int cz = FastMath.floorDiv(z, 16);
            return getChunk(cx, cz);
        }

        public Sampler getChunk(int cx, int cz) {
            long key = MathUtil.squash(cx, cz);
            return cache.getUnchecked(key);
        }
    }
}
