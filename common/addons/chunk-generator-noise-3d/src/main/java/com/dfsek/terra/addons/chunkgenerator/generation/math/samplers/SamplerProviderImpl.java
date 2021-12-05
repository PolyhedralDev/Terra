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

package com.dfsek.terra.addons.chunkgenerator.generation.math.samplers;

import com.dfsek.terra.api.world.World;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class SamplerProviderImpl {
    private final LoadingCache<Pair<Long, World>, Sampler> cache;
    
    
    
    public SamplerProviderImpl(Platform platform, BiomeProvider provider, int elevationSmooth) {
        cache = CacheBuilder.newBuilder().maximumSize(platform.getTerraConfig().getSamplerCache())
                            .build(new CacheLoader<>() {
                                @Override
                                public Sampler load(@NotNull Pair<Long, World> pair) {
                                    long key = pair.getLeft();
                                    int cx = (int) (key >> 32);
                                    int cz = (int) key;
                                    World world = pair.getRight();
                                    return new Sampler3D(cx, cz, world.getSeed(), world.getMinHeight(), world.getMaxHeight(), provider, elevationSmooth);
                                }
                            });
    }
    
    public Sampler get(int x, int z, World world) {
        int cx = FastMath.floorDiv(x, 16);
        int cz = FastMath.floorDiv(z, 16);
        return getChunk(cx, cz, world);
    }
    
    public Sampler getChunk(int cx, int cz, World world) {
        long key = MathUtil.squash(cx, cz);
        return cache.getUnchecked(Pair.of(key, world));
    }
}
