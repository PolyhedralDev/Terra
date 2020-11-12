package com.dfsek.terra.generation.entities;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.world.palette.Palette;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Flora implements GenerationEntity, org.polydev.gaea.world.Flora {
    private final Palette<BlockData> floraPalette;
    private final String id;
    private final boolean physics;
    private final boolean ceiling;

    private final Set<Material> irrigable;

    private final Set<Material> spawnable;
    private final Set<Material> replaceable;

    public Flora(Palette<BlockData> floraPalette, String id, boolean physics, boolean ceiling, Set<Material> irrigable, Set<Material> spawnable, Set<Material> replaceable) {
        this.floraPalette = floraPalette;
        this.id = id;
        this.physics = physics;
        this.ceiling = ceiling;
        this.irrigable = irrigable;
        this.spawnable = spawnable;
        this.replaceable = replaceable;
    }

    @Override
    public void generate(Location location, Random random, JavaPlugin plugin) {

    }

    @Override
    public boolean isValidLocation(Location location, JavaPlugin plugin) {
        Block check = location.getBlock();
        if(ceiling) {
            Block other = check.getRelative(BlockFace.DOWN);
            return spawnable.contains(check.getType()) && replaceable.contains(other.getType());
        } else {
            Block other = check.getRelative(BlockFace.UP);
            return spawnable.contains(check.getType()) && replaceable.contains(other.getType()) && isIrrigated(check);
        }
    }

    @Override
    public List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range range) {
        List<Block> blocks = new ArrayList<>();
        if(ceiling) for(int y : range) {
            if(y > 255 || y < 1) continue;
            Block check = chunk.getBlock(x, y, z);
            if(isValidLocation(check.getLocation(), null)) {
                blocks.add(check);
            }
        }
        else for(int y : range) {
            if(y > 254 || y < 0) continue;
            Block check = chunk.getBlock(x, y, z);
            if(isValidLocation(check.getLocation(), null)) {
                blocks.add(check);
            }
        }
        return blocks;
    }

    private boolean isIrrigated(Block b) {
        if(irrigable == null) return true;
        return irrigable.contains(b.getRelative(BlockFace.NORTH).getType())
                || irrigable.contains(b.getRelative(BlockFace.SOUTH).getType())
                || irrigable.contains(b.getRelative(BlockFace.EAST).getType())
                || irrigable.contains(b.getRelative(BlockFace.WEST).getType());
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean plant(Location location) {
        int size = floraPalette.getSize();
        int c = ceiling ? -1 : 1;
        for(int i = 0; Math.abs(i) < size; i += c) { // Down if ceiling, up if floor
            if(i + 1 > 255) return false;
            if(!replaceable.contains(location.clone().add(0, i + c, 0).getBlock().getType())) return false;
        }
        for(int i = 0; Math.abs(i) < size; i += c) { // Down if ceiling, up if floor
            int lvl = (Math.abs(i));
            location.clone().add(0, i + c, 0).getBlock().setBlockData(floraPalette.get((ceiling ? lvl : size - lvl - 1), location.getBlockX(), location.getBlockZ()), physics);
        }
        return true;
    }
}
