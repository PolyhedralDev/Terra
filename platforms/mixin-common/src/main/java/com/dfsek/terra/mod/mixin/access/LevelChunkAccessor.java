package com.dfsek.terra.mod.mixin.access;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(LevelChunk.class)
public interface LevelChunkAccessor {
    @Invoker("promotePendingBlockEntity")
    public BlockEntity invokeLoadBlockEntity(BlockPos pos, CompoundTag nbt);
}
