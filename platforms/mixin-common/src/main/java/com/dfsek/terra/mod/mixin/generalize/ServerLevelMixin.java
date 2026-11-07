package com.dfsek.terra.mod.mixin.generalize;


import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.LevelLoadListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.concurrent.Executor;


@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    public ServerLevelMixin(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties,
                            ResourceKey<Level> worldKey, LevelStem dimensionOptions,
                            LevelLoadListener chunkLoadProgress, boolean debugWorld, long seed,
                            List<CustomSpawner> spawners, boolean shouldTickTime, @Nullable RandomSequences randomSequencesState) {
        super(properties, worldKey, server.registryAccess(), dimensionOptions.type(), false, debugWorld, seed,
            server.getMaxChainedNeighborUpdates());
    }

    @Redirect(method = "<init>",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/level/dimension/DimensionType;hasEnderDragonFight()Z"))
    public boolean hasEnderDragonFightProxy(DimensionType instance) {
        return this.dimension() == Level.END;
    }
}
