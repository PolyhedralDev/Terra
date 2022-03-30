/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.block.state.properties.base;

import com.dfsek.terra.api.block.state.properties.enums.Axis;
import com.dfsek.terra.api.block.state.properties.enums.Direction;
import com.dfsek.terra.api.block.state.properties.enums.Half;
import com.dfsek.terra.api.block.state.properties.enums.RailShape;
import com.dfsek.terra.api.block.state.properties.enums.RedstoneConnection;
import com.dfsek.terra.api.block.state.properties.enums.WallHeight;


@Deprecated
public final class Properties {
    public static final EnumProperty<Direction> DIRECTION = EnumProperty.of("facing", Direction.class);
    public static final EnumProperty<Axis> AXIS = EnumProperty.of("axis", Axis.class);
    
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty WEST = BooleanProperty.of("west");
    
    public static final EnumProperty<WallHeight> NORTH_HEIGHT = EnumProperty.of("north", WallHeight.class);
    public static final EnumProperty<WallHeight> SOUTH_HEIGHT = EnumProperty.of("south", WallHeight.class);
    public static final EnumProperty<WallHeight> EAST_HEIGHT = EnumProperty.of("east", WallHeight.class);
    public static final EnumProperty<WallHeight> WEST_HEIGHT = EnumProperty.of("west", WallHeight.class);
    
    public static final EnumProperty<RedstoneConnection> NORTH_CONNECTION = EnumProperty.of("north", RedstoneConnection.class);
    public static final EnumProperty<RedstoneConnection> SOUTH_CONNECTION = EnumProperty.of("south", RedstoneConnection.class);
    public static final EnumProperty<RedstoneConnection> EAST_CONNECTION = EnumProperty.of("east", RedstoneConnection.class);
    public static final EnumProperty<RedstoneConnection> WEST_CONNECTION = EnumProperty.of("west", RedstoneConnection.class);
    
    
    public static final EnumProperty<RailShape> RAIL_SHAPE = EnumProperty.of("shape", RailShape.class);
    public static final EnumProperty<Half> HALF = EnumProperty.of("half", Half.class);
    
    public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 15);
    
    public static final BooleanProperty WATERLOGGED = BooleanProperty.of("waterlogged");
}
