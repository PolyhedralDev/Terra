package com.dfsek.terra.lifecycle.mixin;

import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.dfsek.terra.mod.util.SeedHack;


/**
 * Hack to map noise sampler to seeds
 */
@Mixin(RandomState.class)
public class RandomStateMixin {
    @Shadow
    @Final
    private long seed;

    @Inject(method = "createClimateSampler(Lnet/minecraft/world/level/levelgen/densityfunction/SamplerContext;)Lnet/minecraft/world/level/biome/Climate$Sampler;",
            at = @At("RETURN"))
    private void mapMultiNoise(SamplerContext context, CallbackInfoReturnable<Sampler> cir) {
        SeedHack.register(cir.getReturnValue(), seed);
    }
}
