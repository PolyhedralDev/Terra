package com.dfsek.terra.mod.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Random;

import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;
import com.dfsek.terra.mod.config.FertilizableConfig;


public class FertilizableUtil {
    
    private static final Random mojankRandom = new Random();
    
    public static Boolean grow(ServerWorld world, BlockPos pos, BlockState state, Identifier cooldownId) {
        return grow(world, mojankRandom, pos, state, cooldownId);
    }
    
    public static Boolean grow(ServerWorld world, Random random, BlockPos pos, BlockState state, Identifier cooldownId) {
        Map<Identifier, FertilizableConfig> map = BiomeUtil.TERRA_BIOME_FERTILIZABLE_MAP.get(world.getBiome(pos));
        if(map != null) {
            Block block = state.getBlock();
            FertilizableConfig config = map.get(Registry.BLOCK.getId(block));
            if(config != null) {
                ConfiguredStructure canGrow = config.getCanGrow();
                if(canGrow != null) {
                    if(!canGrow.getStructure().get(random).generate(
                            Vector3Int.of(pos.getX(), pos.getY(), pos.getZ()), (WritableWorld) world, random, Rotation.NONE)) {
                        return false;
                    }
                }
                Double cooldown = config.getCooldowns().get(cooldownId);
                if(cooldown != null) {
                    if(random.nextFloat() > cooldown) {
                        return true;
                    }
                }
                config.getStructures().get(random).getStructure().get(random).generate(
                        Vector3Int.of(pos.getX(), pos.getY(), pos.getZ()), (WritableWorld) world, random, Rotation.NONE);
                return true;
            }
        }
        return null;
    }
}
