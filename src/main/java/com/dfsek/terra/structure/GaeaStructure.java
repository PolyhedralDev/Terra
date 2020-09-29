package com.dfsek.terra.structure;

import com.dfsek.terra.Range;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Stairs;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class GaeaStructure implements Serializable {
    public static final long serialVersionUID = -6664585217063842035L;
    private final StructureContainedBlock[][][] structure;
    private final GaeaStructureInfo structureInfo;
    private final String id;
    private final UUID uuid;

    @NotNull
    public static GaeaStructure load(@NotNull File f) throws IOException {
        try {
            return fromFile(f);
        } catch(ClassNotFoundException e) {
            throw new IllegalArgumentException("Provided file does not contain a GaeaStructure.");
        }
    }

    public GaeaStructure(@NotNull Location l1, @NotNull Location l2, @NotNull String id) throws InitializationException {
        int centerX = -1, centerZ = -1;
        this.id = id;
        this.uuid = UUID.randomUUID();
        if(l1.getX() > l2.getX() || l1.getY() > l2.getY() || l1.getZ() > l2.getZ()) throw new IllegalArgumentException("Invalid locations provided!");
        structure = new StructureContainedBlock[l2.getBlockX()-l1.getBlockX()+1][l2.getBlockZ()-l1.getBlockZ()+1][l2.getBlockY()-l1.getBlockY()+1];
        for(int x = 0; x <= l2.getBlockX()-l1.getBlockX(); x++) {
            for(int z = 0; z <= l2.getBlockZ()-l1.getBlockZ(); z++) {
                for(int y = 0; y <= l2.getBlockY()-l1.getBlockY(); y++) {
                    Block b = Objects.requireNonNull(l1.getWorld()).getBlockAt(l1.clone().add(x, y, z));
                    BlockState state = b.getState();
                    BlockData d = b.getBlockData();
                    boolean useState = true;
                    if(state instanceof Sign) {
                        Sign s = (Sign) b.getState();
                        if(s.getLine(0).equals("[TERRA]")) {
                            try {
                                d = Bukkit.createBlockData(s.getLine(2) + s.getLine(3));
                                useState = false;
                                if(s.getLine(1).equals("[CENTER]")) {
                                    centerX = x;
                                    centerZ = z;
                                }
                            } catch(IllegalArgumentException e) {
                                throw new InitializationException("Invalid Block Data on sign: \"" + s.getLine(2) + s.getLine(3) + "\"");
                            }
                        }
                    }
                    structure[x][z][y] = new StructureContainedBlock(x, y, z, useState ? state : null, d);
                }
            }
        }
        if(centerX < 0 || centerZ < 0) throw new InitializationException("No structure center specified.");
        structureInfo = new GaeaStructureInfo(l2.getBlockX()-l1.getBlockX()+1, l2.getBlockY()-l1.getBlockY()+1, l2.getBlockZ()-l1.getBlockZ()+1, centerX, centerZ);
    }

    @NotNull
    public GaeaStructureInfo getStructureInfo() {
        return structureInfo;
    }

    public void paste(@NotNull Location origin, Rotation r, List<Mirror> m) {
        this.executeForBlocksInRange(getRange(Axis.X), getRange(Axis.Y), getRange(Axis.Z), block -> pasteBlock(block, origin, r, m), r, m);
    }

    private int[] getRotatedCoords(int[] arr, Rotation r, List<Mirror> m) {
        int[] cp = Arrays.copyOf(arr, 2);
        switch(r) {
            case CW_90:
                arr[0] = cp[1];
                arr[1] = - cp[0];
                break;
            case CCW_90:
                arr[0] = - cp[1];
                arr[1] = cp[0];
                break;
            case CW_180:
                arr[0] = - cp[0];
                arr[1] = - cp[1];
                break;
        }
        if(m.contains(Mirror.X)) arr[0] = - arr[0];
        if(m.contains(Mirror.Z)) arr[1] = - arr[1];
        return arr;
    }

    /**
     * Inverse of {@link GaeaStructure#getRotatedCoords(int[], Rotation, List)}, gets the coordinates <i>before</i> rotation
     * @param arr 2-integer array holding X and Z coordinates
     * @param r Rotation
     * @param m Mirror
     * @return New coordinates
     */
    private int[] getOriginalCoords(int[] arr, Rotation r, List<Mirror> m) {
        int[] cp = Arrays.copyOf(arr, 2);
        switch(r) {
            case CW_90:
                arr[1] = cp[0];
                arr[0] = - cp[1];
                break;
            case CCW_90:
                arr[1] = - cp[0];
                arr[0] = cp[1];
                break;
            case CW_180:
                arr[1] = - cp[1];
                arr[0] = - cp[0];
                break;
        }
        if(m.contains(Mirror.X)) arr[0] = - arr[0];
        if(m.contains(Mirror.Z)) arr[1] = - arr[1];
        return arr;
    }

    /**
     * Get the BlockFace with rotation and mirrors applied to it
     * @param f BlockFace to apply rotation to
     * @param r Rotation
     * @param m Mirror
     * @return Rotated BlockFace
     */
    private BlockFace getRotatedFace(BlockFace f, Rotation r, List<Mirror> m) {
        BlockFace n = f;
        int rotateNum = r.getDegrees()/90;
        int rn = faceRotation(f);
        if(rn >= 0) {
            n = fromRotation(faceRotation(n) + 4*rotateNum);
        }
        if(f.getModX()!=0 && m.contains(Mirror.X)) n = n.getOppositeFace();
        if(f.getModZ()!=0 && m.contains(Mirror.Z)) n = n.getOppositeFace();
        return n;
    }

    /**
     * Get an integer representation of a BlockFace, to perform math on.
     * @param f BlockFace to get integer for
     * @return integer representation of BlockFace
     */
    private static int faceRotation(BlockFace f) {
        switch(f) {
            case NORTH: return 0;
            case NORTH_NORTH_EAST: return 1;
            case NORTH_EAST: return 2;
            case EAST_NORTH_EAST: return 3;
            case EAST: return 4;
            case EAST_SOUTH_EAST: return 5;
            case SOUTH_EAST: return 6;
            case SOUTH_SOUTH_EAST: return 7;
            case SOUTH: return 8;
            case SOUTH_SOUTH_WEST: return 9;
            case SOUTH_WEST: return 10;
            case WEST_SOUTH_WEST: return 11;
            case WEST: return 12;
            case WEST_NORTH_WEST: return 13;
            case NORTH_WEST: return 14;
            case NORTH_NORTH_WEST: return 15;
            default: return -1;
        }
    }

    /**
     * Convert integer to BlockFace representation
     * @param r integer to get BlockFace for
     * @return BlockFace represented by integer.
     */
    private static BlockFace fromRotation(int r) {
        switch(Math.floorMod(r, 16)) {
            case 0: return BlockFace.NORTH;
            case 1: return BlockFace.NORTH_NORTH_EAST;
            case 2: return BlockFace.NORTH_EAST;
            case 3: return BlockFace.EAST_NORTH_EAST;
            case 4: return BlockFace.EAST;
            case 5: return BlockFace.EAST_SOUTH_EAST;
            case 6: return BlockFace.SOUTH_EAST;
            case 7: return BlockFace.SOUTH_SOUTH_EAST;
            case 8: return BlockFace.SOUTH;
            case 9: return BlockFace.SOUTH_SOUTH_WEST;
            case 10: return BlockFace.SOUTH_WEST;
            case 11: return BlockFace.WEST_SOUTH_WEST;
            case 12: return BlockFace.WEST;
            case 13: return BlockFace.WEST_NORTH_WEST;
            case 14: return BlockFace.NORTH_WEST;
            case 15: return BlockFace.NORTH_NORTH_WEST;
            default: throw new IllegalArgumentException();
        }
    }


    /**
     * Paste structure at an origin location, confined to a single chunk.
     * @param origin Origin location
     * @param chunk Chunk to confine pasting to
     * @param r Rotation
     * @param m Mirror
     */
    public void paste(Location origin, Chunk chunk, Rotation r, List<Mirror> m) {
        int xOr = (chunk.getX() << 4);
        int zOr = (chunk.getZ() << 4);
        Range intersectX;
        Range intersectZ;
        intersectX = new Range(xOr, xOr+16).sub(origin.getBlockX() - structureInfo.getCenterX());
        intersectZ = new Range(zOr, zOr+16).sub(origin.getBlockZ() - structureInfo.getCenterZ());
        if(intersectX == null || intersectZ == null) return;
        executeForBlocksInRange(intersectX, getRange(Axis.Y), intersectZ, block -> pasteBlock(block, origin, r, m), r, m);
        Bukkit.getLogger().info(intersectX.toString() + " : " + intersectZ.toString());
    }

    /**
     * Paste a single StructureDefinedBlock at an origin location, offset by its coordinates.
     * @param block The block to paste
     * @param origin The origin location
     * @param r The rotation of the structure
     * @param m The mirror type of the structure
     */
    private void pasteBlock(StructureContainedBlock block, Location origin, Rotation r, List<Mirror> m) {
        BlockData data = block.getBlockData().clone();
        Block worldBlock = origin.clone().add(block.getX(), block.getY(), block.getZ()).getBlock();
        if(!data.getMaterial().equals(Material.STRUCTURE_VOID)) {
            if(data instanceof Rotatable) {
                BlockFace rt = getRotatedFace(((Rotatable) data).getRotation(), r, m);
                Bukkit.getLogger().info("Previous: " + ((Rotatable) data).getRotation() + ", New: " + rt);
                ((Rotatable) data).setRotation(rt);
            } else if(data instanceof Directional) {
                BlockFace rt = getRotatedFace(((Directional) data).getFacing(), r, m);
                Bukkit.getLogger().info("Previous: " + ((Directional) data).getFacing() + ", New: " + rt);
                ((Directional) data).setFacing(rt);
            }
            worldBlock.setBlockData(data, false);
            if(block.getState() != null) {
                block.getState().getState(worldBlock.getState()).update();
            }
        }
    }

    /**
     * Execute a Consumer for all blocks in a cuboid region defined by 3 Ranges, accounting for rotation.
     * @param xM X Range
     * @param yM Y Range
     * @param zM Z Range
     * @param exec Consumer to execute for each block.
     * @param r Rotation
     * @param m Mirror
     */
    private void executeForBlocksInRange(Range xM, Range yM, Range zM, Consumer<StructureContainedBlock> exec, Rotation r, List<Mirror> m) {
        for(int x : xM) {
            for(int y : yM) {
                for(int z : zM) {
                    int[] c = getRotatedCoords(new int[] {x-structureInfo.getCenterX(), z-structureInfo.getCenterZ()}, r, m);
                    c[0] = c[0] + structureInfo.getCenterX();
                    c[1] = c[1] + structureInfo.getCenterZ();
                    Bukkit.getLogger().info("Before: " + x + ", " + z + " After: " + c[0] + ", " + c[1]);
                    if(isInStructure(c[0], y, c[1])) {
                        StructureContainedBlock b = structure[c[0]][c[1]][y];
                        exec.accept(new StructureContainedBlock(x - getStructureInfo().getCenterX(), y, z - getStructureInfo().getCenterZ(), b.getState(), b.getBlockData()));
                    }
                }
            }
        }
    }

    /**
     * Test whether a set of coordinates is within the current structure
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return True if coordinate set is in structure, false if it is not.
     */
    private boolean isInStructure(int x, int y, int z) {
        return x < structureInfo.getSizeX() && y < structureInfo.getSizeY() && z < structureInfo.getSizeZ() && x >= 0 && y >= 0 && z >= 0;
    }

    /**
     * Save the structure to a file
     * @param f File to save to
     * @throws IOException If file access error occurs
     */
    public void save(@NotNull File f) throws IOException {
        toFile(this, f);
    }

    /**
     * Load a structure from a file.
     * @param f File to load from
     * @return The structure loaded
     * @throws IOException If file access error occurs
     * @throws ClassNotFoundException If structure data is invalid.
     */
    @NotNull
    private static GaeaStructure fromFile(@NotNull File f) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        Object o = ois.readObject();
        ois.close();
        return (GaeaStructure) o;
    }

    @NotNull
    public static GaeaStructure fromStream(@NotNull InputStream f) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(f);
        Object o = ois.readObject();
        ois.close();
        return (GaeaStructure) o;
    }

    private static void toFile(@NotNull Serializable o, @NotNull File f) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(o);
        oos.close();
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public UUID getUuid() {
        return uuid;
    }

    public Range getRange(Axis a) {
        switch(a) {
            case X:
                return new Range(0, structureInfo.getSizeX());
            case Y:
                return new Range(0, structureInfo.getSizeY());
            case Z:
                return new Range(0, structureInfo.getSizeZ());
            default: return null;
        }
    }

    public enum Axis {
        X, Y, Z
    }
    public enum Rotation {
        CW_90(90), CW_180(180), CCW_90(270), NONE(0);
        private final int degrees;
        Rotation(int degrees) {
            this.degrees = degrees;
        }

        public int getDegrees() {
            return degrees;
        }
        public static Rotation fromDegrees(int deg) {
            switch(Math.floorMod(deg, 360)) {
                case 0: return Rotation.NONE;
                case 90: return Rotation.CW_90;
                case 180: return Rotation.CW_180;
                case 270: return Rotation.CCW_90;
                default: throw new IllegalArgumentException();
            }
        }
    }
    public enum Mirror {
        NONE, X, Z
    }
}
