package com.dfsek.terra.fabric.mixin.world;

import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkGenerator.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.world.generator.ChunkGenerator.class, prefix = "vw$"))
public abstract class ChunkGeneratorMixin {
    public Object vw$getHandle() {
        return this;
    }
}
