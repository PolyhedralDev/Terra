package com.dfsek.terra.mod.mixin.gameplay;


import com.dfsek.terra.mod.util.MinecraftAdapter;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.dfsek.terra.mod.util.FertilizableUtil;

import java.util.random.RandomGenerator;


@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    private static final Identifier cooldownId = new Identifier("terra", "random_cooldown");
    
    @Redirect(method = "tickChunk",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/block/BlockState;randomTick(Lnet/minecraft/server/world/ServerWorld;" +
                                "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V"))
    public void injectTickChunk(BlockState instance, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        Boolean value = FertilizableUtil.grow(serverWorld, MinecraftAdapter.adapt(random), blockPos, instance, cooldownId);
        if(value != null) {
            if(!value) {
                instance.randomTick(serverWorld, blockPos, random);
            }
        }
    }
}
