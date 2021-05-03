package com.dfsek.terra.fabric.mixin.world;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ProtoChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ProtoChunk.class)
@Implements(@Interface(iface = ChunkData.class, prefix = "terra$"))
public abstract class ProtoChunkMixin {
    @Shadow
    public abstract BlockState getBlockState(BlockPos pos);

    public @NotNull BlockData terra$getBlockData(int x, int y, int z) {
        return new FabricBlockData(getBlockState(new BlockPos(x, y, z)));
    }

    public void terra$setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        ((net.minecraft.world.chunk.Chunk) this).setBlockState(new BlockPos(x, y, z), ((FabricBlockData) blockData).getHandle(), false);
    }

    public Object terra$getHandle() {
        return this;
    }

    public int terra$getMaxHeight() {
        return 255; // TODO: 1.17 - Implement dynamic height.
    }
}
