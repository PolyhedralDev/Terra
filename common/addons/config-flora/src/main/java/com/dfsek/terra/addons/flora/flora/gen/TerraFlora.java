package com.dfsek.terra.addons.flora.flora.gen;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.block.state.properties.enums.Direction;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.Flora;
import com.dfsek.terra.api.world.World;
import net.jafama.FastMath;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class TerraFlora implements Flora {
    private final List<ProbabilityCollection<BlockState>> layers;
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

    private final NoiseSampler distribution;

    public TerraFlora(List<BlockLayer> layers, boolean physics, boolean ceiling, MaterialSet irrigable, MaterialSet spawnable, MaterialSet replaceable, MaterialSet testRotation, int maxPlacements, Search search, boolean spawnBlacklist, int irrigableOffset, TerraPlugin main, NoiseSampler distribution) {
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
        this.distribution = distribution;

        this.layers = new ArrayList<>();
        layers.forEach(layer -> {
            for(int i = 0; i < layer.getLayers(); i++) {
                this.layers.add(layer.getBlocks());
            }
        });
    }

    @Override
    public List<Vector3> getValidSpawnsAt(Chunk chunk, int x, int z, Range range) {
        int size = layers.size();
        Vector3 current = new Vector3(x, search.equals(Search.UP) ? range.getMin() : range.getMax(), z);
        List<Vector3> blocks = new ArrayList<>();
        int cx = chunk.getX() << 4;
        int cz = chunk.getZ() << 4;
        for(int y : range) {
            if(y > 255 || y < 0) continue;
            current = current.add(0, search.equals(Search.UP) ? 1 : -1, 0);
            if((spawnBlacklist != spawnable.contains(chunk.getBlock(current.getBlockX(), current.getBlockY(), current.getBlockZ()).getBlockType())) && isIrrigated(current.clone().add(cx, irrigableOffset, cz), chunk.getWorld()) && valid(size, current.clone().add(cx, 0, cz), chunk.getWorld())) {
                blocks.add(current.clone());
                if(maxPlacements > 0 && blocks.size() >= maxPlacements) break;
            }
        }
        return blocks;
    }

    private boolean valid(int size, Vector3 block, World world) {
        for(int i = 0; i < size; i++) { // Down if ceiling, up if floor
            if(block.getY() + 1 > 255 || block.getY() < 0) return false;
            block.add(0, ceiling ? -1 : 1, 0);
            if(!replaceable.contains(world.getBlockData(block).getBlockType())) return false;
        }
        return true;
    }

    private boolean isIrrigated(Vector3 b, World world) {
        if(irrigable == null) return true;
        return irrigable.contains(world.getBlockData(b.getBlockX() + 1, b.getBlockY(), b.getBlockZ()).getBlockType())
                || irrigable.contains(world.getBlockData(b.getBlockX() - 1, b.getBlockY(), b.getBlockZ()).getBlockType())
                || irrigable.contains(world.getBlockData(b.getBlockX(), b.getBlockY(), b.getBlockZ() + 1).getBlockType())
                || irrigable.contains(world.getBlockData(b.getBlockX(), b.getBlockY(), b.getBlockZ() - 1).getBlockType());
    }

    private ProbabilityCollection<BlockState> getStateCollection(int layer) {
        return layers.get(FastMath.max(FastMath.min(layer, layers.size() - 1), 0));
    }

    @Override
    public boolean plant(Vector3 location, World world) {
        boolean doRotation = testRotation.size() > 0;
        int size = layers.size();
        int c = ceiling ? -1 : 1;

        EnumSet<Direction> faces = doRotation ? getFaces(location.clone().add(0, c, 0), world) : EnumSet.noneOf(Direction.class);
        if(doRotation && faces.size() == 0) return false; // Don't plant if no faces are valid.

        for(int i = 0; FastMath.abs(i) < size; i += c) { // Down if ceiling, up if floor
            int lvl = (FastMath.abs(i));
            BlockState data = getStateCollection((ceiling ? lvl : size - lvl - 1)).get(distribution, location.getX(), location.getY(), location.getZ(), world.getSeed()).clone();
            if(doRotation) {
                Direction oneFace = new ArrayList<>(faces).get(new Random(location.getBlockX() ^ location.getBlockZ()).nextInt(faces.size())); // Get random face.

                data.setIfPresent(Properties.DIRECTION, oneFace.opposite())
                        .setIfPresent(Properties.NORTH, faces.contains(Direction.NORTH))
                        .setIfPresent(Properties.SOUTH, faces.contains(Direction.SOUTH))
                        .setIfPresent(Properties.EAST, faces.contains(Direction.EAST))
                        .setIfPresent(Properties.WEST, faces.contains(Direction.WEST));
            }
            world.setBlockData(location.clone().add(0, i + c, 0), data, physics);
        }
        return true;
    }

    private EnumSet<Direction> getFaces(Vector3 b, World world) {
        EnumSet<Direction> faces = EnumSet.noneOf(Direction.class);
        test(faces, Direction.NORTH, b, world);
        test(faces, Direction.SOUTH, b, world);
        test(faces, Direction.EAST, b, world);
        test(faces, Direction.WEST, b, world);
        return faces;
    }

    private void test(EnumSet<Direction> faces, Direction f, Vector3 b, World world) {
        if(testRotation.contains(world.getBlockData(b.getBlockX() + f.getModX(), b.getBlockY() + f.getModY(), b.getBlockZ() + f.getModZ()).getBlockType()))
            faces.add(f);
    }

    public enum Search {
        UP,
        DOWN
    }
}
