package com.dfsek.terra.fabric.mixin.lifecycle;

import com.dfsek.terra.fabric.FabricEntryPoint;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;


@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>(Ljava/lang/Thread;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;" +
                     "Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/SaveProperties;" +
                     "Lnet/minecraft/resource/ResourcePackManager;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;" +
                     "Lnet/minecraft/resource/ServerResourceManager;Lcom/mojang/authlib/minecraft/MinecraftSessionService;" +
                     "Lcom/mojang/authlib/GameProfileRepository;Lnet/minecraft/util/UserCache;" +
                     "Lnet/minecraft/server/WorldGenerationProgressListenerFactory;)V",
            at = @At("RETURN"))
    private void injectConstructor(Thread serverThread, DynamicRegistryManager.Impl registryManager, LevelStorage.Session session,
                                   SaveProperties saveProperties, ResourcePackManager dataPackManager, Proxy proxy, DataFixer dataFixer,
                                   ServerResourceManager serverResourceManager, MinecraftSessionService sessionService,
                                   GameProfileRepository gameProfileRepo, UserCache userCache,
                                   WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        FabricEntryPoint.getPlatform().setServer((MinecraftServer) (Object) this);
    }
}
