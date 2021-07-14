package com.dfsek.terra.addons.structure;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.structure.ConfiguredStructure;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.dfsek.terra.api.world.generator.TerraGenerationStage;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructurePopulator implements TerraGenerationStage, Chunkified {
    private final TerraPlugin main;

    public StructurePopulator(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        try(ProfileFrame ignore = main.getProfiler().profile("structure")) {
            if(tw.getConfig().disableStructures()) return;

            int cx = (chunk.getX() << 4);
            int cz = (chunk.getZ() << 4);
            BiomeProvider provider = tw.getBiomeProvider();
            WorldConfig config = tw.getConfig();
            for(ConfiguredStructure conf : config.getRegistry(TerraStructure.class).entries()) {
                Vector3 spawn = conf.getSpawn().getNearestSpawn(cx + 8, cz + 8, world.getSeed());

                //if(!((UserDefinedBiome) provider.getBiome(spawn)).getConfig().getStructures().contains(conf))
                //    continue;
                Random random = new Random(PopulationUtil.getCarverChunkSeed(FastMath.floorDiv(spawn.getBlockX(), 16), FastMath.floorDiv(spawn.getBlockZ(), 16), world.getSeed()));
                conf.getStructure().get(random).generate(spawn.setY(conf.getSpawnStart().get(random)), world, chunk, random, Rotation.fromDegrees(90 * random.nextInt(4)));
            }
        }
    }
}
