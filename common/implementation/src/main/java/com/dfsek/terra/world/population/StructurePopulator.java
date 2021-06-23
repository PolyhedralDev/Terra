package com.dfsek.terra.world.population;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.vector.Location;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.dfsek.terra.api.world.generator.TerraBlockPopulator;
import com.dfsek.terra.config.pack.WorldConfigImpl;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.world.population.items.TerraStructure;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructurePopulator implements TerraBlockPopulator, Chunkified {
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
            if(!tw.isSafe()) return;
            BiomeProvider provider = tw.getBiomeProvider();
            WorldConfig config = tw.getConfig();
            for(TerraStructure conf : config.getRegistry(TerraStructure.class).entries()) {
                Location spawn = conf.getSpawn().getNearestSpawn(cx + 8, cz + 8, world.getSeed()).toLocation(world);

                if(!((UserDefinedBiome) provider.getBiome(spawn)).getConfig().getStructures().contains(conf))
                    continue;
                Random random = new FastRandom(PopulationUtil.getCarverChunkSeed(FastMath.floorDiv(spawn.getBlockX(), 16), FastMath.floorDiv(spawn.getBlockZ(), 16), world.getSeed()));
                conf.getStructure().get(random).generate(spawn.setY(conf.getSpawnStart().get(random)), chunk, random, Rotation.fromDegrees(90 * random.nextInt(4)));
            }
        }
    }
}
