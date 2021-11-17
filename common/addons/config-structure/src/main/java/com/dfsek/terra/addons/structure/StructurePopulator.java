package com.dfsek.terra.addons.structure;

import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.dfsek.terra.api.world.generator.GenerationStage;


public class StructurePopulator implements GenerationStage, Chunkified {
    private final Platform platform;
    
    public StructurePopulator(Platform platform) {
        this.platform = platform;
    }
    
    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        try(ProfileFrame ignore = platform.getProfiler().profile("structure")) {
            if(world.getConfig().disableStructures()) return;
            
            int cx = (chunk.getX() << 4);
            int cz = (chunk.getZ() << 4);
            BiomeProvider provider = world.getBiomeProvider();
            WorldConfig config = world.getConfig();
            for(ConfiguredStructure conf : config.getRegistry(TerraStructure.class).entries()) {
                Vector3 spawn = conf.getSpawn().getNearestSpawn(cx + 8, cz + 8, world.getSeed());
                
                if(!provider.getBiome(spawn, world.getSeed()).getContext().get(BiomeStructures.class).getStructures().contains(conf)) {
                    continue;
                }
                Random random = new Random(PopulationUtil.getCarverChunkSeed(FastMath.floorDiv(spawn.getBlockX(), 16),
                                                                             FastMath.floorDiv(spawn.getBlockZ(), 16), world.getSeed()));
                conf.getStructure().get(random).generate(spawn.setY(conf.getSpawnStart().get(random)), world, chunk, random,
                                                         Rotation.fromDegrees(90 * random.nextInt(4)));
            }
        }
    }
}
