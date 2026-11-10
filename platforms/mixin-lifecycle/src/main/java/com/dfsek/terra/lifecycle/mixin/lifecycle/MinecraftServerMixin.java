package com.dfsek.terra.lifecycle.mixin.lifecycle;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dfsek.terra.lifecycle.LifecyclePlatform;


@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>(Ljava/lang/Thread;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;" +
                     "Lnet/minecraft/server/packs/repository/PackRepository;Lnet/minecraft/server/WorldStem;Ljava/util/Optional;" +
                     "Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/server/Services;" +
                     "Lnet/minecraft/server/level/progress/LevelLoadListener;ZLnet/minecraft/server/notifications/NotificationManager;)V",
            at = @At("RETURN"))
    private void injectConstructor(CallbackInfo ci) {
        LifecyclePlatform.setServer((MinecraftServer) (Object) this);
    }
}
