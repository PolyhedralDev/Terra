package com.dfsek.terra.bukkit.world.block.data;


import com.dfsek.terra.api.platform.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;

/**
 * Utility class to adapt Terra enums to Bukkit enums
 */
public final class TerraEnumAdapter {
    public static Stairs.Shape fromTerraStair(com.dfsek.terra.api.platform.block.data.Stairs.Shape shape) {
        switch(shape) {
            case STRAIGHT:
                return Stairs.Shape.STRAIGHT;
            case INNER_LEFT:
                return Stairs.Shape.INNER_LEFT;
            case OUTER_LEFT:
                return Stairs.Shape.OUTER_LEFT;
            case INNER_RIGHT:
                return Stairs.Shape.INNER_RIGHT;
            case OUTER_RIGHT:
                return Stairs.Shape.OUTER_RIGHT;
            default:
                throw new IllegalStateException();
        }
    }

    public static Bisected.Half fromTerraHalf(com.dfsek.terra.api.platform.block.data.Bisected.Half half) {
        switch(half) {
            case TOP:
                return Bisected.Half.TOP;
            case BOTTOM:
                return Bisected.Half.BOTTOM;
            default:
                throw new IllegalStateException();
        }
    }

    public static org.bukkit.block.BlockFace fromTerraBlockFace(BlockFace face) {
        switch(face) {
            case DOWN:
                return org.bukkit.block.BlockFace.DOWN;
            case UP:
                return org.bukkit.block.BlockFace.UP;
            case NORTH_WEST:
                return org.bukkit.block.BlockFace.NORTH_WEST;
            case NORTH_EAST:
                return org.bukkit.block.BlockFace.NORTH_EAST;
            case SOUTH_EAST:
                return org.bukkit.block.BlockFace.SOUTH_EAST;
            case SOUTH_WEST:
                return org.bukkit.block.BlockFace.SOUTH_WEST;
            case NORTH_NORTH_WEST:
                return org.bukkit.block.BlockFace.NORTH_NORTH_WEST;
            case WEST_NORTH_WEST:
                return org.bukkit.block.BlockFace.WEST_NORTH_WEST;
            case WEST_SOUTH_WEST:
                return org.bukkit.block.BlockFace.WEST_SOUTH_WEST;
            case SOUTH_SOUTH_WEST:
                return org.bukkit.block.BlockFace.SOUTH_SOUTH_WEST;
            case EAST_NORTH_EAST:
                return org.bukkit.block.BlockFace.EAST_NORTH_EAST;
            case WEST:
                return org.bukkit.block.BlockFace.WEST;
            case SOUTH:
                return org.bukkit.block.BlockFace.SOUTH;
            case EAST:
                return org.bukkit.block.BlockFace.EAST;
            case NORTH:
                return org.bukkit.block.BlockFace.NORTH;
            case SELF:
                return org.bukkit.block.BlockFace.SELF;
            case EAST_SOUTH_EAST:
                return org.bukkit.block.BlockFace.EAST_SOUTH_EAST;
            case NORTH_NORTH_EAST:
                return org.bukkit.block.BlockFace.NORTH_NORTH_EAST;
            case SOUTH_SOUTH_EAST:
                return org.bukkit.block.BlockFace.SOUTH_SOUTH_EAST;
            default:
                throw new IllegalStateException();
        }
    }

    public static Slab.Type fromTerraSlabType(com.dfsek.terra.api.platform.block.data.Slab.Type type) {
        switch(type) {
            case TOP:
                return Slab.Type.TOP;
            case DOUBLE:
                return Slab.Type.DOUBLE;
            case BOTTOM:
                return Slab.Type.BOTTOM;
            default:
                throw new IllegalStateException();
        }
    }
}
