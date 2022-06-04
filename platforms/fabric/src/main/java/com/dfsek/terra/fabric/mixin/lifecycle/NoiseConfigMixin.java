package com.dfsek.terra.fabric.mixin.lifecycle;

import com.dfsek.terra.fabric.util.SeedHack;

import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Hack to map noise sampler to seeds
 */
@Mixin(NoiseConfig.class)
public class NoiseConfigMixin {
    @Shadow
    @Final
    private MultiNoiseSampler multiNoiseSampler;
    
    @Inject(method = "<init>(Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;Lnet/minecraft/util/registry/Registry;J)V", at =  @At("TAIL"))
    private void mapMultiNoise(ChunkGeneratorSettings chunkGeneratorSettings, Registry<DoublePerlinNoiseSampler.NoiseParameters> noiseRegistry, long seed, CallbackInfo ci) {
        SeedHack.register(multiNoiseSampler, seed);
    }
}
