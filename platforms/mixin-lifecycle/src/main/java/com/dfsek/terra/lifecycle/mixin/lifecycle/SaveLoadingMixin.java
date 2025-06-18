package com.dfsek.terra.lifecycle.mixin.lifecycle;

import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.server.SaveLoading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.dfsek.terra.mod.util.MinecraftUtil;


@Mixin(SaveLoading.class)
public class SaveLoadingMixin {
    @ModifyArg(
        method = "load(Lnet/minecraft/server/SaveLoading$ServerConfig;Lnet/minecraft/server/SaveLoading$LoadContextSupplier;" +
                 "Lnet/minecraft/server/SaveLoading$SaveApplierFactory;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)" +
                 "Ljava/util/concurrent/CompletableFuture;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/DataPackContents;reload(Lnet/minecraft/resource/ResourceManager;" +
                     "Lnet/minecraft/registry/CombinedDynamicRegistries;Ljava/util/List;Lnet/minecraft/resource/featuretoggle/FeatureSet;" +
                     "Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;" +
                     "Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"),
        index = 1
    )
    private static CombinedDynamicRegistries<ServerDynamicRegistryType> grabManager(
        CombinedDynamicRegistries<ServerDynamicRegistryType> dynamicRegistries) {
        MinecraftUtil.registerFlora(dynamicRegistries.getCombinedRegistryManager().getOrThrow(RegistryKeys.BIOME));
        return dynamicRegistries;
    }
}
