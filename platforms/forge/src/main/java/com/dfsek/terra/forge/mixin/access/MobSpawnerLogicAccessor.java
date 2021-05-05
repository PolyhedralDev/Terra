package com.dfsek.terra.forge.mixin.access;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.spawner.AbstractSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSpawner.class)
public interface MobSpawnerLogicAccessor {
    @Invoker("getEntityId")
    ResourceLocation callGetEntityId();
}
