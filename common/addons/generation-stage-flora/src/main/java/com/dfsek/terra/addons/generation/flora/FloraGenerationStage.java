package com.dfsek.terra.addons.generation.flora;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.TerraGenerationStage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Populates Flora
 */
public class FloraGenerationStage implements TerraGenerationStage {
    private final TerraPlugin main;

    public FloraGenerationStage(TerraPlugin main) {
        this.main = main;
    }

    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        try(ProfileFrame ignore = main.getProfiler().profile("flora")) {
            if(world.getConfig().disableFlora()) return;

            long seed = world.getSeed();
            BiomeProvider provider = world.getBiomeProvider();
            Map<Vector2, List<FloraLayer>> layers = new HashMap<>();
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    Vector2 l = new Vector2(x, z);
                    layers.put(l, provider.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z, seed).getContext().get(BiomeFlora.class).getLayers());
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
