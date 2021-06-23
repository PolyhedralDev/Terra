package com.dfsek.terra.forge.mixin.implementations.chunk;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.forge.block.ForgeBlock;
import com.dfsek.terra.forge.block.ForgeBlockData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.world.chunk.Chunk.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ChunkMixin {

    @Shadow
    @Final
    private net.minecraft.world.World level;

    public int terra$getX() {
        return ((IChunk) this).getPos().x;
    }

    public int terra$getZ() {
        return ((IChunk) this).getPos().z;
    }

    public World terra$getWorld() {
        return (World) level;
    }

    public Block terra$getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + (terra$getX() << 4), y, z + (terra$getZ() << 4));
        return new ForgeBlock(pos, level);
    }

    public @NotNull BlockData terra$getBlockData(int x, int y, int z) {
        return terra$getBlock(x, y, z).getBlockData();
    }

    public void terra$setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        ((IChunk) this).setBlockState(new BlockPos(x, y, z), ((ForgeBlockData) blockData).getHandle(), false);
    }

    public Object terra$getHandle() {
        return this;
    }
}
