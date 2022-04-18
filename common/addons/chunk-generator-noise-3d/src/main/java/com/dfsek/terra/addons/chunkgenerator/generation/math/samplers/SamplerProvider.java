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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.jafama.FastMath;

import java.util.concurrent.ExecutionException;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class SamplerProvider {
    private final Cache<WorldContext, Sampler3D> cache;
    private final int elevationSmooth;
    
    public SamplerProvider(Platform platform, int elevationSmooth) {
        this.elevationSmooth = elevationSmooth;
        cache = CacheBuilder.newBuilder().maximumSize(platform.getTerraConfig().getSamplerCache()).build();
    }
    
    public Sampler3D get(int x, int z, WorldProperties world, BiomeProvider provider) {
        int cx = FastMath.floorDiv(x, 16);
        int cz = FastMath.floorDiv(z, 16);
        return getChunk(cx, cz, world, provider);
    }
    
    public Sampler3D getChunk(int cx, int cz, WorldProperties world, BiomeProvider provider) {
        WorldContext context = new WorldContext(cx, cz, world.getSeed(), world.getMinHeight(), world.getMaxHeight());
        try {
            return cache.get(context,
                             () -> new Sampler3D(context.cx, context.cz, context.seed, context.minHeight, context.maxHeight, provider,
                                                 elevationSmooth));
        } catch(ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    
    private record WorldContext(int cx, int cz, long seed, int minHeight, int maxHeight) {
    }
}
