package com.dfsek.terra.generation.math;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import net.jafama.FastMath;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SamplerCache {
    private final Map<Long, Container> containerMap;
    private final TerraPlugin main;

    public SamplerCache(TerraPlugin main) {
        containerMap = new HashMap<>();
        this.main = main;
    }

    public Sampler get(World world, int x, int z) {
        return containerMap.computeIfAbsent(world.getSeed(), seed -> new Container(world)).get(x, z);
    }

    public Sampler getChunk(World world, int chunkX, int chunkZ) {
        return containerMap.computeIfAbsent(world.getSeed(), seed -> new Container(world)).getChunk(chunkX, chunkZ);
    }

    public void clear() {
        containerMap.clear();
    }

    private class Container {
        private final World world;
        private final TerraWorld terraWorld;
        private final Map<Long, Sampler> cache = Collections.synchronizedMap(new LinkedHashMap<Long, Sampler>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Sampler> eldest) {
                return this.size() > main.getTerraConfig().getSamplerCache();
            }
        });

        private Container(World world) {
            this.world = world;
            terraWorld = main.getWorld(world);
        }

        public Sampler get(int x, int z) {
            int cx = FastMath.floorDiv(x, 16);
            int cz = FastMath.floorDiv(z, 16);
            return getChunk(cx, cz);
        }

        public Sampler getChunk(int cx, int cz) {
            long key = MathUtil.squash(cx, cz);
            synchronized(cache) {
                return cache.computeIfAbsent(key, k -> new Sampler(cx, cz, terraWorld.getGrid(), world, terraWorld.getConfig().getTemplate().getBaseBlend(), terraWorld.getConfig().getTemplate().getElevationBlend()));
            }
        }
    }
}
