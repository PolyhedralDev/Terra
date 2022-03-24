package com.dfsek.terra.fabric.mixin.lifecycle;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.DynamicRegistryManager.Immutable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.event.BiomeRegistrationEvent;


@Mixin(MinecraftServer.class)
public abstract class MinecraftServer_BiomeRegistrationMixin {
    @Shadow
    public abstract Immutable getRegistryManager();
    
    @Inject(method = "reloadResources",
            at = @At("HEAD"))
    private void inject(Collection<String> dataPacks, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        FabricEntryPoint.getPlatform().getEventManager().callEvent(new BiomeRegistrationEvent(getRegistryManager())); // register biomes
    }
}
