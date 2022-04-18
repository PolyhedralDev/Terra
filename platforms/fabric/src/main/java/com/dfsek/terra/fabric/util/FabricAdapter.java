/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.fabric.util;

import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.WallShape;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.HeightLimitView;

import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.block.state.properties.enums.Half;
import com.dfsek.terra.api.block.state.properties.enums.RailShape;
import com.dfsek.terra.api.block.state.properties.enums.RedstoneConnection;
import com.dfsek.terra.api.block.state.properties.enums.WallHeight;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.info.WorldProperties;


public final class FabricAdapter {
    public static BlockPos adapt(Vector3 v) {
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }
    
    public static Vector3 adapt(BlockPos pos) {
        return Vector3.of(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public static Direction adapt(com.dfsek.terra.api.block.state.properties.enums.Direction direction) {
        return switch(direction) {
            case SOUTH -> Direction.SOUTH;
            case NORTH -> Direction.NORTH;
            case WEST -> Direction.WEST;
            case EAST -> Direction.EAST;
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
        };
    }
    
    public static WorldProperties adapt(HeightLimitView height, long seed) {
        return new WorldProperties() {
            @Override
            public long getSeed() {
                return seed;
            }
            
            @Override
            public int getMaxHeight() {
                return height.getTopY();
            }
            
            @Override
            public int getMinHeight() {
                return height.getBottomY();
            }
            
            @Override
            public Object getHandle() {
                return height;
            }
        };
    }
    
    public static com.dfsek.terra.api.block.state.properties.enums.Direction adapt(Direction direction) {
        return switch(direction) {
            case SOUTH -> com.dfsek.terra.api.block.state.properties.enums.Direction.SOUTH;
            case NORTH -> com.dfsek.terra.api.block.state.properties.enums.Direction.NORTH;
            case WEST -> com.dfsek.terra.api.block.state.properties.enums.Direction.WEST;
            case EAST -> com.dfsek.terra.api.block.state.properties.enums.Direction.EAST;
            case UP -> com.dfsek.terra.api.block.state.properties.enums.Direction.UP;
            case DOWN -> com.dfsek.terra.api.block.state.properties.enums.Direction.DOWN;
        };
    }
    
    public static WallHeight adapt(WallShape shape) {
        return switch(shape) {
            case LOW -> WallHeight.LOW;
            case NONE -> WallHeight.NONE;
            case TALL -> WallHeight.TALL;
        };
    }
    
    public static WallShape adapt(WallHeight shape) {
        return switch(shape) {
            case LOW -> WallShape.LOW;
            case NONE -> WallShape.NONE;
            case TALL -> WallShape.TALL;
        };
    }
    
    public static RedstoneConnection adapt(WireConnection connection) {
        return switch(connection) {
            case NONE -> RedstoneConnection.NONE;
            case UP -> RedstoneConnection.UP;
            case SIDE -> RedstoneConnection.SIDE;
        };
    }
    
    public static WireConnection adapt(RedstoneConnection connection) {
        return switch(connection) {
            case NONE -> WireConnection.NONE;
            case UP -> WireConnection.UP;
            case SIDE -> WireConnection.SIDE;
        };
    }
    
    
    public static Half adapt(BlockHalf half) {
        return switch(half) {
            case BOTTOM -> Half.BOTTOM;
            case TOP -> Half.TOP;
        };
    }
    
    public static BlockHalf adapt(Half half) {
        return switch(half) {
            case TOP -> BlockHalf.TOP;
            case BOTTOM -> BlockHalf.BOTTOM;
            default -> throw new IllegalStateException();
        };
    }
    
    public static RailShape adapt(net.minecraft.block.enums.RailShape railShape) {
        return switch(railShape) {
            case EAST_WEST -> RailShape.EAST_WEST;
            case NORTH_EAST -> RailShape.NORTH_EAST;
            case NORTH_WEST -> RailShape.NORTH_WEST;
            case SOUTH_EAST -> RailShape.SOUTH_EAST;
            case SOUTH_WEST -> RailShape.SOUTH_WEST;
            case NORTH_SOUTH -> RailShape.NORTH_SOUTH;
            case ASCENDING_EAST -> RailShape.ASCENDING_EAST;
            case ASCENDING_NORTH -> RailShape.ASCENDING_NORTH;
            case ASCENDING_SOUTH -> RailShape.ASCENDING_SOUTH;
            case ASCENDING_WEST -> RailShape.ASCENDING_WEST;
        };
    }
    
    public static net.minecraft.block.enums.RailShape adapt(RailShape railShape) {
        return switch(railShape) {
            case EAST_WEST -> net.minecraft.block.enums.RailShape.EAST_WEST;
            case NORTH_EAST -> net.minecraft.block.enums.RailShape.NORTH_EAST;
            case NORTH_WEST -> net.minecraft.block.enums.RailShape.NORTH_WEST;
            case SOUTH_EAST -> net.minecraft.block.enums.RailShape.SOUTH_EAST;
            case SOUTH_WEST -> net.minecraft.block.enums.RailShape.SOUTH_WEST;
            case NORTH_SOUTH -> net.minecraft.block.enums.RailShape.NORTH_SOUTH;
            case ASCENDING_EAST -> net.minecraft.block.enums.RailShape.ASCENDING_EAST;
            case ASCENDING_NORTH -> net.minecraft.block.enums.RailShape.ASCENDING_NORTH;
            case ASCENDING_SOUTH -> net.minecraft.block.enums.RailShape.ASCENDING_SOUTH;
            case ASCENDING_WEST -> net.minecraft.block.enums.RailShape.ASCENDING_WEST;
        };
    }
    
    
    public static Axis adapt(Direction.Axis axis) {
        return switch(axis) {
            case X -> Axis.X;
            case Y -> Axis.Y;
            case Z -> Axis.Z;
        };
    }
    
    public static Direction.Axis adapt(Axis axis) {
        return switch(axis) {
            case Z -> Direction.Axis.Z;
            case Y -> Direction.Axis.Y;
            case X -> Direction.Axis.X;
        };
    }
}
