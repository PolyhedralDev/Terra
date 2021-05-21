package com.dfsek.terra.fabric.mixin.access;

import net.minecraft.util.Identifier;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobSpawnerLogic.class)
public interface MobSpawnerLogicAccessor {
    @Invoker("getEntityId")
    Identifier callGetEntityId();
}
