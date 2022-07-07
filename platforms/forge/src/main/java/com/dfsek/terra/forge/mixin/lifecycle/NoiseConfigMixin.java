package com.dfsek.terra.forge.mixin.lifecycle;

import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseHypercube;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import com.dfsek.terra.mod.util.SeedHack;


/**
 * Hack to map noise sampler to seeds
 */
@Mixin(NoiseConfig.class)
public class NoiseConfigMixin {
    @Shadow
    @Final
    private long legacyWorldSeed;
    
    @Redirect(method = "<init>(Lnet/minecraft/world/gen/chunk/ChunkGeneratorSettings;Lnet/minecraft/util/registry/Registry;J)V",
              at = @At(value = "NEW",
                       target = "(Lnet/minecraft/world/gen/densityfunction/DensityFunction;" +
                                "Lnet/minecraft/world/gen/densityfunction/DensityFunction;" +
                                "Lnet/minecraft/world/gen/densityfunction/DensityFunction;" +
                                "Lnet/minecraft/world/gen/densityfunction/DensityFunction;" +
                                "Lnet/minecraft/world/gen/densityfunction/DensityFunction;" +
                                "Lnet/minecraft/world/gen/densityfunction/DensityFunction;Ljava/util/List;)" +
                                "Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$MultiNoiseSampler;"))
    private MultiNoiseSampler t(DensityFunction densityFunction, DensityFunction densityFunction2, DensityFunction densityFunction3,
                                DensityFunction densityFunction4, DensityFunction densityFunction5, DensityFunction densityFunction6,
                                List<NoiseHypercube> list) {
        MultiNoiseSampler sampler = new MultiNoiseSampler(densityFunction, densityFunction2, densityFunction3, densityFunction4,
                                                          densityFunction5, densityFunction6, list);
        SeedHack.register(sampler, legacyWorldSeed);
        return sampler;
    }
}
