package com.dfsek.terra.api.block.data.properties.base;

import com.dfsek.terra.api.block.data.properties.enums.Direction;
import com.dfsek.terra.api.block.data.properties.enums.RailShape;

public final class Properties {
    public static final EnumProperty<Direction> DIRECTION = EnumProperty.of("facing", Direction.class);

    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    public static final EnumProperty<RailShape> RAIL_SHAPE = EnumProperty.of("shape", RailShape.class);

    public static final IntProperty ROTATION = IntProperty.of("rotation", 0, 15);
}
