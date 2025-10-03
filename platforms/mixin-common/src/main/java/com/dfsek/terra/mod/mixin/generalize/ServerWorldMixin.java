package com.dfsek.terra.mod.mixin.generalize;


import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkLoadProgress;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.SpecialSpawner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.concurrent.Executor;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    public ServerWorldMixin(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties,
                            RegistryKey<World> worldKey, DimensionOptions dimensionOptions,
                            ChunkLoadProgress chunkLoadProgress, boolean debugWorld, long seed,
                            List<SpecialSpawner> spawners, boolean shouldTickTime, @Nullable RandomSequencesState randomSequencesState) {
        super(properties, worldKey, server.getRegistryManager(), dimensionOptions.dimensionTypeEntry(), false, debugWorld, seed,
            server.getMaxChainedNeighborUpdates());
    }

    @Redirect(method = "<init>",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/registry/entry/RegistryEntry;matchesKey(Lnet/minecraft/registry/RegistryKey;)Z"))
    public <T> boolean matchesKeyProxy(RegistryEntry<T> instance, RegistryKey<T> tRegistryKey) {
        if(tRegistryKey == DimensionTypes.THE_END) {
            return (this.getRegistryKey() == World.END);
        }
        return instance.matchesKey(tRegistryKey);
    }
}
