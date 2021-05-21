package com.dfsek.terra.world.population;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.world.PopulationUtil;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.generation.TerraBlockPopulator;
import com.dfsek.terra.profiler.ProfileFrame;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.population.items.flora.FloraLayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Populates Flora
 */
public class FloraPopulator implements TerraBlockPopulator {
    private final TerraPlugin main;

    public FloraPopulator(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        TerraWorld tw = main.getWorld(world);
        try(ProfileFrame ignore = main.getProfiler().profile("flora")) {
            if(tw.getConfig().getTemplate().disableFlora()) return;

            if(!tw.isSafe()) return;
            BiomeProvider provider = tw.getBiomeProvider();
            Map<Vector2, List<FloraLayer>> layers = new HashMap<>();
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    UserDefinedBiome biome = (UserDefinedBiome) provider.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z);
                    Vector2 l = new Vector2(x, z);
                    layers.put(l, biome.getConfig().getFlora());
                }
            }

            Random random = PopulationUtil.getRandom(chunk);

            int iter = 0;
            boolean finished = false;
            while(!finished) {
                finished = true;
                for(Map.Entry<Vector2, List<FloraLayer>> entry : layers.entrySet()) {
                    if(entry.getValue().size() <= iter) continue;
                    finished = false;
                    FloraLayer layer = entry.getValue().get(iter);
                    if(layer.getDensity() >= random.nextDouble() * 100D) layer.place(chunk, entry.getKey());
                }
                iter++;
            }
        }
    }
}
