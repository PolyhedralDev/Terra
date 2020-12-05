package com.dfsek.terra.generation.items.flora;

import com.dfsek.terra.util.MaterialSet;
import net.jafama.FastMath;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.world.Flora;
import org.polydev.gaea.world.palette.Palette;

import java.util.ArrayList;
import java.util.List;

public class TerraFlora implements Flora {
    private final Palette<BlockData> floraPalette;
    private final boolean physics;
    private final boolean ceiling;

    private final MaterialSet irrigable;

    private final MaterialSet spawnable;
    private final MaterialSet replaceable;

    private final int maxPlacements;

    private final Search search;

    private final boolean spawnBlacklist;

    private final int irrigableOffset;

    public TerraFlora(Palette<BlockData> floraPalette, boolean physics, boolean ceiling, MaterialSet irrigable, MaterialSet spawnable, MaterialSet replaceable, int maxPlacements, Search search, boolean spawnBlacklist, int irrigableOffset) {
        this.floraPalette = floraPalette;
        this.physics = physics;
        this.spawnBlacklist = spawnBlacklist;
        this.ceiling = ceiling;
        this.irrigable = irrigable;
        this.spawnable = spawnable;
        this.replaceable = replaceable;
        this.maxPlacements = maxPlacements;
        this.search = search;
        this.irrigableOffset = irrigableOffset;
    }

    @Override
    public List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range range) {
        int size = floraPalette.getSize();
        Block current = chunk.getBlock(x, search.equals(Search.UP) ? range.getMin() : range.getMax(), z);
        List<Block> blocks = new ArrayList<>();
        for(int y : range) {
            if(y > 255 || y < 0) continue;
            current = current.getRelative(search.equals(Search.UP) ? BlockFace.UP : BlockFace.DOWN);
            if((spawnBlacklist != spawnable.contains(current.getType())) && isIrrigated(current.getRelative(BlockFace.UP, irrigableOffset)) && valid(size, current)) {
                blocks.add(current);
                if(maxPlacements > 0 && blocks.size() >= maxPlacements) break;
            }
        }
        return blocks;
    }

    private boolean valid(int size, Block block) {
        for(int i = 0; i < size; i++) { // Down if ceiling, up if floor
            if(block.getY() + 1 > 255 || block.getY() < 0) return false;
            block = block.getRelative(ceiling ? BlockFace.DOWN : BlockFace.UP);
            if(!replaceable.contains(block.getType())) return false;
        }
        return true;
    }

    private boolean isIrrigated(Block b) {
        if(irrigable == null) return true;
        return irrigable.contains(b.getRelative(BlockFace.NORTH).getType())
                || irrigable.contains(b.getRelative(BlockFace.SOUTH).getType())
                || irrigable.contains(b.getRelative(BlockFace.EAST).getType())
                || irrigable.contains(b.getRelative(BlockFace.WEST).getType());
    }


    @Override
    public boolean plant(Location location) {
        int size = floraPalette.getSize();
        int c = ceiling ? -1 : 1;
        for(int i = 0; FastMath.abs(i) < size; i += c) { // Down if ceiling, up if floor
            int lvl = (FastMath.abs(i));
            location.clone().add(0, i + c, 0).getBlock().setBlockData(floraPalette.get((ceiling ? lvl : size - lvl - 1), location.getBlockX(), location.getBlockZ()), physics);
        }
        return true;
    }

    public enum Search {
        UP,
        DOWN
    }
}
