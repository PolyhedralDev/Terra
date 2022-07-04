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

package com.dfsek.terra.mod.generation;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.StreamSupport;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.data.Codecs;
import com.dfsek.terra.mod.util.SeedHack;


public class TerraBiomeSource extends BiomeSource {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TerraBiomeSource.class);
    private final Registry<net.minecraft.world.biome.Biome> biomeRegistry;
    private ConfigPack pack;
    
    public TerraBiomeSource(Registry<net.minecraft.world.biome.Biome> biomes, ConfigPack pack) {
        super(StreamSupport
                      .stream(pack.getBiomeProvider()
                                  .getBiomes()
                                  .spliterator(), false)
                      .map(b -> biomes.getOrCreateEntry(((ProtoPlatformBiome) b.getPlatformBiome()).getDelegate())));
        this.biomeRegistry = biomes;
        this.pack = pack;
        
        LOGGER.debug("Biomes: " + getBiomes());
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return Codecs.TERRA_BIOME_SOURCE;
    }
    
    @Override
    public RegistryEntry<net.minecraft.world.biome.Biome> getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseSampler noiseSampler) {
        return biomeRegistry
                .entryOf(((ProtoPlatformBiome) pack
                                 .getBiomeProvider()
                                 .getBiome(biomeX << 2, biomeY << 2, biomeZ << 2, SeedHack.getSeed(noiseSampler))
                                 .getPlatformBiome()).getDelegate()
                        );
    }
    
    public BiomeProvider getProvider() {
        return pack.getBiomeProvider();
    }
    
    public Registry<net.minecraft.world.biome.Biome> getBiomeRegistry() {
        return biomeRegistry;
    }
    
    public ConfigPack getPack() {
        return pack;
    }
    
    public void setPack(ConfigPack pack) {
        this.pack = pack;
    }
}
