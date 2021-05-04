package com.dfsek.terra.fabric.mixin.implementations.chunk;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.fabric.block.FabricBlock;
import com.dfsek.terra.fabric.block.FabricBlockData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldChunk.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terra$"))
public abstract class WorldChunkMixin {
    @Final
    @Shadow
    private net.minecraft.world.World world;

    public int terra$getX() {
        return ((net.minecraft.world.chunk.Chunk) this).getPos().x;
    }

    public int terra$getZ() {
        return ((net.minecraft.world.chunk.Chunk) this).getPos().z;
    }

    public World terra$getWorld() {
        return (World) world;
    }

    public Block terra$getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + (terra$getX() << 4), y, z + (terra$getZ() << 4));
        return new FabricBlock(pos, world);
    }

    public @NotNull BlockData terra$getBlockData(int x, int y, int z) {
        return terra$getBlock(x, y, z).getBlockData();
    }

    public void terra$setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        ((net.minecraft.world.chunk.Chunk) this).setBlockState(new BlockPos(x, y, z), ((FabricBlockData) blockData).getHandle(), false);
    }

    public Object terra$getHandle() {
        return this;
    }
}
