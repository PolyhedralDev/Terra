package com.dfsek.terra.forge.mixin.implementations.chunk;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.forge.block.ForgeBlock;
import com.dfsek.terra.forge.block.ForgeBlockData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.WorldGenRegion;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldGenRegion.class)
@Implements(@Interface(iface = Chunk.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class WorldGenRegionMixin {
    @Final
    @Shadow
    private int x;

    @Final
    @Shadow
    private int z;

    public int terra$getX() {
        return x;
    }

    public int terra$getZ() {
        return z;
    }

    public World terra$getWorld() {
        return (World) this;
    }

    public Block terra$getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + (this.x << 4), y, z + (this.z << 4));
        return new ForgeBlock(pos, (WorldGenRegion) (Object) this);
    }

    public @NotNull BlockData terra$getBlockData(int x, int y, int z) {
        return terra$getBlock(x, y, z).getBlockData();
    }

    public void terra$setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        ((WorldGenRegion) (Object) this).setBlock(new BlockPos(x + (this.x << 4), y, z + (this.z << 4)), ((ForgeBlockData) blockData).getHandle(), 0);
    }

    public Object terra$getHandle() {
        return this;
    }
}
