package com.dfsek.terra.structure;

import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.procgen.math.Vector2;
import com.dfsek.terra.util.structure.RotationUtil;
import net.jafama.FastMath;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.BlockInventoryHolder;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static com.dfsek.terra.util.structure.RotationUtil.getRotatedCoords;

@SuppressWarnings("unused")
public class Structure implements Serializable {
    private static final long serialVersionUID = -6664585217063842035L;
    private final StructureContainedBlock[][][] structure;
    private final StructureInfo structureInfo;
    private final String id;
    private final UUID uuid;
    private final HashSet<StructureContainedBlock> spawns;
    private final HashSet<StructureContainedInventory> inventories;

    public Structure(@NotNull Location l1, @NotNull Location l2, @NotNull String id) throws InitializationException {
        int centerX = -1, centerZ = -1;
        this.id = id;
        this.uuid = UUID.randomUUID();
        this.spawns = new HashSet<>();
        this.inventories = new HashSet<>();
        if(l1.getX() > l2.getX() || l1.getY() > l2.getY() || l1.getZ() > l2.getZ())
            throw new IllegalArgumentException("Invalid locations provided!");
        structure = new StructureContainedBlock[l2.getBlockX() - l1.getBlockX() + 1][l2.getBlockZ() - l1.getBlockZ() + 1][l2.getBlockY() - l1.getBlockY() + 1];
        for(int x = 0; x <= l2.getBlockX() - l1.getBlockX(); x++) {
            for(int z = 0; z <= l2.getBlockZ() - l1.getBlockZ(); z++) {
                for(int y = 0; y <= l2.getBlockY() - l1.getBlockY(); y++) {
                    Block b = Objects.requireNonNull(l1.getWorld()).getBlockAt(l1.clone().add(x, y, z));
                    BlockState state = b.getState();
                    BlockData d = b.getBlockData();
                    boolean useState = true;
                    StructureContainedBlock.Pull pull = StructureContainedBlock.Pull.NONE;
                    int pullOffset = 0;
                    StructureSpawnRequirement requirement = StructureSpawnRequirement.BLANK;
                    try {
                        if(state instanceof Sign) { // Magic sign stuff
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
                                        String spawn = og.substring(og.indexOf("=") + 1, og.length() - 1);
                                        try {
                                            requirement = StructureSpawnRequirement.valueOf(spawn.toUpperCase());
                                        } catch(IllegalArgumentException e) {
                                            throw new InitializationException("Invalid spawn type: " + spawn, b.getLocation());
                                        }
                                    } else if(s.getLine(1).startsWith("[PULL=") && s.getLine(1).endsWith("]")) {
                                        String og = s.getLine(1);
                                        String spawn = og.substring(og.indexOf("=") + 1, og.indexOf("_"));
                                        pullOffset = Integer.parseInt(og.substring(og.indexOf("_") + 1, og.length() - 1));
                                        try {
                                            pull = StructureContainedBlock.Pull.valueOf(spawn);
                                        } catch(IllegalArgumentException e) {
                                            throw new InitializationException("Invalid pull type: " + spawn, b.getLocation());
                                        }
                                    } else {
                                        throw new InitializationException("Invalid Magic Sign: \"" + s.getLine(1) + "\"", b.getLocation());
                                    }
                                } catch(IllegalArgumentException e) {
                                    throw new InitializationException("Invalid Block Data on sign: \"" + s.getLine(2) + s.getLine(3) + "\"", b.getLocation());
                                }
                            }
                        }
                    } catch(StringIndexOutOfBoundsException e) {
                        throw new InitializationException("Invalid sign.", b.getLocation());
                    }
                    StructureContainedBlock block = new StructureContainedBlock(x, y, z, useState ? state : null, d, requirement, pull, pullOffset);
                    if(state instanceof BlockInventoryHolder) {
                        inventories.add(new StructureContainedInventory(((BlockInventoryHolder) state).getInventory(), block));
                    }
                    if(!requirement.equals(StructureSpawnRequirement.BLANK)) spawns.add(block);
                    structure[x][z][y] = block;
                }
            }
        }
        if(centerX < 0 || centerZ < 0) throw new InitializationException("No structure center specified.", null);
        structureInfo = new StructureInfo(l2.getBlockX() - l1.getBlockX() + 1, l2.getBlockY() - l1.getBlockY() + 1, l2.getBlockZ() - l1.getBlockZ() + 1, new Vector2(centerX, centerZ));
    }

    @NotNull
    public static Structure load(@NotNull File f) throws IOException {
        try {
            return fromFile(f);
        } catch(ClassNotFoundException e) {
            throw new IllegalArgumentException("Provided file does not contain a GaeaStructure.");
        }
    }

    /**
     * Load a structure from a file.
     *
     * @param f File to load from
     * @return The structure loaded
     * @throws IOException            If file access error occurs
     * @throws ClassNotFoundException If structure data is invalid.
     */
    @NotNull
    private static Structure fromFile(@NotNull File f) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        Object o = ois.readObject();
        ois.close();
        return (Structure) o;
    }

    @NotNull
    public static Structure fromStream(@NotNull InputStream f) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(f);
        Object o = ois.readObject();
        ois.close();
        return (Structure) o;
    }

    private static void toFile(@NotNull Serializable o, @NotNull File f) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(o);
        oos.close();
    }

    public StructureContainedBlock[][][] getRawStructure() {
        return structure;
    }

    /**
     * Paste the structure at a Location, ignoring chunk boundaries.
     *
     * @param origin Origin location
     * @param r      Rotation
     */
    public void paste(@NotNull Location origin, Rotation r) {
        Range xRange = getRange(Rotation.Axis.X, r);
        Range zRange = getRange(Rotation.Axis.Z, r);
        this.executeForBlocksInRange(xRange, getRange(Rotation.Axis.Y, r), zRange, block -> pasteBlock(block, origin, r), r);
    }

    /**
     * Paste a single StructureDefinedBlock at an origin location, offset by its coordinates.
     *
     * @param block  The block to paste
     * @param origin The origin location
     * @param r      The rotation of the structure
     */
    private void pasteBlock(StructureContainedBlock block, Location origin, Rotation r) {
        BlockData data = block.getBlockData().clone();
        if(!data.getMaterial().equals(Material.STRUCTURE_VOID)) {

            Location loc = origin.clone().add(block.getX(), block.getY(), block.getZ());
            Block worldBlock = loc.getBlock();

            main:
            while(worldBlock.isEmpty()) {
                if(loc.getBlockY() > 255 || loc.getBlockY() < 0) return;
                if(block.getPull() == null) break;
                switch(block.getPull()) {
                    case UP:
                        worldBlock = worldBlock.getRelative(BlockFace.UP);
                        break;
                    case DOWN:
                        worldBlock = worldBlock.getRelative(BlockFace.DOWN);
                        break;
                    default:
                        break main;
                }

            }
            int offset = block.getPullOffset();
            if(offset != 0)
                worldBlock = worldBlock.getRelative((offset > 0) ? BlockFace.UP : BlockFace.DOWN, FastMath.abs(offset));

            RotationUtil.rotateBlockData(data, r);

            worldBlock.setBlockData(data, false);
            if(block.getState() != null) {
                block.getState().getState(worldBlock.getState()).update(true, false);
            }
        }
    }

    /**
     * Execute a Consumer for all blocks in a cuboid region defined by 3 Ranges, accounting for rotation.
     *
     * @param xM   X Range
     * @param yM   Y Range
     * @param zM   Z Range
     * @param exec Consumer to execute for each block.
     * @param r    Rotation
     */
    private void executeForBlocksInRange(Range xM, Range yM, Range zM, Consumer<StructureContainedBlock> exec, Rotation r) {
        for(int x : xM) {
            for(int y : yM) {
                for(int z : zM) {
                    Vector2 c = getRotatedCoords(new Vector2(x - structureInfo.getCenterX(), z - structureInfo.getCenterZ()), r);
                    c.add(new Vector2(structureInfo.getCenterX(), structureInfo.getCenterZ()));
                    if(isInStructure((int) c.getX(), y, (int) c.getZ())) {
                        StructureContainedBlock b = structure[(int) c.getX()][(int) c.getZ()][y];
                        exec.accept(new StructureContainedBlock(x - getStructureInfo().getCenterX(), y, z - getStructureInfo().getCenterZ(), b.getState(), b.getBlockData(), b.getRequirement(), b.getPull(), b.getPullOffset()));
                    }
                }
            }
        }
    }

    /**
     * Get GaeaStructureInfo object
     *
     * @return Structure Info
     */
    @NotNull
    public StructureInfo getStructureInfo() {
        return structureInfo;
    }

    /**
     * Test whether a set of coordinates is within the current structure
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return True if coordinate set is in structure, false if it is not.
     */
    private boolean isInStructure(int x, int y, int z) {
        return x < structureInfo.getSizeX() && y < structureInfo.getSizeY() && z < structureInfo.getSizeZ() && x >= 0 && y >= 0 && z >= 0;
    }

    @NotNull
    public Range getRange(@NotNull Rotation.Axis a, @NotNull Rotation r) {
        if(a.equals(Rotation.Axis.Y)) return getRawRange(a);
        Vector2 center = new Vector2(structureInfo.getCenterX(), structureInfo.getCenterZ());
        Range x = getRawRange(Rotation.Axis.X);
        Range z = getRawRange(Rotation.Axis.Z);
        Vector2 min = getRotatedCoords(new Vector2(x.getMin(), z.getMin()).subtract(center), r.inverse()).add(center);
        Vector2 max = getRotatedCoords(new Vector2(x.getMax(), z.getMax()).subtract(center), r.inverse()).add(center);

        if(a.equals(Rotation.Axis.X))
            return new Range((int) FastMath.floor(FastMath.min(min.getX(), max.getX())), (int) FastMath.ceil(FastMath.max(min.getX(), max.getX())) + 1);
        else
            return new Range((int) FastMath.floor(FastMath.min(min.getZ(), max.getZ())), (int) FastMath.ceil(FastMath.max(min.getZ(), max.getZ())) + 1);
    }

    @NotNull
    private Range getRawRange(@NotNull Rotation.Axis a) {
        switch(a) {
            case X:
                return new Range(0, structureInfo.getSizeX());
            case Y:
                return new Range(0, structureInfo.getSizeY());
            case Z:
                return new Range(0, structureInfo.getSizeZ());
            default:
                throw new IllegalArgumentException();
        }
    }

    public boolean checkSpawns(Location origin, Rotation r) {
        for(StructureContainedBlock b : spawns) {
            Vector2 rot = getRotatedCoords(new Vector2(b.getX() - structureInfo.getCenterX(), b.getZ() - structureInfo.getCenterZ()), r);
            if(!b.getRequirement().getInstance(origin.getWorld()).matches((int) rot.getX() + origin.getBlockX(), origin.getBlockY() + b.getY(), (int) rot.getZ() + origin.getBlockZ()))
                return false;
        }
        return true;
    }

    public HashSet<StructureContainedBlock> getSpawns() {
        return spawns;
    }

    public HashSet<StructureContainedInventory> getInventories() {
        return inventories;
    }

    /**
     * Paste structure at an origin location, confined to a single chunk.
     *
     * @param origin Origin location
     * @param chunk  Chunk to confine pasting to
     * @param r      Rotation
     */
    public void paste(Location origin, Chunk chunk, Rotation r) {
        int xOr = (chunk.getX() << 4);
        int zOr = (chunk.getZ() << 4);
        Range intersectX = new Range(xOr, xOr + 16).sub(origin.getBlockX() - structureInfo.getCenterX());
        Range intersectZ = new Range(zOr, zOr + 16).sub(origin.getBlockZ() - structureInfo.getCenterZ());
        if(intersectX == null || intersectZ == null) return;
        executeForBlocksInRange(intersectX, getRange(Rotation.Axis.Y, r), intersectZ, block -> pasteBlock(block, origin, r), r);
        Debug.info(intersectX.toString() + " : " + intersectZ.toString());
    }

    /**
     * From an origin location (First bound) fetch the second bound.
     *
     * @param origin Origin location
     * @return Other bound location
     */
    public Location getOtherBound(Location origin) {
        return origin.clone().add(structureInfo.getSizeX(), structureInfo.getSizeY(), structureInfo.getSizeZ());
    }

    /**
     * Save the structure to a file
     *
     * @param f File to save to
     * @throws IOException If file access error occurs
     */
    public void save(@NotNull File f) throws IOException {
        toFile(this, f);
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public UUID getUuid() {
        return uuid;
    }

}
