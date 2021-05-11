package com.dfsek.terra.fabric.mixin.init;

import com.dfsek.terra.fabric.TerraFabricPlugin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/util/WindowProvider;createWindow(Lnet/minecraft/client/WindowSettings;Ljava/lang/String;Ljava/lang/String;)Lnet/minecraft/client/util/Window;", // sorta arbitrary position, after mod init, before window opens
            shift = At.Shift.BEFORE))
    public void injectConstructor(RunArgs args, CallbackInfo callbackInfo) {
        TerraFabricPlugin.getInstance().packInit(); // Load during MinecraftClient construction, after other mods have registered blocks and stuff
    }
}
