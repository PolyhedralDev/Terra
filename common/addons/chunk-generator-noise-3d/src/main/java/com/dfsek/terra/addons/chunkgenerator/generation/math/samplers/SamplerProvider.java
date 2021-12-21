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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.util.MathUtil;
import com.dfsek.terra.api.util.generic.pair.Pair;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class SamplerProvider {
    private final LoadingCache<WorldContext, Sampler3D> cache;
    
    
    public SamplerProvider(Platform platform, BiomeProvider provider, int elevationSmooth) {
        cache = CacheBuilder.newBuilder().maximumSize(platform.getTerraConfig().getSamplerCache())
                            .build(new CacheLoader<>() {
                                @Override
                                public Sampler3D load(@NotNull WorldContext context) {
                                    return new Sampler3D(context.cx, context.cz, context.seed, context.minHeight, context.maxHeight, provider,
                                                         elevationSmooth);
                                }
                            });
    }
    
    public Sampler3D get(int x, int z, World world) {
        int cx = FastMath.floorDiv(x, 16);
        int cz = FastMath.floorDiv(z, 16);
        return getChunk(cx, cz, world);
    }
    
    public Sampler3D getChunk(int cx, int cz, World world) {
        return cache.getUnchecked(new WorldContext(cx, cz, world.getSeed(), world.getMinHeight(), world.getMaxHeight()));
    }
    
    private record WorldContext(int cx, int cz, long seed, int minHeight, int maxHeight) {
    }
}
