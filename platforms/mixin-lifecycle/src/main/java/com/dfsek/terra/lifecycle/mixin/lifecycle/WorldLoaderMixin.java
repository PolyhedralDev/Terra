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
        // The innermost thenComposeAsync callback of WorldLoader.load, which builds the server resources.
        method = "lambda$load$2(Lcom/mojang/datafixers/util/Pair;Ljava/util/List;Lnet/minecraft/server/WorldLoader$WorldDataSupplier;" +
                 "Lnet/minecraft/server/packs/resources/CloseableResourceManager;Lnet/minecraft/core/LayeredRegistryAccess;" +
                 "Lnet/minecraft/core/RegistryAccess$Frozen;Ljava/util/List;Lnet/minecraft/server/WorldLoader$InitConfig;" +
                 "Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lnet/minecraft/server/WorldLoader$ResultFactory;" +
                 "Lnet/minecraft/core/RegistryAccess$Frozen;)Ljava/util/concurrent/CompletionStage;",
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
