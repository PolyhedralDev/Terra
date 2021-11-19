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
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.RegistryLookupCodec;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.Objects;
import java.util.stream.Collectors;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.util.FabricUtil;


public class TerraBiomeSource extends BiomeSource {
    public static final Codec<ConfigPack> PACK_CODEC = (RecordCodecBuilder.create(config -> config.group(
                                                                                                          Codec.STRING.fieldOf("pack").forGetter(ConfigPack::getID)
                                                                                                        )
                                                                                                  .apply(config, config.stable(
                                                                                                          FabricEntryPoint.getPlatform()
                                                                                                                          .getConfigRegistry()::get))));
    public static final Codec<TerraBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                                                                                                              RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry),
                                                                                                              Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed),
                                                                                                              PACK_CODEC.fieldOf("pack").stable().forGetter(source -> source.pack))
                                                                                                      .apply(instance, instance.stable(
                                                                                                              TerraBiomeSource::new)));
    
    private final Registry<Biome> biomeRegistry;
    private final long seed;
    private ConfigPack pack;
    
    public TerraBiomeSource(Registry<Biome> biomes, long seed, ConfigPack pack) {
        super(biomes.stream()
                    .filter(biome -> Objects.requireNonNull(biomes.getId(biome))
                                            .getNamespace()
                                            .equals("terra")) // Filter out non-Terra biomes.
                    .collect(Collectors.toList()));
        this.biomeRegistry = biomes;
        this.seed = seed;
        this.pack = pack;
    }
    
    public void setPack(ConfigPack pack) {
        this.pack = pack;
    }
    
    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }
    
    @Override
    public BiomeSource withSeed(long seed) {
        return new TerraBiomeSource(this.biomeRegistry, seed, pack);
    }
    
    @Override
    public Biome getBiomeForNoiseGen(int biomeX, int biomeY, int biomeZ) {
        TerraBiome biome = pack.getBiomeProviderBuilder().getBiome(biomeX << 2, biomeZ << 2, seed);
        return biomeRegistry.get(new Identifier("terra", FabricUtil.createBiomeID(pack, biome.getID())));
    }
    
    public BiomeProvider getProvider() {
        return pack.getBiomeProviderBuilder();
    }
}
