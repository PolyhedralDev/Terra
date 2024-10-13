package com.dfsek.terra.lifecycle.mixin.lifecycle;


import com.dfsek.terra.api.Platform;

import com.dfsek.terra.mod.CommonPlatform;

import com.dfsek.terra.mod.ModPlatform;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.dfsek.terra.lifecycle.util.LifecycleUtil.initialized;


@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Inject(method = "onCloseScreen()V", at = @At("HEAD"))
    public void onClose(CallbackInfo ci) {
        ModPlatform platform = CommonPlatform.get();
        platform.getRawConfigRegistry().clear();
        initialized = false;
    }
}
