package com.dfsek.terra.forge.mixin.lifecycle;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler.NoiseParameters;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseHypercube;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import com.dfsek.terra.mod.util.SeedHack;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Hack to map noise sampler to seeds
 */
@Mixin(NoiseConfig.class)
public class NoiseConfigMixin {
    @Shadow
    @Final
    private MultiNoiseSampler multiNoiseSampler;
    
    @Inject(method = "<init>(Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;Lnet/minecraft/registry/RegistryEntryLookup;J)V",
            at = @At("TAIL"))
    private void mapMultiNoise(ChunkGeneratorSettings chunkGeneratorSettings, RegistryEntryLookup<NoiseParameters> noiseParametersLookup, long seed,
                               CallbackInfo ci) {
        SeedHack.register(multiNoiseSampler, seed);
    }
}
