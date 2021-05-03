package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.BlockType;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.fabric.inventory.FabricEnchantment;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import com.dfsek.terra.fabric.world.block.FabricBlockType;
import com.dfsek.terra.fabric.world.block.data.FabricDirectional;
import com.dfsek.terra.fabric.world.block.data.FabricMultipleFacing;
import com.dfsek.terra.fabric.world.block.data.FabricOrientable;
import com.dfsek.terra.fabric.world.block.data.FabricRotatable;
import com.dfsek.terra.fabric.world.block.data.FabricSlab;
import com.dfsek.terra.fabric.world.block.data.FabricStairs;
import com.dfsek.terra.fabric.world.block.data.FabricWaterlogged;
import com.dfsek.terra.fabric.world.entity.FabricEntityType;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldHandle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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

        if(state.contains(Properties.AXIS)) return new FabricOrientable(state, Properties.AXIS);
        if(state.contains(Properties.HORIZONTAL_AXIS)) return new FabricOrientable(state, Properties.HORIZONTAL_AXIS);

        if(state.contains(Properties.ROTATION)) return new FabricRotatable(state);

        if(state.contains(Properties.FACING)) return new FabricDirectional(state, Properties.FACING);
        if(state.contains(Properties.HOPPER_FACING)) return new FabricDirectional(state, Properties.HOPPER_FACING);
        if(state.contains(Properties.HORIZONTAL_FACING)) return new FabricDirectional(state, Properties.HORIZONTAL_FACING);

        if(state.getProperties().containsAll(Arrays.asList(Properties.NORTH, Properties.SOUTH, Properties.EAST, Properties.WEST)))
            return new FabricMultipleFacing(state);
        if(state.contains(Properties.WATERLOGGED)) return new FabricWaterlogged(state);
        return new FabricBlockData(state);
    }

    public static Direction adapt(BlockFace face) {
        switch(face) {
            case NORTH:
                return Direction.NORTH;
            case WEST:
                return Direction.WEST;
            case SOUTH:
                return Direction.SOUTH;
            case EAST:
                return Direction.EAST;
            case UP:
                return Direction.UP;
            case DOWN:
                return Direction.DOWN;
            default:
                throw new IllegalArgumentException("Illegal direction: " + face);
        }
    }

    public static BlockType adapt(Block block) {
        return new FabricBlockType(block);
    }

    public static EntityType adapt(net.minecraft.entity.EntityType<?> entityType) {
        return new FabricEntityType(entityType);
    }

    public static net.minecraft.entity.EntityType<? extends Entity> adapt(EntityType entityType) {
        return ((FabricEntityType) entityType).getHandle();
    }

    public static Enchantment adapt(net.minecraft.enchantment.Enchantment enchantment) {
        return new FabricEnchantment(enchantment);
    }

    public static net.minecraft.enchantment.Enchantment adapt(Enchantment enchantment) {
        return ((FabricEnchantment) enchantment).getHandle();
    }

    public WorldAccess adapt(FabricWorldHandle worldHandle) {
        return worldHandle.getWorld();
    }
}
