package com.dfsek.terra.world.population;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.vector.Vector2Impl;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.util.world.PopulationUtil;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.TerraBlockPopulator;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.population.items.tree.TreeLayer;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class TreePopulator implements TerraBlockPopulator {
    private final TerraPlugin main;

    public TreePopulator(TerraPlugin main) {
        this.main = main;
    }

    private static int offset(Random r, int i) {
        return FastMath.min(FastMath.max(i + r.nextInt(3) - 1, 0), 15);
    }

    @Override
    @SuppressWarnings("try")
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        try(ProfileFrame ignore = main.getProfiler().profile("tree")) {
            if(tw.getConfig().getTemplate().disableTrees()) return;

            if(!tw.isSafe()) return;
            BiomeProvider provider = tw.getBiomeProvider();
            Random random = PopulationUtil.getRandom(chunk);
            for(int x = 0; x < 16; x += 2) {
                for(int z = 0; z < 16; z += 2) {
                    UserDefinedBiome biome = (UserDefinedBiome) provider.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z);
                    for(TreeLayer layer : biome.getConfig().getTrees()) {
                        if(layer.getDensity() >= random.nextDouble() * 100) {
                            layer.place(chunk, new Vector2Impl(offset(random, x), offset(random, z)));
                        }
                    }
                }
            }
        }
    }
}
