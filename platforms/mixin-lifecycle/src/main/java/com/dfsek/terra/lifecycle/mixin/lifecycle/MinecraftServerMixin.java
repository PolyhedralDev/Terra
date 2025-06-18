package com.dfsek.terra.lifecycle.mixin.lifecycle;

import com.mojang.datafixers.DataFixer;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.ApiServices;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

import com.dfsek.terra.lifecycle.LifecyclePlatform;


@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>(Ljava/lang/Thread;Lnet/minecraft/world/level/storage/LevelStorage$Session;" +
                     "Lnet/minecraft/resource/ResourcePackManager;Lnet/minecraft/server/SaveLoader;Ljava/net/Proxy;" +
                     "Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/util/ApiServices;" +
                     "Lnet/minecraft/server/WorldGenerationProgressListenerFactory;)V",
            at = @At("RETURN"))
    private void injectConstructor(Thread serverThread, LevelStorage.Session session, ResourcePackManager dataPackManager,
                                   SaveLoader saveLoader, Proxy proxy, DataFixer dataFixer, ApiServices apiServices,
                                   WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        LifecyclePlatform.setServer((MinecraftServer) (Object) this);
    }
}
