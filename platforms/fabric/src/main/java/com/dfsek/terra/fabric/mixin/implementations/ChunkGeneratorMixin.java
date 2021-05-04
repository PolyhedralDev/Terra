package com.dfsek.terra.fabric.mixin.implementations;

import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkGenerator.class)
@Implements(@Interface(iface = com.dfsek.terra.api.platform.world.generator.ChunkGenerator.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ChunkGeneratorMixin {
    public Object terra$getHandle() {
        return this;
    }
}
