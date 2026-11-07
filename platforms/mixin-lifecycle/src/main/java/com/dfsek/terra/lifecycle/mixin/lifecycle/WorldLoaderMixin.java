package com.dfsek.terra.lifecycle.mixin.lifecycle;

import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.WorldLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.dfsek.terra.mod.util.MinecraftUtil;


@Mixin(WorldLoader.class)
public class WorldLoaderMixin {
    @ModifyArg(
        method = "load(Lnet/minecraft/server/WorldLoader$InitConfig;Lnet/minecraft/server/WorldLoader$WorldDataSupplier;Lnet/minecraft/server/WorldLoader$ResultFactory;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/ReloadableServerResources;loadResources(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/core/LayeredRegistryAccess;Ljava/util/List;Lnet/minecraft/world/flag/FeatureFlagSet;Lnet/minecraft/commands/Commands$CommandSelection;Lnet/minecraft/server/permissions/PermissionSet;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"),
        index = 1
    )
    private static LayeredRegistryAccess<RegistryLayer> grabManager(
        LayeredRegistryAccess<RegistryLayer> dynamicRegistries) {
        MinecraftUtil.registerFlora(dynamicRegistries.compositeAccess().lookupOrThrow(Registries.BIOME));
        return dynamicRegistries;
    }
}
