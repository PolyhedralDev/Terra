/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.base.BooleanProperty;
import com.dfsek.terra.api.block.state.properties.base.EnumProperty;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.block.state.properties.enums.RailShape;
import com.dfsek.terra.api.block.state.properties.enums.RedstoneConnection;
import com.dfsek.terra.api.block.state.properties.enums.WallHeight;
import com.dfsek.terra.api.util.vector.Vector2;
import com.dfsek.terra.api.util.vector.Vector2.Mutable;


public final class RotationUtil {
    /**
     * Rotate and mirror a coordinate pair.
     *
     * @param orig Vector to rotate.
     * @param r    Rotation
     *
     * @return Rotated vector
     */
    public static Vector2 rotateVector(Vector2 orig, Rotation r) {
        Mutable copy = orig.mutable();
        switch(r) {
            case CW_90 -> copy.setX(orig.getZ()).setZ(-orig.getX());
            case CCW_90 -> copy.setX(-orig.getZ()).setZ(orig.getX());
            case CW_180 -> copy.multiply(-1);
        }
        return copy.immutable();
    }
    
    
    public static Axis getRotatedAxis(Axis orig, Rotation r) {
        Axis other = orig;
        final boolean shouldSwitch = r.equals(Rotation.CW_90) || r.equals(Rotation.CCW_90);
        switch(orig) {
            case X:
                if(shouldSwitch) other = Axis.Z;
                break;
            case Z:
                if(shouldSwitch) other = Axis.X;
                break;
        }
        return other;
    }
    
    /**
     * Method to rotate the incredibly obnoxious Rail.Shape enum
     *
     * @param orig Original shape
     * @param r    Rotate
     *
     * @return Rotated/mirrored shape
     */
    @SuppressWarnings("fallthrough")
    public static RailShape getRotatedRail(RailShape orig, Rotation r) {
        return switch(r) {
            case CCW_90 -> switch(orig) {
                case NORTH_WEST -> RailShape.SOUTH_WEST;
                case NORTH_SOUTH -> RailShape.EAST_WEST;
                case SOUTH_WEST -> RailShape.SOUTH_EAST;
                case SOUTH_EAST -> RailShape.NORTH_EAST;
                case EAST_WEST -> RailShape.NORTH_SOUTH;
                case NORTH_EAST -> RailShape.NORTH_WEST;
                case ASCENDING_EAST -> RailShape.ASCENDING_NORTH;
                case ASCENDING_WEST -> RailShape.ASCENDING_SOUTH;
                case ASCENDING_NORTH -> RailShape.ASCENDING_WEST;
                case ASCENDING_SOUTH -> RailShape.ASCENDING_EAST;
            };
            case CW_90 -> switch(orig) {
                case NORTH_WEST -> RailShape.NORTH_EAST;
                case NORTH_SOUTH -> RailShape.EAST_WEST;
                case SOUTH_WEST -> RailShape.NORTH_WEST;
                case SOUTH_EAST -> RailShape.SOUTH_WEST;
                case EAST_WEST -> RailShape.NORTH_SOUTH;
                case NORTH_EAST -> RailShape.SOUTH_EAST;
                case ASCENDING_EAST -> RailShape.ASCENDING_SOUTH;
                case ASCENDING_WEST -> RailShape.ASCENDING_NORTH;
                case ASCENDING_NORTH -> RailShape.ASCENDING_EAST;
                case ASCENDING_SOUTH -> RailShape.ASCENDING_WEST;
            };
            case CW_180 -> switch(orig) {
                case NORTH_WEST -> RailShape.SOUTH_EAST;
                case NORTH_SOUTH -> RailShape.NORTH_SOUTH;
                case SOUTH_WEST -> RailShape.NORTH_EAST;
                case SOUTH_EAST -> RailShape.NORTH_WEST;
                case EAST_WEST -> RailShape.EAST_WEST;
                case NORTH_EAST -> RailShape.SOUTH_WEST;
                case ASCENDING_EAST -> RailShape.ASCENDING_WEST;
                case ASCENDING_WEST -> RailShape.ASCENDING_EAST;
                case ASCENDING_NORTH -> RailShape.ASCENDING_SOUTH;
                case ASCENDING_SOUTH -> RailShape.ASCENDING_NORTH;
            };
            default -> orig;
        };
    }
    
