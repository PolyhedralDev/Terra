package com.dfsek.terra.fabric.mixin.lifecycle;

import com.dfsek.terra.fabric.util.BiomeUtil;
import com.dfsek.terra.fabric.util.TagUtil;
import net.minecraft.server.DataPackContents;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DataPackContents.class)
public class DataPackContentsMixin {
    /*
     * #refresh populates all tags in the registries
     */
    @Inject(method = "refresh(Lnet/minecraft/util/registry/DynamicRegistryManager;)V", at = @At("RETURN"))
    private void injectReload(DynamicRegistryManager dynamicRegistryManager, CallbackInfo ci) {
        Registry<Biome> biomeRegistry = dynamicRegistryManager.get(Registry.BIOME_KEY);
        TagUtil.registerBiomeTags(biomeRegistry);
        BiomeUtil.registerFlora(biomeRegistry);
    }
}
