package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import com.dfsek.terra.fabric.world.block.FabricBlockType;
import com.dfsek.terra.fabric.world.block.data.FabricMultipleFacing;
import com.dfsek.terra.fabric.world.block.data.FabricOrientable;
import com.dfsek.terra.fabric.world.block.data.FabricSlab;
import com.dfsek.terra.fabric.world.block.data.FabricStairs;
import com.dfsek.terra.fabric.world.block.data.FabricWaterlogged;
import com.dfsek.terra.fabric.world.entity.FabricEntityType;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldHandle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.Arrays;

public final class FabricAdapter {
    public static BlockPos adapt(Vector3 v) {
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }

    public static Vector3 adapt(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static FabricBlockData adapt(BlockState state) {
        if(state.contains(Properties.STAIR_SHAPE)) return new FabricStairs(state);

        if(state.contains(Properties.SLAB_TYPE)) return new FabricSlab(state);

        if(state.contains(Properties.AXIS)) return new FabricOrientable(state);

        if(state.getProperties().containsAll(Arrays.asList(Properties.NORTH, Properties.SOUTH, Properties.EAST, Properties.WEST)))
            return new FabricMultipleFacing(state);
        if(state.contains(Properties.WATERLOGGED)) return new FabricWaterlogged(state);
        return new FabricBlockData(state);
    }

    public static BlockType adapt(Block block) {
        return new FabricBlockType(block);
    }

    public WorldAccess adapt(FabricWorldHandle worldHandle) {
        return worldHandle.getWorld();
    }

    public static EntityType adapt(net.minecraft.entity.EntityType<?> entityType) {
        return new FabricEntityType(entityType);
    }

    public static net.minecraft.entity.EntityType<?> adapt(EntityType entityType) {
        return ((FabricEntityType) entityType).getHandle();
    }

}
