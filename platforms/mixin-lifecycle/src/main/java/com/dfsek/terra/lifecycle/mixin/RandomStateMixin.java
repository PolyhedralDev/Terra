package com.dfsek.terra.lifecycle.mixin;

import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.dfsek.terra.mod.util.SeedHack;


/**
 * Hack to map noise sampler to seeds
 */
@Mixin(RandomState.class)
public class RandomStateMixin {
    @Shadow
    @Final
    private Sampler sampler;

    @Inject(method = "<init>(Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;Lnet/minecraft/core/HolderGetter;J)V",
            at = @At("TAIL"))
    private void mapMultiNoise(NoiseGeneratorSettings chunkGeneratorSettings,
                               HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup, long seed,
                               CallbackInfo ci) {
        SeedHack.register(sampler, seed);
    }
}
