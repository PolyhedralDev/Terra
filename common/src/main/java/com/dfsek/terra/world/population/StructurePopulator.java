package com.dfsek.terra.world.population;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.generation.TerraBlockPopulator;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.profiler.ProfileFuture;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.population.items.TerraStructure;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructurePopulator implements TerraBlockPopulator {
    private final TerraPlugin main;

    public StructurePopulator(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        try(ProfileFuture ignored = tw.getProfiler().measure("StructureTime")) {
            int cx = (chunk.getX() << 4);
            int cz = (chunk.getZ() << 4);
            if(!tw.isSafe()) return;
            BiomeProvider provider = tw.getBiomeProvider();
            ConfigPack config = tw.getConfig();
            for(TerraStructure conf : config.getStructures()) {
                Location spawn = conf.getSpawn().getNearestSpawn(cx + 8, cz + 8, world.getSeed()).toLocation(world);

                if(!((UserDefinedBiome) provider.getBiome(spawn)).getConfig().getStructures().contains(conf))
                    continue;
                Random random = new FastRandom(MathUtil.getCarverChunkSeed(FastMath.floorDiv(spawn.getBlockX(), 16), FastMath.floorDiv(spawn.getBlockZ(), 16), world.getSeed()));
                System.out.println("chunk: {" + chunk.getX() + ", " + chunk.getZ() + "}");
                conf.getStructure().get(random).execute(spawn.setY(conf.getSpawnStart().get(random)), chunk, random, Rotation.fromDegrees(90 * random.nextInt(4)));
            }
        }
    }
}
