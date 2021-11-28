/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.world;

import com.dfsek.terra.api.world.generator.SamplerCache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.util.math.Sampler;
import com.dfsek.terra.api.world.access.World;


public class SamplerCacheImpl implements SamplerCache {
    private final LoadingCache<Long, Sampler> cache;
    
    public SamplerCacheImpl(Platform platform, World world) {
        cache = CacheBuilder.newBuilder().maximumSize(platform.getTerraConfig().getSamplerCache())
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
