package com.dfsek.terra.population;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import com.dfsek.terra.api.gaea.profiler.ProfileFuture;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.generator.TerraBlockPopulator;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.vector.Vector2;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.generation.items.tree.TreeLayer;
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
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        try(ProfileFuture ignored = tw.getProfiler().measure("TreeTime")) {
            if(!tw.isSafe()) return;
            TerraBiomeGrid grid = tw.getGrid();
            for(int x = 0; x < 16; x += 2) {
                for(int z = 0; z < 16; z += 2) {
                    UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z, GenerationPhase.POPULATE);
                    for(TreeLayer layer : biome.getConfig().getTrees()) {
                        if(layer.getDensity() >= random.nextDouble() * 100)
                            layer.place(chunk, new Vector2(offset(random, x), offset(random, z)), random);
                    }
                }
            }
        }
    }
}
