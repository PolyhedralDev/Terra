package com.dfsek.terra.mod.mixin.lifecycle;

import com.dfsek.terra.mod.mixin_ifaces.FloraFeatureHolder;
import com.dfsek.terra.mod.mixin_ifaces.RegistrarInstance;

import net.minecraft.registry.Registerable;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(WorldPresets.Registrar.class)
public class WorldPresetsRegistrarMixin {
    @Inject(at = @At("RETURN"), method = "<init>")
    public void injectConstructor(Registerable<WorldPreset> presetRegisterable, CallbackInfo ci) {
        RegistrarInstance.INSTANCE = (WorldPresets.Registrar) (Object) this;
    }
}
