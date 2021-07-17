package com.dfsek.terra.addons.tree;

import com.dfsek.terra.addons.tree.tree.TreeLayer;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.generator.TerraGenerationStage;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Random;


public class TreePopulator implements TerraGenerationStage {
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
            if(tw.getConfig().disableTrees()) return;

            BiomeProvider provider = tw.getBiomeProvider();
            Random random = PopulationUtil.getRandom(chunk);
            for(int x = 0; x < 16; x += 2) {
                for(int z = 0; z < 16; z += 2) {
                    for(TreeLayer layer : provider.getBiome((chunk.getX() << 4) + x, (chunk.getZ() << 4) + z).getContext().get(BiomeTrees.class).getTrees()) {
                        if(layer.getDensity() >= random.nextDouble() * 100) {
                            layer.place(chunk, new Vector2(offset(random, x), offset(random, z)));
                        }
                    }
                }
            }
        }
    }
}
