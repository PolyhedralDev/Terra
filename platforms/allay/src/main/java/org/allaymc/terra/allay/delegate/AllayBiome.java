package org.allaymc.terra.allay.delegate;

import org.allaymc.api.world.biome.BiomeType;

import com.dfsek.terra.api.world.biome.PlatformBiome;

/**
 * @author daoge_cmd
 */
public record AllayBiome(BiomeType allayBiome) implements PlatformBiome {
    @Override
    public BiomeType getHandle() {
        return allayBiome;
    }
}
