/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.flora.flora.gen;

import net.jafama.FastMath;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.state.properties.base.Properties;
import com.dfsek.terra.api.block.state.properties.enums.Direction;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.buffer.Buffer;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;


public class TerraFlora implements Structure {
    private final List<ProbabilityCollection<BlockState>> layers;
    private final boolean physics;
    private final boolean ceiling;
    
    private final MaterialSet testRotation;
    
    private final NoiseSampler distribution;
    
    private final String id;
    
    public TerraFlora(List<BlockLayer> layers, boolean physics, boolean ceiling,
                      MaterialSet testRotation,
                      NoiseSampler distribution, String id) {
        this.physics = physics;
        this.testRotation = testRotation;
        this.ceiling = ceiling;
        this.distribution = distribution;
        this.id = id;
        
        this.layers = new ArrayList<>();
        layers.forEach(layer -> {
            for(int i = 0; i < layer.getLayers(); i++) {
                this.layers.add(layer.getBlocks());
            }
        });
    }
    
    private void test(EnumSet<Direction> faces, Direction f, Vector3 b, World world) {
        if(testRotation.contains(
                world.getBlockData(b.getBlockX() + f.getModX(), b.getBlockY() + f.getModY(), b.getBlockZ() + f.getModZ()).getBlockType()))
            faces.add(f);
    }
    
    private ProbabilityCollection<BlockState> getStateCollection(int layer) {
        return layers.get(FastMath.max(FastMath.min(layer, layers.size() - 1), 0));
    }
    
    private EnumSet<Direction> getFaces(Vector3 b, World world) {
        EnumSet<Direction> faces = EnumSet.noneOf(Direction.class);
        test(faces, Direction.NORTH, b, world);
        test(faces, Direction.SOUTH, b, world);
        test(faces, Direction.EAST, b, world);
        test(faces, Direction.WEST, b, world);
        return faces;
    }
    
    @Override
    public String getID() {
        return id;
    }
    
    @Override
    public boolean generate(Vector3 location, World world, Chunk chunk, Random random, Rotation rotation) {
        return generate(location, world, random, rotation);
    }
    
    @Override
    public boolean generate(Buffer buffer, World world, Random random, Rotation rotation, int recursions) {
        return generate(buffer.getOrigin(), world, random, rotation);
    }
    
    @Override
    public boolean generate(Vector3 location, World world, Random random, Rotation rotation) {
        boolean doRotation = testRotation.size() > 0;
        int size = layers.size();
        int c = ceiling ? -1 : 1;
        
        EnumSet<Direction> faces = doRotation ? getFaces(location.clone().add(0, c, 0), world) : EnumSet.noneOf(Direction.class);
        if(doRotation && faces.size() == 0) return false; // Don't plant if no faces are valid.
        
        for(int i = 0; FastMath.abs(i) < size; i += c) { // Down if ceiling, up if floor
            int lvl = (FastMath.abs(i));
            BlockState data = getStateCollection((ceiling ? lvl : size - lvl - 1)).get(distribution, location.getX(), location.getY(),
                                                                                       location.getZ(), world.getSeed()).clone();
            if(doRotation) {
                Direction oneFace = new ArrayList<>(faces).get(
                        new Random(location.getBlockX() ^ location.getBlockZ()).nextInt(faces.size())); // Get random face.
                
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
}
