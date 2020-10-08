package com.dfsek.terra.structure;

import com.dfsek.terra.Debug;
import com.dfsek.terra.procgen.math.Vector2;
import com.dfsek.terra.util.structure.RotationUtil;
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
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.Rotatable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.math.Range;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import static com.dfsek.terra.util.structure.RotationUtil.*;

public class GaeaStructure implements Serializable {
    public static final long serialVersionUID = -6664585217063842035L;
    private final StructureContainedBlock[][][] structure;
    private final GaeaStructureInfo structureInfo;
    private final String id;
    private final UUID uuid;
    private final HashSet<StructureContainedBlock> spawns;

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
        this.spawns = new HashSet<>();
        if(l1.getX() > l2.getX() || l1.getY() > l2.getY() || l1.getZ() > l2.getZ()) throw new IllegalArgumentException("Invalid locations provided!");
        structure = new StructureContainedBlock[l2.getBlockX()-l1.getBlockX()+1][l2.getBlockZ()-l1.getBlockZ()+1][l2.getBlockY()-l1.getBlockY()+1];
        for(int x = 0; x <= l2.getBlockX()-l1.getBlockX(); x++) {
            for(int z = 0; z <= l2.getBlockZ()-l1.getBlockZ(); z++) {
                for(int y = 0; y <= l2.getBlockY()-l1.getBlockY(); y++) {
                    Block b = Objects.requireNonNull(l1.getWorld()).getBlockAt(l1.clone().add(x, y, z));
                    BlockState state = b.getState();
                    BlockData d = b.getBlockData();
                    boolean useState = true;
                    StructureSpawnRequirement requirement = StructureSpawnRequirement.BLANK;
                    if(state instanceof Sign) {
                        Sign s = (Sign) b.getState();
                        if(s.getLine(0).equals("[TERRA]")) {
                            try {
                                d = Bukkit.createBlockData(s.getLine(2) + s.getLine(3));
                                useState = false;
                                if(s.getLine(1).equals("[CENTER]")) {
                                    centerX = x;
                                    centerZ = z;
                                } else if(s.getLine(1).startsWith("[SPAWN=") && s.getLine(1).endsWith("]")) {
                                    String og = s.getLine(1);
                                    String spawn = og.substring(og.indexOf("=")+1, og.length()-1);
                                    try {
                                        requirement = StructureSpawnRequirement.valueOf(spawn.toUpperCase());
                                    } catch(IllegalArgumentException e) {
                                        throw new InitializationException("Invalid spawn type: " + spawn);
                                    }
                                }
                            } catch(IllegalArgumentException e) {
                                throw new InitializationException("Invalid Block Data on sign: \"" + s.getLine(2) + s.getLine(3) + "\"");
                            }
                        }
                    }
                    StructureContainedBlock block = new StructureContainedBlock(x, y, z, useState ? state : null, d, requirement);
                    if(!requirement.equals(StructureSpawnRequirement.BLANK)) spawns.add(block);
                    structure[x][z][y] = block;
                }
            }
        }
        if(centerX < 0 || centerZ < 0) throw new InitializationException("No structure center specified.");
        structureInfo = new GaeaStructureInfo(l2.getBlockX()-l1.getBlockX()+1, l2.getBlockY()-l1.getBlockY()+1, l2.getBlockZ()-l1.getBlockZ()+1, new Vector2(centerX, centerZ));
    }

    /**
     * Get GaeaStructureInfo object
     * @return Structure Info
     */
    @NotNull
    public GaeaStructureInfo getStructureInfo() {
        return structureInfo;
    }

    /**
     * Paste the structure at a Location, ignoring chunk boundaries.
     * @param origin Origin location
     * @param r Rotation
     */
    public void paste(@NotNull Location origin, Rotation r) {
        this.executeForBlocksInRange(getRange(Axis.X), getRange(Axis.Y), getRange(Axis.Z), block -> pasteBlock(block, origin, r), r);
    }

    public boolean checkSpawns(Location origin, Rotation r) {
        for(StructureContainedBlock b : spawns) {
            Vector2 rot = getRotatedCoords(new Vector2(b.getX()-structureInfo.getCenterX(), b.getZ()-structureInfo.getCenterZ()), r);
            if(!b.getRequirement().matches(origin.getWorld(), (int) rot.getX()+origin.getBlockX(), origin.getBlockY(), (int) rot.getZ()+origin.getBlockZ())) return false;
        }
        return true;
    }



    /**
     * Paste structure at an origin location, confined to a single chunk.
     * @param origin Origin location
     * @param chunk Chunk to confine pasting to
     * @param r Rotation
     */
    public void paste(Location origin, Chunk chunk, Rotation r) {
        int xOr = (chunk.getX() << 4);
        int zOr = (chunk.getZ() << 4);
        Range intersectX;
        Range intersectZ;
        intersectX = new Range(xOr, xOr+16).sub(origin.getBlockX() - structureInfo.getCenterX());
        intersectZ = new Range(zOr, zOr+16).sub(origin.getBlockZ() - structureInfo.getCenterZ());
        if(intersectX == null || intersectZ == null) return;
        executeForBlocksInRange(intersectX, getRange(Axis.Y), intersectZ, block -> pasteBlock(block, origin, r), r);
        Debug.info(intersectX.toString() + " : " + intersectZ.toString());
    }

    /**
     * Paste a single StructureDefinedBlock at an origin location, offset by its coordinates.
     * @param block The block to paste
     * @param origin The origin location
     * @param r The rotation of the structure
     */
    private void pasteBlock(StructureContainedBlock block, Location origin, Rotation r) {
        BlockData data = block.getBlockData().clone();
        Location loc = origin.clone().add(block.getX(), block.getY(), block.getZ());
        Block worldBlock = loc.getBlock();
        if(!data.getMaterial().equals(Material.STRUCTURE_VOID)) {
            if(data instanceof Rotatable) {
                BlockFace rt = getRotatedFace(((Rotatable) data).getRotation(), r);
                ((Rotatable) data).setRotation(rt);
            } else if(data instanceof Directional) {
                BlockFace rt = getRotatedFace(((Directional) data).getFacing(), r);
                ((Directional) data).setFacing(rt);
            } else if(data instanceof MultipleFacing) {
                MultipleFacing mfData = (MultipleFacing) data;
                List<BlockFace> faces = new ArrayList<>(mfData.getFaces());
                for(BlockFace face : faces) {
                    mfData.setFace(face, false);
                    mfData.setFace(getRotatedFace(face, r), true);
                }
            } else if(data instanceof Rail) {
                Rail.Shape newShape = getRotatedRail(((Rail) data).getShape(), r);
                ((Rail) data).setShape(newShape);
            } else if(data instanceof Orientable) {
                org.bukkit.Axis newAxis = getRotatedAxis(((Orientable) data).getAxis(), r);
                ((Orientable) data).setAxis(newAxis);
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
     */
    private void executeForBlocksInRange(Range xM, Range yM, Range zM, Consumer<StructureContainedBlock> exec, Rotation r) {
        for(int x : xM) {
            for(int y : yM) {
                for(int z : zM) {
                    Vector2 c = getRotatedCoords(new Vector2(x-structureInfo.getCenterX(), z-structureInfo.getCenterZ()), r);
                    c.add(new Vector2(structureInfo.getCenterX(), structureInfo.getCenterZ()));
                    if(isInStructure((int) c.getX(), y, (int) c.getZ())) {
                        StructureContainedBlock b = structure[(int) c.getX()][(int) c.getZ()][y];
                        exec.accept(new StructureContainedBlock(x - getStructureInfo().getCenterX(), y, z - getStructureInfo().getCenterZ(), b.getState(), b.getBlockData(), b.getRequirement()));
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
     * From an origin location (First bound) fetch the second bound.
     * @param origin Origin location
     * @return Other bound location
     */
    public Location getOtherBound(Location origin) {
        return origin.clone().add(structureInfo.getSizeX(), structureInfo.getSizeY(), structureInfo.getSizeZ());
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
}
