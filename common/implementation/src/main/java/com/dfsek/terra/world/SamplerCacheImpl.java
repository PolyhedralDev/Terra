package com.dfsek.terra.world;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.Sampler;


public class SamplerCacheImpl implements com.dfsek.terra.api.world.generator.SamplerCache {
    private final LoadingCache<Long, Sampler> cache;
    
    public SamplerCacheImpl(TerraPlugin main, World world) {
        cache = CacheBuilder.newBuilder().maximumSize(main.getTerraConfig().getSamplerCache())
                            .build(new CacheLoader<>() {
                                @Override
                                public Sampler load(@NotNull Long key) {
                                    int cx = (int) (key >> 32);
                                    int cz = (int) key.longValue();
                                    return world.getGenerator().createSampler(cx, cz, world.getBiomeProvider(), world,
                                                                              world.getConfig().elevationBlend());
                                }
                            });
    }
    
    @Override
    public Sampler get(int x, int z) {
        int cx = FastMath.floorDiv(x, 16);
        int cz = FastMath.floorDiv(z, 16);
        return getChunk(cx, cz);
    }
    
    @Override
    public void clear() {
        cache.invalidateAll();
        cache.cleanUp();
    }
    
    @Override
    public Sampler getChunk(int cx, int cz) {
        long key = MathUtil.squash(cx, cz);
        return cache.getUnchecked(key);
    }
}
