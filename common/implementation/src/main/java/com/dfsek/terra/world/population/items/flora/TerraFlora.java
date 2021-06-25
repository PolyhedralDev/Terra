package com.dfsek.terra.world.population.items.flora;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockFace;
import com.dfsek.terra.api.block.data.Directional;
import com.dfsek.terra.api.block.data.MultipleFacing;
import com.dfsek.terra.api.block.data.Rotatable;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.Flora;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.vector.Vector3Impl;
import net.jafama.FastMath;

import java.util.ArrayList;
import java.util.List;

public class TerraFlora implements Flora {
    private final Palette floraPalette;
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

    public TerraFlora(Palette floraPalette, boolean physics, boolean ceiling, MaterialSet irrigable, MaterialSet spawnable, MaterialSet replaceable, MaterialSet testRotation, int maxPlacements, Search search, boolean spawnBlacklist, int irrigableOffset, TerraPlugin main) {
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
    public List<Vector3> getValidSpawnsAt(Chunk chunk, int x, int z, Range range) {
        int size = floraPalette.getSize();
        Vector3 current = new Vector3Impl(x, search.equals(Search.UP) ? range.getMin() : range.getMax(), z);
        List<Vector3> blocks = new ArrayList<>();
        for(int y : range) {
            if(y > 255 || y < 0) continue;
            current = current.add(0, search.equals(Search.UP) ? 1 : -1, 0);
            if((spawnBlacklist != spawnable.contains(chunk.getBlock(current.getBlockX(), current.getBlockY(), current.getBlockZ()).getBlockType())) && isIrrigated(current.add(0, irrigableOffset, 0), chunk) && valid(size, current.clone(), chunk)) {
                blocks.add(current.clone());
                if(maxPlacements > 0 && blocks.size() >= maxPlacements) break;
            }
        }
        return blocks;
    }

    private boolean valid(int size, Vector3 block, Chunk chunk) {
        for(int i = 0; i < size; i++) { // Down if ceiling, up if floor
            if(block.getY() + 1 > 255 || block.getY() < 0) return false;
            block.add(0, ceiling ? -1 : 1, 0);
            if(!replaceable.contains(chunk.getBlock(block.getBlockX(), block.getBlockY(), block.getBlockZ()).getBlockType())) return false;
        }
        return true;
    }

    private boolean isIrrigated(Vector3 b, Chunk chunk) {
        if(irrigable == null) return true;
        return irrigable.contains(chunk.getBlock(b.getBlockX()+1, b.getBlockY(), b.getBlockZ()).getBlockType())
                || irrigable.contains(chunk.getBlock(b.getBlockX()-1, b.getBlockY(), b.getBlockZ()).getBlockType())
                || irrigable.contains(chunk.getBlock(b.getBlockX(), b.getBlockY(), b.getBlockZ()+1).getBlockType())
                || irrigable.contains(chunk.getBlock(b.getBlockX(), b.getBlockY(), b.getBlockZ()-1).getBlockType());
    }


    @Override
    public boolean plant(Location location) {
        boolean doRotation = testRotation.size() > 0;
        int size = floraPalette.getSize();
        int c = ceiling ? -1 : 1;

        List<BlockFace> faces = doRotation ? getFaces(location.clone().add(0, c, 0).toVector(), location.getWorld()) : new GlueList<>();
        if(doRotation && faces.size() == 0) return false; // Don't plant if no faces are valid.

        for(int i = 0; FastMath.abs(i) < size; i += c) { // Down if ceiling, up if floor
            int lvl = (FastMath.abs(i));
            BlockData data = floraPalette.get((ceiling ? lvl : size - lvl - 1), location.getX(), location.getY(), location.getZ()).clone();
            if(doRotation) {
                BlockFace oneFace = faces.get(new FastRandom(location.getBlockX() ^ location.getBlockZ()).nextInt(faces.size())); // Get random face.
                if(data instanceof Directional) {
                    ((Directional) data).setFacing(oneFace.getOppositeFace());
                } else if(data instanceof MultipleFacing) {
                    MultipleFacing o = (MultipleFacing) data;
                    for(BlockFace face : o.getFaces()) o.setFace(face, false);
                    for(BlockFace face : faces) o.setFace(face, true);
                } else if(data instanceof Rotatable) {
                    ((Rotatable) data).setRotation(oneFace);
                }
            }
            location.getWorld().setBlockData(location.toVector().add(0, i + c, 0), data, physics);
        }
        return true;
    }

    private List<BlockFace> getFaces(Vector3 b, World world) {
        List<BlockFace> faces = new GlueList<>();
        test(faces, BlockFace.NORTH, b, world);
        test(faces, BlockFace.SOUTH, b, world);
        test(faces, BlockFace.EAST, b, world);
        test(faces, BlockFace.WEST, b, world);
        return faces;
    }

    private void test(List<BlockFace> faces, BlockFace f, Vector3 b, World world) {
        if(testRotation.contains(world.getBlockData(b.getBlockX()+f.getModX(), b.getBlockY()+f.getModY(), b.getBlockZ()+f.getModZ()).getBlockType())) faces.add(f);
    }

    public enum Search {
        UP,
        DOWN
    }
}
