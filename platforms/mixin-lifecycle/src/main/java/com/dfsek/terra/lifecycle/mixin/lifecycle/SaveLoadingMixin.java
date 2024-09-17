package com.dfsek.terra.lifecycle.mixin.lifecycle;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.SaveLoading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.dfsek.terra.mod.util.MinecraftUtil;


@Mixin(SaveLoading.class)
public class SaveLoadingMixin {
    @ModifyArg(
        method = "load(Lnet/minecraft/server/SaveLoading$ServerConfig;Lnet/minecraft/server/SaveLoading$LoadContextSupplier;Lnet/minecraft/server/SaveLoading$SaveApplierFactory;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/RegistryLoader;loadFromResource(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/DynamicRegistryManager;Ljava/util/List;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;"        ),
        index = 1
    )
    private static DynamicRegistryManager grabManager(DynamicRegistryManager registryManager) {
        MinecraftUtil.registerFlora(registryManager.get(RegistryKeys.BIOME));
        return registryManager;
    }
}
