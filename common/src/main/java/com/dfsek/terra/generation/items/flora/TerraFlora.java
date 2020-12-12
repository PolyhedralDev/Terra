package com.dfsek.terra.generation.items.flora;

import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.gaea.util.FastRandom;
import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.api.gaea.world.Flora;
import com.dfsek.terra.api.gaea.world.palette.Palette;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.block.data.Directional;
import com.dfsek.terra.api.generic.world.block.data.MultipleFacing;
import com.dfsek.terra.api.generic.world.block.data.Rotatable;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.util.MaterialSet;
import net.jafama.FastMath;

import java.util.ArrayList;
import java.util.List;

public class TerraFlora implements Flora {
    private final Palette<BlockData> floraPalette;
    private final boolean physics;
    private final boolean ceiling;

    private final MaterialSet irrigable;

    private final MaterialSet spawnable;
    private final MaterialSet replaceable;

    private final MaterialSet testRotation;

    private final int maxPlacements;

    private final Search search;

    private final boolean spawnBlacklist;

    private final int irrigableOffset;

    private final TerraPlugin main;

    public TerraFlora(Palette<BlockData> floraPalette, boolean physics, boolean ceiling, MaterialSet irrigable, MaterialSet spawnable, MaterialSet replaceable, MaterialSet testRotation, int maxPlacements, Search search, boolean spawnBlacklist, int irrigableOffset, TerraPlugin main) {
        this.floraPalette = floraPalette;
        this.physics = physics;
        this.testRotation = testRotation;
        this.spawnBlacklist = spawnBlacklist;
        this.ceiling = ceiling;
        this.irrigable = irrigable;
        this.spawnable = spawnable;
        this.replaceable = replaceable;
        this.maxPlacements = maxPlacements;
        this.search = search;
        this.irrigableOffset = irrigableOffset;
        this.main = main;
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
        WorldHandle handle = main.getWorldHandle();

        boolean doRotation = testRotation.size() > 0;
        int size = floraPalette.getSize();
        int c = ceiling ? -1 : 1;

        List<BlockFace> faces = doRotation ? getFaces(location.clone().add(0, c, 0).getBlock()) : new GlueList<>();
        if(doRotation && faces.size() == 0) return false; // Don't plant if no faces are valid.
        BlockFace oneFace = doRotation ? faces.get(new FastRandom(location.getBlockX() ^ location.getBlockZ()).nextInt(faces.size())) : null; // Get random face.

        for(int i = 0; FastMath.abs(i) < size; i += c) { // Down if ceiling, up if floor
            int lvl = (FastMath.abs(i));
            BlockData data = floraPalette.get((ceiling ? lvl : size - lvl - 1), location.getBlockX(), location.getBlockZ()).clone();
            if(doRotation) {
                if(data instanceof Directional) {
                    ((Directional) data).setFacing(oneFace);
                } else if(data instanceof MultipleFacing) {
                    MultipleFacing o = (MultipleFacing) data;
                    for(BlockFace face : o.getFaces()) o.setFace(face, false);
                    for(BlockFace face : faces) o.setFace(face, true);
                } else if(data instanceof Rotatable) {
                    ((Rotatable) data).setRotation(oneFace);
                }
            }
            handle.setBlockData(location.clone().add(0, i + c, 0).getBlock(), data, physics);
        }
        return true;
    }

    private List<BlockFace> getFaces(Block b) {
        List<BlockFace> faces = new GlueList<>();
        test(faces, BlockFace.NORTH, b);
        test(faces, BlockFace.SOUTH, b);
        test(faces, BlockFace.EAST, b);
        test(faces, BlockFace.WEST, b);
        return faces;
    }

    private void test(List<BlockFace> faces, BlockFace f, Block b) {
        if(testRotation.contains(b.getRelative(f).getType())) faces.add(f);
    }

    public enum Search {
        UP,
        DOWN
    }
}