    public static void rotateBlockData(BlockState data, Rotation r) {
        data
                .ifProperty(Properties.NORTH, state -> state.set(rotateCardinal(Properties.NORTH, r), state.get(Properties.NORTH)))
                .ifProperty(Properties.SOUTH, state -> state.set(rotateCardinal(Properties.SOUTH, r), state.get(Properties.SOUTH)))
                .ifProperty(Properties.EAST, state -> state.set(rotateCardinal(Properties.EAST, r), state.get(Properties.EAST)))
                .ifProperty(Properties.WEST, state -> state.set(rotateCardinal(Properties.WEST, r), state.get(Properties.WEST)))
                .ifProperty(Properties.DIRECTION, state -> state.set(Properties.DIRECTION, state.get(Properties.DIRECTION).rotate(r)))
                .ifProperty(Properties.AXIS, state -> state.set(Properties.AXIS, getRotatedAxis(state.get(Properties.AXIS), r)))
                .ifProperty(Properties.RAIL_SHAPE,
                            state -> state.set(Properties.RAIL_SHAPE, getRotatedRail(state.get(Properties.RAIL_SHAPE), r)))
                .ifProperty(Properties.NORTH_CONNECTION,
                            state -> state.set(rotateRedstone(Properties.NORTH_CONNECTION, r), state.get(Properties.NORTH_CONNECTION)))
                .ifProperty(Properties.SOUTH_CONNECTION,
                            state -> state.set(rotateRedstone(Properties.SOUTH_CONNECTION, r), state.get(Properties.SOUTH_CONNECTION)))
                .ifProperty(Properties.EAST_CONNECTION,
                            state -> state.set(rotateRedstone(Properties.EAST_CONNECTION, r), state.get(Properties.EAST_CONNECTION)))
                .ifProperty(Properties.WEST_CONNECTION,
                            state -> state.set(rotateRedstone(Properties.WEST_CONNECTION, r), state.get(Properties.WEST_CONNECTION)))
                .ifProperty(Properties.NORTH_HEIGHT,
                            state -> state.set(rotateWall(Properties.NORTH_HEIGHT, r), state.get(Properties.NORTH_HEIGHT)))
                .ifProperty(Properties.SOUTH_HEIGHT,
                            state -> state.set(rotateWall(Properties.SOUTH_HEIGHT, r), state.get(Properties.SOUTH_HEIGHT)))
                .ifProperty(Properties.EAST_HEIGHT,
                            state -> state.set(rotateWall(Properties.EAST_HEIGHT, r), state.get(Properties.EAST_HEIGHT)))
                .ifProperty(Properties.WEST_HEIGHT,
                            state -> state.set(rotateWall(Properties.WEST_HEIGHT, r), state.get(Properties.WEST_HEIGHT)));
    }
    
    private static BooleanProperty rotateCardinal(BooleanProperty property, Rotation r) {
        switch(r) {
            case NONE:
                return property;
            case CW_90:
                if(property == Properties.NORTH) return Properties.EAST;
                if(property == Properties.EAST) return Properties.SOUTH;
                if(property == Properties.SOUTH) return Properties.WEST;
                if(property == Properties.WEST) return Properties.NORTH;
                throw new IllegalStateException();
            case CW_180:
                if(property == Properties.NORTH) return Properties.SOUTH;
                if(property == Properties.EAST) return Properties.WEST;
                if(property == Properties.SOUTH) return Properties.NORTH;
                if(property == Properties.WEST) return Properties.EAST;
                throw new IllegalStateException();
            case CCW_90:
                if(property == Properties.NORTH) return Properties.WEST;
                if(property == Properties.EAST) return Properties.NORTH;
                if(property == Properties.SOUTH) return Properties.EAST;
                if(property == Properties.WEST) return Properties.SOUTH;
                throw new IllegalStateException();
        }
        throw new IllegalStateException();
    }
    
    private static EnumProperty<RedstoneConnection> rotateRedstone(EnumProperty<RedstoneConnection> property, Rotation r) {
        switch(r) {
            case NONE:
                return property;
            case CW_90:
                if(property == Properties.NORTH_CONNECTION) return Properties.EAST_CONNECTION;
                if(property == Properties.EAST_CONNECTION) return Properties.SOUTH_CONNECTION;
                if(property == Properties.SOUTH_CONNECTION) return Properties.WEST_CONNECTION;
                if(property == Properties.WEST_CONNECTION) return Properties.NORTH_CONNECTION;
                throw new IllegalStateException();
            case CW_180:
                if(property == Properties.NORTH_CONNECTION) return Properties.SOUTH_CONNECTION;
                if(property == Properties.EAST_CONNECTION) return Properties.WEST_CONNECTION;
                if(property == Properties.SOUTH_CONNECTION) return Properties.NORTH_CONNECTION;
                if(property == Properties.WEST_CONNECTION) return Properties.EAST_CONNECTION;
                throw new IllegalStateException();
            case CCW_90:
                if(property == Properties.NORTH_CONNECTION) return Properties.WEST_CONNECTION;
                if(property == Properties.EAST_CONNECTION) return Properties.NORTH_CONNECTION;
                if(property == Properties.SOUTH_CONNECTION) return Properties.EAST_CONNECTION;
                if(property == Properties.WEST_CONNECTION) return Properties.SOUTH_CONNECTION;
                throw new IllegalStateException();
        }
        throw new IllegalStateException();
    }
    
    private static EnumProperty<WallHeight> rotateWall(EnumProperty<WallHeight> property, Rotation r) {
        switch(r) {
            case NONE:
                return property;
            case CW_90:
                if(property == Properties.NORTH_HEIGHT) return Properties.EAST_HEIGHT;
                if(property == Properties.EAST_HEIGHT) return Properties.SOUTH_HEIGHT;
                if(property == Properties.SOUTH_HEIGHT) return Properties.WEST_HEIGHT;
                if(property == Properties.WEST_HEIGHT) return Properties.NORTH_HEIGHT;
                throw new IllegalStateException();
            case CW_180:
                if(property == Properties.NORTH_HEIGHT) return Properties.SOUTH_HEIGHT;
                if(property == Properties.EAST_HEIGHT) return Properties.WEST_HEIGHT;
                if(property == Properties.SOUTH_HEIGHT) return Properties.NORTH_HEIGHT;
                if(property == Properties.WEST_HEIGHT) return Properties.EAST_HEIGHT;
                throw new IllegalStateException();
            case CCW_90:
                if(property == Properties.NORTH_HEIGHT) return Properties.WEST_HEIGHT;
                if(property == Properties.EAST_HEIGHT) return Properties.NORTH_HEIGHT;
                if(property == Properties.SOUTH_HEIGHT) return Properties.EAST_HEIGHT;
                if(property == Properties.WEST_HEIGHT) return Properties.SOUTH_HEIGHT;
                throw new IllegalStateException();
        }
        throw new IllegalStateException();
    }
}
