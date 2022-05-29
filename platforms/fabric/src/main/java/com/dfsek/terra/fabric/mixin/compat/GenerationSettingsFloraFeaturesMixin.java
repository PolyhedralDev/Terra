package com.dfsek.terra.fabric.mixin.compat;

import com.dfsek.terra.fabric.util.FloraFeatureHolder;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(GenerationSettings.class)
@Implements(@Interface(iface = FloraFeatureHolder.class, prefix = "terra$"))
public class GenerationSettingsFloraFeaturesMixin {
    private List<ConfiguredFeature<?, ?>> flora;
    
    public void terra$setFloraFeatures(List<ConfiguredFeature<?, ?>> features) {
        this.flora = features;
    }
    
    @Inject(method = "getFlowerFeatures", cancellable = true, at = @At("HEAD"))
    public void inject(CallbackInfoReturnable<List<ConfiguredFeature<?, ?>>> cir) {
        if(flora != null) {
            cir.setReturnValue(flora);
        }
    }
}
