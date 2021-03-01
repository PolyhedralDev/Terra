package com.dfsek.terra.world.generation.math;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.math.samplers.Sampler;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SamplerCache {
    private final LoadingCache<Long, Sampler> cache;

    public SamplerCache(TerraPlugin main, TerraWorld world) {
        cache = CacheBuilder.newBuilder().maximumSize(main.getTerraConfig().getSamplerCache())
                .build(new CacheLoader<Long, Sampler>() {
                    @Override
                    public Sampler load(@NotNull Long key) {
                        int cx = (int) (key >> 32);
                        int cz = (int) key.longValue();
                        return world.getGenerator().createSampler(cx, cz, world.getBiomeProvider(), world.getWorld(), world.getConfig().getTemplate().getElevationBlend());
                    }
                });
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
