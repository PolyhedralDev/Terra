package com.dfsek.terra.bukkit.nms.v1_19_R1.util;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Random;

import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.bukkit.nms.v1_19_R1.AwfulBukkitHacks;


public class FertilizableUtil {
    public static boolean grow(ServerWorld world, Random random, Vector3Int pos, ResourceLocation block) {
        Map<ResourceLocation, ProbabilityCollection<ConfiguredStructure>> fertilizables =  AwfulBukkitHacks.TERRA_BIOME_FERTILIZABLE_MAP.get(world.getBiomeProvider().getBiome(pos, world.getSeed()));
        if (fertilizables != null) {
            ProbabilityCollection<ConfiguredStructure> probabilityCollection = fertilizables.get(block);
            if (probabilityCollection != null) {
                ConfiguredStructure structure = probabilityCollection.get(random);
                structure.getStructure().get(random).generate(pos, world, random, Rotation.NONE);
                return true;
            }
        }
        return false;
    }
}
