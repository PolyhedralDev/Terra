package com.dfsek.terra.fabric.mixin.access;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(MobSpawnerLogic.class)
public interface MobSpawnerLogicAccessor {
    @Invoker("getEntityId")
    Identifier callGetEntityId(World world, BlockPos blockPos);
}
