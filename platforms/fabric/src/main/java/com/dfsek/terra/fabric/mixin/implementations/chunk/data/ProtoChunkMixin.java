package com.dfsek.terra.fabric.mixin.implementations.chunk.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ProtoChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.generator.ChunkData;
import com.dfsek.terra.fabric.block.FabricBlockState;


@Mixin(ProtoChunk.class)
@Implements(@Interface(iface = ChunkData.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ProtoChunkMixin {
    @Shadow
    public abstract net.minecraft.block.BlockState getBlockState(BlockPos pos);
    
    public void terra$setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        ((net.minecraft.world.chunk.Chunk) this).setBlockState(new BlockPos(x, y, z), ((FabricBlockState) blockState).getHandle(), false);
    }
    
    public @NotNull BlockState terra$getBlock(int x, int y, int z) {
        return new FabricBlockState(getBlockState(new BlockPos(x, y, z)));
    }
    
    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
    
    public int terra$getMaxHeight() {
        return 255; // TODO: 1.17 - Implement dynamic height.
    }
}
