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

package com.dfsek.terra.fabric.generation;

import com.mojang.serialization.Codec;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;

import java.util.Objects;
import java.util.stream.Collectors;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.fabric.data.Codecs;
import com.dfsek.terra.fabric.util.ProtoPlatformBiome;


public class TerraBiomeSource extends BiomeSource {
    
    private final Registry<net.minecraft.world.biome.Biome> biomeRegistry;
    private final long seed;
    private ConfigPack pack;
    
    public TerraBiomeSource(Registry<net.minecraft.world.biome.Biome> biomes, long seed, ConfigPack pack) {
        super(biomes.streamEntries()
                    .filter(biome -> Objects.requireNonNull(biomes.getId(biome.value()))
                                            .getNamespace()
                                            .equals("terra")) // Filter out non-Terra biomes.
                    .collect(Collectors.toList()));
        this.biomeRegistry = biomes;
        this.seed = seed;
        this.pack = pack;
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return Codecs.TERRA_BIOME_SOURCE;
    }
    
    @Override
    public BiomeSource withSeed(long seed) {
        return new TerraBiomeSource(this.biomeRegistry, seed, pack);
    }
    
    @Override
    public RegistryEntry<net.minecraft.world.biome.Biome> getBiome(int biomeX, int biomeY, int biomeZ, MultiNoiseSampler noiseSampler) {
        return ((ProtoPlatformBiome) pack.getBiomeProvider().getBiome(biomeX << 2, biomeZ << 2, seed).getPlatformBiome()).getDelegate();
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
    
    public long getSeed() {
        return seed;
    }
}
