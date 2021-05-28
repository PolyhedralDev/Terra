package com.dfsek.terra.forge.mixin.init;

import com.dfsek.terra.forge.TerraForgePlugin;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MinecraftServerMixin {
    @Inject(method = "main([Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DynamicRegistries;builtin()Lnet/minecraft/util/registry/DynamicRegistries$Impl;"))
    private static void injectConstructor(String[] args, CallbackInfo ci) {
        TerraForgePlugin.getInstance().init(); // Load during MinecraftServer construction, after other mods have registered blocks and stuff
    }
}
