package com.dfsek.terra.mod.util;

import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3Int;

import com.dfsek.terra.api.world.WritableWorld;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;

import java.util.Map;

import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class FertilizableUtil {
    public static boolean grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        Map<Identifier, ProbabilityCollection<ConfiguredStructure>> fertilizables =  BiomeUtil.TERRA_BIOME_FERTILIZABLE_MAP.get(world.getBiome(pos));
        if (fertilizables != null) {
            ProbabilityCollection<ConfiguredStructure> probabilityCollection = fertilizables.get(Registry.BLOCK.getId(state.getBlock()));
            if (probabilityCollection != null) {
                ConfiguredStructure structure = probabilityCollection.get((java.util.Random) random);
                structure.getStructure().get((java.util.Random) random).generate(Vector3Int.of(pos.getX(), pos.getY(), pos.getZ()), (WritableWorld) world, (java.util.Random) random, Rotation.NONE);
                return true;
            }
        }
        return false;
    }
}
