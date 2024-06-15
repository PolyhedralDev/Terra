package org.allaymc.terra.allay.delegate;

import com.dfsek.terra.api.world.biome.PlatformBiome;
import org.allaymc.api.world.biome.BiomeType;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public record AllayBiome(BiomeType allayBiome) implements PlatformBiome {
    @Override
    public BiomeType getHandle() {
        return allayBiome;
    }
}
