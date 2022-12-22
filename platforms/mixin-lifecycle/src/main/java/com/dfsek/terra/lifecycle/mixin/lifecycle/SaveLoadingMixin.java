package com.dfsek.terra.lifecycle.mixin.lifecycle;

import com.dfsek.terra.lifecycle.util.LifecycleUtil;

import com.dfsek.terra.mod.util.MinecraftUtil;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.SaveLoading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


@Mixin(SaveLoading.class)
public class SaveLoadingMixin {
    @ModifyArg(
            method = "method_42097(Lnet/minecraft/registry/DynamicRegistryManager$Immutable;" +
                     "Lnet/minecraft/server/SaveLoading$SaveApplierFactory;Lnet/minecraft/resource/LifecycledResourceManager;" +
                     "Lnet/minecraft/registry/CombinedDynamicRegistries;Lnet/minecraft/server/SaveLoading$LoadContext;" +
                     "Lnet/minecraft/server/DataPackContents;)Ljava/lang/Object;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/DataPackContents;refresh(Lnet/minecraft/registry/DynamicRegistryManager;)V"
            ),
            index = 0
    )
    private static DynamicRegistryManager grabManager(DynamicRegistryManager in) {
        MinecraftUtil.registerFlora(in.get(RegistryKeys.BIOME));
        return in;
    }
}
