package com.dfsek.terra.bukkit.nms.v26_3;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.nms.NMSBiomeProvider;
import com.dfsek.terra.bukkit.nms.NMSChunkGeneratorDelegate;
import com.dfsek.terra.bukkit.nms.NMSVersionBindings;


public final class ChunkGenerator26_3 extends NMSChunkGeneratorDelegate {
    private final ChunkGenerator vanilla;

    public ChunkGenerator26_3(ChunkGenerator vanilla, ConfigPack pack, NMSBiomeProvider biomeProvider, long seed,
                             NMSVersionBindings bindings) {
        super(vanilla, pack, biomeProvider, seed, bindings);
        this.vanilla = vanilla;
    }

    @Override
    public CompletableFuture<ChunkAccess> buildTerrain(ChunkAccess chunk, Blender blender, RandomState randomState,
                                                       StructureManager structures, BiomeManager biomeManager,
                                                       WorldGenRegion region, Set<Holder<Biome>> biomes) {
        return vanilla.buildTerrain(chunk, blender, randomState, structures, biomeManager, region, biomes)
            .thenApply(result -> {
                applyStructureBeard(structures, result);
                return result;
            });
    }

    @Override
    public void addDebugScreenInfo(List<String> text, RandomState randomState, BlockPos pos, SamplerContext context) {
    }
}
