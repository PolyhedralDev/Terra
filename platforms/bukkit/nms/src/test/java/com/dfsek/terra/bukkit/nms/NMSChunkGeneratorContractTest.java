package com.dfsek.terra.bukkit.nms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;


class NMSChunkGeneratorContractTest {
    @Test
    void implementsPaperWorldGenerationHooks() throws ReflectiveOperationException {
        assertHook("fillFromNoise", CompletableFuture.class, Blender.class, RandomState.class, StructureManager.class, ChunkAccess.class);
        assertHook("buildSurface", void.class, WorldGenRegion.class, StructureManager.class, RandomState.class, ChunkAccess.class);
        assertHook("getBaseHeight", int.class, int.class, int.class, Heightmap.Types.class, LevelHeightAccessor.class, RandomState.class);
        assertHook("getBaseColumn", NoiseColumn.class, int.class, int.class, LevelHeightAccessor.class, RandomState.class);
        assertHook("applyBiomeDecoration", void.class, WorldGenLevel.class, ChunkAccess.class, StructureManager.class);
        assertHook("createStructures", void.class, RegistryAccess.class, ChunkGeneratorStructureState.class, StructureManager.class,
            ChunkAccess.class, StructureTemplateManager.class, ResourceKey.class);
        assertHook("spawnOriginalMobs", void.class, WorldGenRegion.class);
        assertHook("getMobsAt", WeightedList.class, Holder.class, StructureManager.class, MobCategory.class, BlockPos.class);
    }

    private static void assertHook(String name, Class<?> returnType, Class<?>... parameterTypes) throws ReflectiveOperationException {
        Method method = NMSChunkGeneratorDelegate.class.getDeclaredMethod(name, parameterTypes);
        assertEquals(returnType, method.getReturnType(), name + " return type changed");
    }
}
