package com.dfsek.terra.fabric.mixin.lifecycle.server;

import com.dfsek.terra.fabric.TerraFabricPlugin;
import com.dfsek.terra.fabric.event.GameInitializationEvent;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class ServerMainMixin {
    @Inject(method = "main([Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DynamicRegistryManager;create()Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;"))
    private static void injectConstructor(String[] args, CallbackInfo ci) {
        TerraFabricPlugin.getInstance().getEventManager().callEvent(new GameInitializationEvent()); // Load during MinecraftServer construction, after other mods have registered blocks and stuff
    }
}
