package com.dfsek.terra.api.structures.structure;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.block.Axis;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.data.Directional;
import com.dfsek.terra.api.platform.block.data.MultipleFacing;
import com.dfsek.terra.api.platform.block.data.Orientable;
import com.dfsek.terra.api.platform.block.data.Rail;
import com.dfsek.terra.api.platform.block.data.RedstoneWire;
import com.dfsek.terra.api.platform.block.data.Rotatable;
import com.dfsek.terra.api.platform.block.data.Wall;
import com.google.common.collect.Sets;
import net.jafama.FastMath;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class RotationUtil {
    private static final Set<BlockFace> CARDINALS = Sets.newHashSet(BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST);

    /**
     * Rotate and mirror a coordinate pair.
     *
     * @param orig Vector to rotate.
     * @param r    Rotation
     */
    public static void rotateVector(Vector2 orig, Rotation r) {
        Vector2 copy = orig.clone();
        switch(r) {
            case CW_90 -> copy.setX(orig.getZ()).setZ(-orig.getX());
            case CCW_90 -> copy.setX(-orig.getZ()).setZ(orig.getX());
            case CW_180 -> copy.multiply(-1);
        }
        orig.setX(copy.getX());
        orig.setZ(copy.getZ());
    }

    /**
     * Get the BlockFace with rotation and mirrors applied to it
     *
     * @param f BlockFace to apply rotation to
     * @param r Rotation
     * @return Rotated BlockFace
     */
    public static BlockFace getRotatedFace(BlockFace f, Rotation r) {
        BlockFace n = f;
        int rotateNum = r.getDegrees() / 90;
        int rn = faceRotation(f);
        if(rn >= 0) {
            n = fromRotation(faceRotation(n) + 4 * rotateNum);
        }
        return n;
    }

    /**
     * Get an integer representation of a BlockFace, to perform math on.
     *
     * @param f BlockFace to get integer for
     * @return integer representation of BlockFace
     */
    public static int faceRotation(BlockFace f) {
        return switch(f) {
            case NORTH -> 0;
            case NORTH_NORTH_EAST -> 1;
            case NORTH_EAST -> 2;
            case EAST_NORTH_EAST -> 3;
            case EAST -> 4;
            case EAST_SOUTH_EAST -> 5;
            case SOUTH_EAST -> 6;
            case SOUTH_SOUTH_EAST -> 7;
            case SOUTH -> 8;
            case SOUTH_SOUTH_WEST -> 9;
            case SOUTH_WEST -> 10;
            case WEST_SOUTH_WEST -> 11;
            case WEST -> 12;
            case WEST_NORTH_WEST -> 13;
            case NORTH_WEST -> 14;
            case NORTH_NORTH_WEST -> 15;
            default -> -1;
        };
    }

    /**
     * Convert integer to BlockFace representation
     *
     * @param r integer to get BlockFace for
     * @return BlockFace represented by integer.
     */
    public static BlockFace fromRotation(int r) {
        return switch(FastMath.floorMod(r, 16)) {
            case 0 -> BlockFace.NORTH;
            case 1 -> BlockFace.NORTH_NORTH_EAST;
            case 2 -> BlockFace.NORTH_EAST;
            case 3 -> BlockFace.EAST_NORTH_EAST;
            case 4 -> BlockFace.EAST;
            case 5 -> BlockFace.EAST_SOUTH_EAST;
            case 6 -> BlockFace.SOUTH_EAST;
            case 7 -> BlockFace.SOUTH_SOUTH_EAST;
            case 8 -> BlockFace.SOUTH;
            case 9 -> BlockFace.SOUTH_SOUTH_WEST;
            case 10 -> BlockFace.SOUTH_WEST;
            case 11 -> BlockFace.WEST_SOUTH_WEST;
            case 12 -> BlockFace.WEST;
            case 13 -> BlockFace.WEST_NORTH_WEST;
            case 14 -> BlockFace.NORTH_WEST;
            case 15 -> BlockFace.NORTH_NORTH_WEST;
            default -> throw new IllegalArgumentException();
        };
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
     * @return Rotated/mirrored shape
     */
    @SuppressWarnings("fallthrough")
    public static Rail.Shape getRotatedRail(Rail.Shape orig, Rotation r) {
        return switch(r) {
            case CCW_90 -> switch(orig) {
                case NORTH_WEST -> Rail.Shape.SOUTH_WEST;
                case NORTH_SOUTH -> Rail.Shape.EAST_WEST;
                case SOUTH_WEST -> Rail.Shape.SOUTH_EAST;
                case SOUTH_EAST -> Rail.Shape.NORTH_EAST;
                case EAST_WEST -> Rail.Shape.NORTH_SOUTH;
                case NORTH_EAST -> Rail.Shape.NORTH_WEST;
                case ASCENDING_EAST -> Rail.Shape.ASCENDING_NORTH;
                case ASCENDING_WEST -> Rail.Shape.ASCENDING_SOUTH;
                case ASCENDING_NORTH -> Rail.Shape.ASCENDING_WEST;
                case ASCENDING_SOUTH -> Rail.Shape.ASCENDING_EAST;
            };
            case CW_90 -> switch(orig) {
                case NORTH_WEST -> Rail.Shape.NORTH_EAST;
                case NORTH_SOUTH -> Rail.Shape.EAST_WEST;
                case SOUTH_WEST -> Rail.Shape.NORTH_WEST;
                case SOUTH_EAST -> Rail.Shape.SOUTH_WEST;
                case EAST_WEST -> Rail.Shape.NORTH_SOUTH;
                case NORTH_EAST -> Rail.Shape.SOUTH_EAST;
                case ASCENDING_EAST -> Rail.Shape.ASCENDING_SOUTH;
                case ASCENDING_WEST -> Rail.Shape.ASCENDING_NORTH;
                case ASCENDING_NORTH -> Rail.Shape.ASCENDING_EAST;
                case ASCENDING_SOUTH -> Rail.Shape.ASCENDING_WEST;
            };
            case CW_180 -> switch(orig) {
                case NORTH_WEST -> Rail.Shape.SOUTH_EAST;
                case NORTH_SOUTH -> Rail.Shape.NORTH_SOUTH;
                case SOUTH_WEST -> Rail.Shape.NORTH_EAST;
                case SOUTH_EAST -> Rail.Shape.NORTH_WEST;
                case EAST_WEST -> Rail.Shape.EAST_WEST;
                case NORTH_EAST -> Rail.Shape.SOUTH_WEST;
                case ASCENDING_EAST -> Rail.Shape.ASCENDING_WEST;
                case ASCENDING_WEST -> Rail.Shape.ASCENDING_EAST;
                case ASCENDING_NORTH -> Rail.Shape.ASCENDING_SOUTH;
                case ASCENDING_SOUTH -> Rail.Shape.ASCENDING_NORTH;
            };
            default -> orig;
        };
    }

    public static void rotateBlockData(BlockData data, Rotation r) {
        if(data instanceof Rotatable) {
            BlockFace rt = getRotatedFace(((Rotatable) data).getRotation(), r);
            ((Rotatable) data).setRotation(rt);
        } else if(data instanceof Directional) {
            BlockFace rt = getRotatedFace(((Directional) data).getFacing(), r);
            ((Directional) data).setFacing(rt);
        } else if(data instanceof MultipleFacing) {
            MultipleFacing mfData = (MultipleFacing) data;
            Map<BlockFace, Boolean> faces = new EnumMap<>(BlockFace.class);
            for(BlockFace f : mfData.getAllowedFaces()) {
                faces.put(f, mfData.hasFace(f));
            }
            for(Map.Entry<BlockFace, Boolean> face : faces.entrySet()) {
                mfData.setFace(getRotatedFace(face.getKey(), r), face.getValue());
            }
        } else if(data instanceof Rail) {
            Rail.Shape newShape = getRotatedRail(((Rail) data).getShape(), r);
            ((Rail) data).setShape(newShape);
        } else if(data instanceof Orientable) {
            Axis newAxis = getRotatedAxis(((Orientable) data).getAxis(), r);
            ((Orientable) data).setAxis(newAxis);
        } else if(data instanceof RedstoneWire) {
            Map<BlockFace, RedstoneWire.Connection> connections = new EnumMap<>(BlockFace.class);
            RedstoneWire rData = (RedstoneWire) data;
            for(BlockFace f : rData.getAllowedFaces()) {
                connections.put(f, rData.getFace(f));
            }
            for(Map.Entry<BlockFace, RedstoneWire.Connection> e : connections.entrySet()) {
                rData.setFace(getRotatedFace(e.getKey(), r), e.getValue());
            }
        } else if(data instanceof Wall) {
            Wall wallData = (Wall) data;
            Map<BlockFace, Wall.Height> faces = new EnumMap<>(BlockFace.class);
            for(BlockFace b : CARDINALS) faces.put(b, wallData.getHeight(b));
            for(Map.Entry<BlockFace, Wall.Height> face : faces.entrySet()) {
                wallData.setHeight(getRotatedFace(face.getKey(), r), face.getValue());
            }
        }
    }
}
