package com.dfsek.terra.generation.math;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import net.jafama.FastMath;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SamplerCache {
    private final Map<Long, Container> cache;
    private final TerraPlugin main;

    public SamplerCache(TerraPlugin main) {
        cache = new HashMap<>();
        this.main = main;
    }

    public Sampler get(World world, int x, int z) {
        return cache.computeIfAbsent(world.getSeed(), seed -> new Container(world, new LinkedHashMap<Long, Sampler>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Sampler> eldest) {
                return size() > main.getTerraConfig().getCheckCache();
            }
        })).get(x, z);
    }

    public Sampler getChunk(World world, int chunkX, int chunkZ) {
        return cache.computeIfAbsent(world.getSeed(), seed -> new Container(world, new LinkedHashMap<Long, Sampler>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Sampler> eldest) {
                return size() > main.getTerraConfig().getCheckCache();
            }
        })).getChunk(chunkX, chunkZ);
    }


    private class Container {
        private final World world;
        private final Map<Long, Sampler> cache;

        private Container(World world, Map<Long, Sampler> cache) {
            this.world = world;
            this.cache = cache;
        }

        public Sampler get(int x, int z) {
            int cx = FastMath.floorDiv(x, 16);
            int cz = FastMath.floorDiv(z, 16);
            return getChunk(cx, cz);
        }

        public Sampler getChunk(int cx, int cz) {
            long key = (((long) cx) << 32) | (cz & 0xffffffffL);
            TerraWorld tw = main.getWorld(world);
            return cache.computeIfAbsent(key, k -> new Sampler(cx, cz, tw.getGrid(), world, tw.getConfig().getTemplate().getBaseBlend(), tw.getConfig().getTemplate().getElevationBlend()));
        }
    }
}
