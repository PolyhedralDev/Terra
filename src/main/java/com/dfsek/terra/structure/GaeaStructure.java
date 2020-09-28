package com.dfsek.terra.structure;

import com.dfsek.terra.Range;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
        structure = new StructureContainedBlock[l2.getBlockX()-l1.getBlockX()+1][l2.getBlockY()-l1.getBlockY()+1][l2.getBlockZ()-l1.getBlockZ()+1];
        for(int x = 0; x <= l2.getBlockX()-l1.getBlockX(); x++) {
            for(int y = 0; y <= l2.getBlockY()-l1.getBlockY(); y++) {
                for(int z = 0; z <= l2.getBlockZ()-l1.getBlockZ(); z++) {
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
                    structure[x][y][z] = new StructureContainedBlock(x, y, z, useState ? state : null, d);
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

    public void paste(@NotNull Location origin) {
        this.executeForBlocksInRange(getRange(Axis.X), getRange(Axis.Y), getRange(Axis.Z), block -> pasteBlock(block, origin));
    }

    public void paste(Location origin, Chunk chunk) {
        int xOr = (chunk.getX() << 4);
        int zOr = (chunk.getZ() << 4);
        Range intersectX = new Range(xOr, xOr+16).sub(origin.getBlockX() - structureInfo.getCenterX());
        Range intersectZ = new Range(zOr, zOr+16).sub(origin.getBlockZ() - structureInfo.getCenterZ());
        if(intersectX == null || intersectZ == null) return;
        executeForBlocksInRange(intersectX, getRange(Axis.Y), intersectZ, block -> pasteBlock(block, origin));
    }

    private void pasteBlock(StructureContainedBlock block, Location origin) {
        BlockData data = block.getBlockData();
        Block worldBlock = origin.clone().add(block.getX()-structureInfo.getCenterX(), block.getY(), block.getZ()-structureInfo.getCenterZ()).getBlock();
        if(!data.getMaterial().equals(Material.STRUCTURE_VOID)) worldBlock.setBlockData(data, false);
        if(block.getState() != null) {
            block.getState().getState(worldBlock.getState()).update();
        }
    }

    private void executeForBlocksInRange(Range xM, Range yM, Range zM, Consumer<StructureContainedBlock> exec) {
        for(int x : xM) {
            for(int y : yM) {
                for(int z : zM) {
                    if(isInStructure(x, y, z)) exec.accept(structure[x][y][z]);
                }
            }
        }
    }

    private boolean isInStructure(int x, int y, int z) {
        return x < structureInfo.getSizeX() && y < structureInfo.getSizeY() && z < structureInfo.getSizeZ() && x >= 0 && y >= 0 && z >= 0;
    }

    public void save(@NotNull File f) throws IOException {
        toFile(this, f);
    }

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

    enum Axis {
        X, Y, Z
    }
}
