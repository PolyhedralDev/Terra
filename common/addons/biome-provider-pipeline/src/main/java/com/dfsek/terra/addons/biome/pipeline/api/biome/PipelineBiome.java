package com.dfsek.terra.addons.biome.pipeline.api.biome;

import java.util.Set;

import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.tag.BiomeTagIndex;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public abstract sealed class PipelineBiome implements StringIdentifiable
    permits DelegatedPipelineBiome, PlaceholderPipelineBiome, SelfPipelineBiome {
    private final int intId;

    PipelineBiome(int intId) {
        this.intId = intId;
    }

    /**
     * Registers a new ID per call; call before {@code index} is sealed.
     */
    @ApiStatus.Internal
    public static PipelineBiome placeholder(String id, BiomeTagIndex index) {
        return new PlaceholderPipelineBiome(id, index);
    }

    /**
     * {@code biome} must come from the same pack as the index it is tested against.
     */
    public static PipelineBiome from(Biome biome) {
        return new DelegatedPipelineBiome(biome);
    }

    public static PipelineBiome self() {
        return SelfPipelineBiome.INSTANCE;
    }

    /**
     * Dense per pack, not stable across loads.
     */
    @Contract(pure = true)
    public final int getIntID() {
        return intId;
    }

    @NotNull
    public abstract Biome getBiome();

    @NotNull
    public abstract Set<String> getTags();

    @Contract(pure = true)
    public boolean isPlaceholder() {
        return false;
    }

    @Contract(pure = true)
    public boolean isSelf() {
        return false;
    }


}