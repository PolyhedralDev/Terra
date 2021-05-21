package com.dfsek.terra.world.population;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.generation.TerraBlockPopulator;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.profiler.ProfileFrame;
import com.dfsek.terra.world.TerraWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class OrePopulator implements TerraBlockPopulator {
    private final TerraPlugin main;

    public OrePopulator(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        try(ProfileFrame ignore = main.getProfiler().profile("ore")) {
            if(tw.getConfig().getTemplate().disableOres()) return;

            if(!tw.isSafe()) return;
            for(int cx = -1; cx <= 1; cx++) {
                for(int cz = -1; cz <= 1; cz++) {
                    Random random = new FastRandom(MathUtil.getCarverChunkSeed(chunk.getX() + cx, chunk.getZ() + cz, world.getSeed()));
                    int originX = ((chunk.getX() + cx) << 4);
                    int originZ = ((chunk.getZ() + cz) << 4);
                    TerraBiome b = tw.getBiomeProvider().getBiome(originX + 8, originZ + 8);
                    BiomeTemplate config = ((UserDefinedBiome) b).getConfig();
                    int finalCx = cx;
                    int finalCz = cz;
                    config.getOreHolder().forEach((id, orePair) -> {
                        try(ProfileFrame ignored = main.getProfiler().profile("ore:" + id)) {
                            int amount = orePair.getRight().getAmount().get(random);
                            for(int i = 0; i < amount; i++) {
                                Vector3 location = new Vector3(random.nextInt(16) + 16 * finalCx, orePair.getRight().getHeight().get(random), random.nextInt(16) + 16 * finalCz);
                                orePair.getLeft().generate(location, chunk, random);
                            }
                        }
                    });
                }
            }
        }
    }
}
