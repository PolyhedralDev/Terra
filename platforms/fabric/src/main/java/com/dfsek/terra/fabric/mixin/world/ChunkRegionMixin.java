package com.dfsek.terra.fabric.mixin.world;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldAccess;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkRegion.class)
@Implements(@Interface(iface = Chunk.class, prefix = "vw$"))
public abstract class ChunkRegionMixin {
    @Final
    @Shadow
    private int centerChunkX;

    @Final
    @Shadow
    private int centerChunkZ;

    public int vw$getX() {
        return centerChunkX;
    }

    public int vw$getZ() {
        return centerChunkZ;
    }

    public World vw$getWorld() {
        return new FabricWorldAccess((ChunkRegion) (Object) this);
    }

    public Block vw$getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + (centerChunkX << 4), y, z + (centerChunkZ << 4));
        return new FabricBlock(pos, (ChunkRegion) (Object) this);
    }

    public @NotNull BlockData vw$getBlockData(int x, int y, int z) {
        return vw$getBlock(x, y, z).getBlockData();
    }

    public void vw$setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        ((ChunkRegion) (Object) this).setBlockState(new BlockPos(x + (centerChunkX << 4), y, z + (centerChunkZ << 4)), ((FabricBlockData) blockData).getHandle(), 0);
    }

    public Object vw$getHandle() {
        return (ChunkRegion) (Object) this;
    }
}
