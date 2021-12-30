/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.sponge;

import java.util.Random;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector2Int;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;


public class SpongeStructure implements Structure, Keyed<SpongeStructure> {
    
    private final BlockState[][][] blocks;
    
    private final RegistryKey id;
    
    public SpongeStructure(BlockState[][][] blocks, RegistryKey id) {
        this.blocks = blocks;
        this.id = id;
    }
    
    @Override
    public boolean generate(Vector3Int location, WritableWorld world, Random random, Rotation rotation) {
        int bX = location.getX();
        int bY = location.getY();
        int bZ = location.getZ();
        for(int x = 0; x < blocks.length; x++) {
            for(int z = 0; z < blocks[x].length; z++) {
                Vector2Int r = Vector2Int.of(x, z).rotate(rotation);
                int rX = r.getX();
                int rZ = r.getZ();
                for(int y = 0; y < blocks[x][z].length; y++) {
                    BlockState state = blocks[x][z][y];
                    if(state == null) continue;
                    world.setBlockState(bX + rX, bY + y, bZ + rZ, state);
                }
            }
        }
        return true;
    }
    
    @Override
    public RegistryKey getRegistryKey() {
        return id;
    }
}
