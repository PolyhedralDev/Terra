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
import com.dfsek.terra.generation.items.flora.FloraLayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Populates Flora and Trees
 */
public class FloraPopulator implements TerraBlockPopulator {
    private final TerraPlugin main;

    public FloraPopulator(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        try(ProfileFuture ignored = tw.getProfiler().measure("FloraTime")) {
            if(!tw.isSafe()) return;
            TerraBiomeGrid grid = tw.getGrid();
            Map<Vector2, List<FloraLayer>> layers = new HashMap<>();
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    UserDefinedBiome biome = (UserDefinedBiome) grid.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z, GenerationPhase.POPULATE);
                    Vector2 l = new Vector2(x, z);
                    layers.put(l, biome.getConfig().getFlora());
                }
            }
            int iter = 0;
            boolean finished = false;
            while(!finished) {
                finished = true;
                for(Map.Entry<Vector2, List<FloraLayer>> entry : layers.entrySet()) {
                    if(entry.getValue().size() <= iter) continue;
                    finished = false;
                    FloraLayer layer = entry.getValue().get(iter);
                    if(layer.getDensity() >= random.nextDouble() * 100D) layer.place(chunk, entry.getKey(), random);
                }
                iter++;
            }
        }
    }
}
