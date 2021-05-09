package com.dfsek.terra.fabric.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    @Final
    protected SaveProperties saveProperties;


    @Inject(method = "createWorlds(Lnet/minecraft/server/WorldGenerationProgressListener;)V", at = @At("HEAD"))
    public void injectCreateWorlds(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        System.out.println("Generator: " + this.saveProperties.getGeneratorOptions());
        Thread.dumpStack();
    }
}
