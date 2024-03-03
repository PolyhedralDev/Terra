package com.dfsek.terra.mod.mixin.generalize;


import net.minecraft.block.entity.SignText;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.village.raid.RaidManager;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelStorage.Session;
import net.minecraft.world.spawner.SpecialSpawner;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Supplier;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager,
                               RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient,
                               boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess,
            maxChainedNeighborUpdates);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/entry/RegistryEntry;matchesKey(Lnet/minecraft/registry/RegistryKey;)Z"))
    public <T> boolean matchesKeyProxy(RegistryEntry<T> instance, RegistryKey<T> tRegistryKey) {
        if (tRegistryKey == DimensionTypes.THE_END) {
            return (this.getRegistryKey() == World.END);
        }
        return instance.matchesKey(tRegistryKey);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/raid/RaidManager;nameFor(Lnet/minecraft/registry/entry/RegistryEntry;)Ljava/lang/String;"))
    public String nameForProxy(RegistryEntry<DimensionType> dimensionTypeEntry) {
        RegistryEntry<DimensionType> entry = dimensionTypeEntry;
        if (this.getRegistryKey() == World.END) {
            entry = getRegistryManager().get(RegistryKeys.DIMENSION_TYPE).getEntry(DimensionTypes.THE_NETHER).orElseThrow();
        }
        return RaidManager.nameFor(entry);
    }
}
