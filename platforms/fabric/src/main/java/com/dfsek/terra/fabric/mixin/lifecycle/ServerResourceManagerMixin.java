package com.dfsek.terra.fabric.mixin.lifecycle;

import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.event.BiomeRegistrationEvent;


@Mixin(ServerResourceManager.class)
public class ServerResourceManagerMixin {
    @Inject(method = "reload(Ljava/util/List;Lnet/minecraft/util/registry/DynamicRegistryManager;" +
                     "Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;" +
                     "Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
            at = @At("HEAD"))
    private static void inject(List<ResourcePack> packs, DynamicRegistryManager registryManager,
                               CommandManager.RegistrationEnvironment commandEnvironment, int functionPermissionLevel,
                               Executor prepareExecutor,
                               Executor applyExecutor, CallbackInfoReturnable<CompletableFuture<ServerResourceManager>> cir) {
        FabricEntryPoint.getPlatform().getEventManager().callEvent(new BiomeRegistrationEvent(registryManager)); // register biomes
    }
}
