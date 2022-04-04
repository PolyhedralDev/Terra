package com.dfsek.terra.fabric.mixin.lifecycle;

import com.dfsek.terra.fabric.util.FabricUtil;

import net.minecraft.server.DataPackContents;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DataPackContents.class)
public class DataPackContentsMixin {
    @Shadow
    @Final
    private static Logger LOGGER;
    
    @Inject(method = "refresh(Lnet/minecraft/util/registry/DynamicRegistryManager;)V", at = @At("RETURN"))
    private void injectReload(DynamicRegistryManager dynamicRegistryManager, CallbackInfo ci) {
        LOGGER.info("Doing tag garbage....");
        FabricUtil.registerTags(dynamicRegistryManager.get(Registry.BIOME_KEY));
    }
}
