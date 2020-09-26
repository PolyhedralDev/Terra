package com.dfsek.terra.structure;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

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

public class GaeaStructure implements Serializable {
    public static final long serialVersionUID = -6664585217063842035L;
    private final StructureContainedBlock[][][] structure;
    private final String id;
    private final UUID uuid;

    public static GaeaStructure load(File f) throws IOException {
        try {
            return fromFile(f);
        } catch(ClassNotFoundException e) {
            throw new IllegalArgumentException("Provided file does not contain a GaeaStructure.");
        }
    }

    public GaeaStructure(Location l1, Location l2, String id) {
        this.id = id;
        this.uuid = UUID.randomUUID();
        if(l1.getX() > l2.getX() || l1.getY() > l2.getY() || l1.getZ() > l2.getZ()) throw new IllegalArgumentException("Invalid locations provided!");
        structure = new StructureContainedBlock[l2.getBlockX()-l1.getBlockX()+1][l2.getBlockY()-l1.getBlockY()+1][l2.getBlockZ()-l1.getBlockZ()+1];
        for(int x = 0; x <= l2.getBlockX()-l1.getBlockX(); x++) {
            for(int y = 0; y <= l2.getBlockY()-l1.getBlockY(); y++) {
                for(int z = 0; z <= l2.getBlockZ()-l1.getBlockZ(); z++) {
                    structure[x][y][z] = new StructureContainedBlock(x, y, z, Objects.requireNonNull(l1.getWorld()).getBlockAt(l1.clone().add(x, y, z)));
                }
            }
        }
    }

    public void paste(Location origin) {
        for(StructureContainedBlock[][] bList2 : structure) {
            for(StructureContainedBlock[] bList1 : bList2) {
                for(StructureContainedBlock block : bList1) {
                    BlockData data = block.getBlockData();
                    Block worldBlock = origin.clone().add(block.getX(), block.getY(), block.getZ()).getBlock();
                    if(!data.getMaterial().equals(Material.STRUCTURE_VOID)) worldBlock.setBlockData(data);
                }
            }
        }
    }

    public void paste(Location origin, Chunk c) {
        for(StructureContainedBlock[][] bList2 : structure) {
            for(StructureContainedBlock[] bList1 : bList2) {
                for(StructureContainedBlock block : bList1) {
                    Location newLoc = origin.clone().add(block.getX(), block.getY(), block.getZ());
                    BlockData data = block.getBlockData();
                    if(newLoc.getChunk().equals(c) && !data.getMaterial().equals(Material.STRUCTURE_VOID)) newLoc.getBlock().setBlockData(block.getBlockData());
                }
            }
        }
    }

    public void save(File f) throws IOException {
        toFile(this, f);
    }

    private static GaeaStructure fromFile(File f) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        Object o = ois.readObject();
        ois.close();
        return (GaeaStructure) o;
    }

    public static GaeaStructure fromStream(InputStream f) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(f);
        Object o = ois.readObject();
        ois.close();
        return (GaeaStructure) o;
    }

    private static void toFile(Serializable o, File f) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(o);
        oos.close();
    }

    public String getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }
}
