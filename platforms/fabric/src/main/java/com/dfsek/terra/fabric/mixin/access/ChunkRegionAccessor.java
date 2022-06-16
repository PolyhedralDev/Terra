package com.dfsek.terra.fabric.mixin.access;

import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;


@Mixin(ChunkRegion.class)
public interface ChunkRegionAccessor {
    @Accessor("chunks")
    List<Chunk> getChunks();
}
