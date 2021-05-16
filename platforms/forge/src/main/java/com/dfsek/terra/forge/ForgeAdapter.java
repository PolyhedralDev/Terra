package com.dfsek.terra.forge;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.state.Container;
import com.dfsek.terra.api.platform.block.state.MobSpawner;
import com.dfsek.terra.api.platform.block.state.Sign;
import com.dfsek.terra.forge.block.ForgeBlockData;
import com.dfsek.terra.forge.block.data.ForgeDirectional;
import com.dfsek.terra.forge.block.data.ForgeMultipleFacing;
import com.dfsek.terra.forge.block.data.ForgeOrientable;
import com.dfsek.terra.forge.block.data.ForgeRotatable;
import com.dfsek.terra.forge.block.data.ForgeSlab;
import com.dfsek.terra.forge.block.data.ForgeStairs;
import com.dfsek.terra.forge.block.data.ForgeWaterlogged;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.Arrays;

public final class ForgeAdapter {
    public static BlockPos adapt(Vector3 v) {
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }

    public static Vector3 adapt(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static ForgeBlockData adapt(BlockState state) {
        if(state.hasProperty(BlockStateProperties.STAIRS_SHAPE)) return new ForgeStairs(state);

        if(state.hasProperty(BlockStateProperties.SLAB_TYPE)) return new ForgeSlab(state);

        if(state.hasProperty(BlockStateProperties.AXIS)) return new ForgeOrientable(state, BlockStateProperties.AXIS);
        if(state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) return new ForgeOrientable(state, BlockStateProperties.HORIZONTAL_AXIS);

        if(state.hasProperty(BlockStateProperties.ROTATION_16)) return new ForgeRotatable(state);

        if(state.hasProperty(BlockStateProperties.FACING)) return new ForgeDirectional(state, BlockStateProperties.FACING);
        if(state.hasProperty(BlockStateProperties.FACING_HOPPER)) return new ForgeDirectional(state, BlockStateProperties.FACING_HOPPER);
        if(state.hasProperty(BlockStateProperties.HORIZONTAL_FACING))
            return new ForgeDirectional(state, BlockStateProperties.HORIZONTAL_FACING);

        if(state.getProperties().containsAll(Arrays.asList(BlockStateProperties.NORTH, BlockStateProperties.SOUTH, BlockStateProperties.EAST, BlockStateProperties.WEST)))
            return new ForgeMultipleFacing(state);
        if(state.hasProperty(BlockStateProperties.WATERLOGGED)) return new ForgeWaterlogged(state);
        return new ForgeBlockData(state);
    }

    public static com.dfsek.terra.api.platform.block.state.BlockState adapt(com.dfsek.terra.api.platform.block.Block block) {
        IWorld worldAccess = (IWorld) block.getLocation().getWorld();

        TileEntity entity = worldAccess.getBlockEntity(adapt(block.getLocation().toVector()));
        if(entity instanceof SignTileEntity) {
            return (Sign) entity;
        } else if(entity instanceof MobSpawnerTileEntity) {
            return (MobSpawner) entity;
        } else if(entity instanceof LockableLootTileEntity) {
            return (Container) entity;
        }
        return null;
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
}
