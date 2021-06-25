package com.dfsek.terra.fabric.mixin.implementations.chunk;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.fabric.block.FabricBlockData;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
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
    public abstract BlockState getBlockState(BlockPos pos);

    @Shadow
    @Nullable
    public abstract BlockState setBlockState(BlockPos pos, BlockState state, boolean moved);

    public int terra$getX() {
        return ((net.minecraft.world.chunk.Chunk) this).getPos().x;
    }

    public int terra$getZ() {
        return ((net.minecraft.world.chunk.Chunk) this).getPos().z;
    }

    public World terra$getWorld() {
        return (World) world;
    }

    public @NotNull BlockData terra$getBlock(int x, int y, int z) {
        return new FabricBlockData(getBlockState(new BlockPos(x, y, z)));
    }

    public void terra$setBlock(int x, int y, int z, BlockData data, boolean physics) {
        setBlockState(new BlockPos(x, y, z), ((FabricBlockData) data).getHandle(), false);
    }

    public void terra$setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        ((net.minecraft.world.chunk.Chunk) this).setBlockState(new BlockPos(x, y, z), ((FabricBlockData) blockData).getHandle(), false);
    }

    @Intrinsic
    public Object terra$getHandle() {
        return this;
    }
}
