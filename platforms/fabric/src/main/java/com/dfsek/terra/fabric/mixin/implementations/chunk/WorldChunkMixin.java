package com.dfsek.terra.fabric.mixin.implementations.chunk;

import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.fabric.block.FabricBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldChunk.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class WorldChunkMixin {
    @Final
    @Shadow
    private net.minecraft.world.World world;

    @Shadow
    public abstract net.minecraft.block.BlockState getBlockState(BlockPos pos);

    @Shadow
    @Nullable
    public abstract net.minecraft.block.BlockState setBlockState(BlockPos pos, net.minecraft.block.BlockState state, boolean moved);

    public int terra$getX() {
        return ((net.minecraft.world.chunk.Chunk) this).getPos().x;
    }

    public int terra$getZ() {
        return ((net.minecraft.world.chunk.Chunk) this).getPos().z;
    }

    public World terra$getWorld() {
        return (World) world;
    }

    public @NotNull BlockState terra$getBlock(int x, int y, int z) {
        return new FabricBlockState(getBlockState(new BlockPos(x, y, z)));
    }

    public void terra$setBlock(int x, int y, int z, BlockState data, boolean physics) {
        setBlockState(new BlockPos(x, y, z), ((FabricBlockState) data).getHandle(), false);
    }

    public void terra$setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        ((net.minecraft.world.chunk.Chunk) this).setBlockState(new BlockPos(x, y, z), ((FabricBlockState) blockState).getHandle(), false);
    }

    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
}
