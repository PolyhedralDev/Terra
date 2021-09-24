package com.dfsek.terra.api.block.state.properties.enums;

import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.generic.Construct;


public enum Direction {
    NORTH(0, 0, 0, -1),
    EAST(1, 1, 0, 0),
    SOUTH(2, 0, 0, 1),
    WEST(3, -1, 0, 0),
    UP(-1, 0, 1, 0),
    DOWN(-1, 0, -1, 0);
    
    private static final Direction[] rotations = Construct.construct(() -> new Direction[]{ NORTH, SOUTH, EAST, WEST });
    
    private final int rotation;
    
    private final int modX;
    private final int modY;
    private final int modZ;
    
    Direction(int rotation, int modX, int modY, int modZ) {
        this.rotation = rotation;
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }
    
    public Direction rotate(Rotation rotation) {
        switch(this) {
            case UP:
            case DOWN:
                return this;
            default:
                return rotations[(this.rotation + rotation.getDegrees() / 90) % 4];
        }
    }
    
    public Direction opposite() {
        switch(this) {
            case DOWN:
                return UP;
            case UP:
                return DOWN;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
        }
        
        throw new IllegalStateException();
    }
    
    public int getModX() {
        return modX;
    }
    
    public int getModY() {
        return modY;
    }
    
    public int getModZ() {
        return modZ;
    }
}
