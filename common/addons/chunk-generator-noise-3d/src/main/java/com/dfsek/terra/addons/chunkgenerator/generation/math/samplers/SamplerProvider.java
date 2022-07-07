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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.jafama.FastMath;

import com.dfsek.terra.addons.chunkgenerator.config.noise.BiomeNoiseProperties;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class SamplerProvider {
    private final Cache<WorldContext, Sampler3D> cache;
    private final int elevationSmooth;
    private final PropertyKey<BiomeNoiseProperties> noisePropertiesKey;
    private final int maxBlend;
    
    public SamplerProvider(Platform platform, int elevationSmooth, PropertyKey<BiomeNoiseProperties> noisePropertiesKey, int maxBlend) {
        cache = Caffeine
                .newBuilder()
                .maximumSize(platform.getTerraConfig().getSamplerCache())
                .build();
        this.elevationSmooth = elevationSmooth;
        this.noisePropertiesKey = noisePropertiesKey;
        this.maxBlend = maxBlend;
    }
    
    public Sampler3D get(int x, int z, WorldProperties world, BiomeProvider provider) {
        int cx = FastMath.floorDiv(x, 16);
        int cz = FastMath.floorDiv(z, 16);
        return getChunk(cx, cz, world, provider);
    }
    
    public Sampler3D getChunk(int cx, int cz, WorldProperties world, BiomeProvider provider) {
        WorldContext context = new WorldContext(cx, cz, world.getSeed(), world.getMinHeight(), world.getMaxHeight());
        return cache.get(context, c -> new Sampler3D(c.cx, c.cz, c.seed, c.minHeight, c.maxHeight, provider,
                                                     elevationSmooth, noisePropertiesKey, maxBlend));
    }
    
    private record WorldContext(int cx, int cz, long seed, int minHeight, int maxHeight) {
    }
}
