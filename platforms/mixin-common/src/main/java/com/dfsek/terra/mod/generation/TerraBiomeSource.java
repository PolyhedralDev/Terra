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

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.mod.config.ProtoPlatformBiome;
import com.dfsek.terra.mod.data.Codecs;
import com.dfsek.terra.mod.util.SeedHack;


public class TerraBiomeSource extends BiomeSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerraBiomeSource.class);
    private ConfigPack pack;

    public TerraBiomeSource(ConfigPack pack) {
        this.pack = pack;

        LOGGER.debug("Biomes: " + possibleBiomes());
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return Codecs.TERRA_BIOME_SOURCE;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return StreamSupport
            .stream(pack.getBiomeProvider()
                .getBiomes()
                .spliterator(), false)
            .map(b -> ((ProtoPlatformBiome) b.getPlatformBiome()).getDelegate());
    }

    @Override
    public Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ, Sampler Sampler) {
        return ((ProtoPlatformBiome) pack
            .getBiomeProvider()
            .getBiome(biomeX << 2, biomeY << 2, biomeZ << 2, SeedHack.getSeed(Sampler))
            .getPlatformBiome()).getDelegate();
    }

    public BiomeProvider getProvider() {
        return pack.getBiomeProvider();
    }

    public ConfigPack getPack() {
        return pack;
    }

    public void setPack(ConfigPack pack) {
        this.pack = pack;
    }
}
