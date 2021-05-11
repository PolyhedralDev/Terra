package com.dfsek.terra.fabric.mixin.implementations.chunk;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.fabric.block.FabricBlock;
import com.dfsek.terra.fabric.block.FabricBlockData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ChunkRegionMixin {
    @Final
    @Shadow
    private int centerChunkX;

    @Final
    @Shadow
    private int centerChunkZ;

    public int terra$getX() {
        return centerChunkX;
    }

    public int terra$getZ() {
        return centerChunkZ;
    }

    public World terra$getWorld() {
        return (World) this;
    }

    public Block terra$getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + (centerChunkX << 4), y, z + (centerChunkZ << 4));
        return new FabricBlock(pos, (ChunkRegion) (Object) this);
    }

    public @NotNull BlockData terra$getBlockData(int x, int y, int z) {
        return terra$getBlock(x, y, z).getBlockData();
    }

    public void terra$setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        ((ChunkRegion) (Object) this).setBlockState(new BlockPos(x + (centerChunkX << 4), y, z + (centerChunkZ << 4)), ((FabricBlockData) blockData).getHandle(), 0);
    }

    // getHandle already added in world/ChunkRegionMixin.
}
