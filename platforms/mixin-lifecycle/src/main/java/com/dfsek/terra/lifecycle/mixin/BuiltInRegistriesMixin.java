package com.dfsek.terra.lifecycle.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dfsek.terra.lifecycle.util.RegistryUtil;


@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void registerTerraGenerators(CallbackInfo ci) {
        RegistryUtil.register();
    }
}