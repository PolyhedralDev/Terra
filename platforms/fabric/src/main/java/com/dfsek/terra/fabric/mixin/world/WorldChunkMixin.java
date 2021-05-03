package com.dfsek.terra.fabric.mixin.world;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import com.dfsek.terra.fabric.world.handles.FabricWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldChunk.class)
@Implements(@Interface(iface = Chunk.class, prefix = "vw$"))
public abstract class WorldChunkMixin {
    @Final
    @Shadow
    private net.minecraft.world.World world;

    public int vw$getX() {
        return ((net.minecraft.world.chunk.Chunk) this).getPos().x;
    }

    public int vw$getZ() {
        return ((net.minecraft.world.chunk.Chunk) this).getPos().z;
    }

    public World vw$getWorld() {
        return new FabricWorld((ServerWorld) world, (ChunkGenerator) ((ServerWorld) world).getChunkManager().getChunkGenerator());
    }

    public Block vw$getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + (vw$getX() << 4), y, z + (vw$getZ() << 4));
        return new FabricBlock(pos, world);
    }

    public @NotNull BlockData vw$getBlockData(int x, int y, int z) {
        return vw$getBlock(x, y, z).getBlockData();
    }

    public void vw$setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        ((net.minecraft.world.chunk.Chunk) this).setBlockState(new BlockPos(x, y, z), ((FabricBlockData) blockData).getHandle(), false);
    }

    public Object vw$getHandle() {
        return this;
    }
}
