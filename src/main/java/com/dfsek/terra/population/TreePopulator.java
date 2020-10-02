package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.generation.UserDefinedDecorator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.util.WorldUtil;

import java.util.Random;

public class TreePopulator extends GaeaBlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = TerraProfiler.fromWorld(world).measure("TreeGenTime")) {
            TerraWorld tw = TerraWorld.getWorld(world);
            TerraBiomeGrid grid = tw.getGrid();;
            int x = random.nextInt(16); // Decrease chances of chunk-crossing trees
            int z = random.nextInt(16);
            Location origin = chunk.getBlock(x, 0, z).getLocation();
            Biome b = grid.getBiome(origin, GenerationPhase.POPULATE);
            if(((UserDefinedDecorator) b.getDecorator()).getTreeChance() < random.nextInt(100)) return;
            int max = 50;
            int att = 0;
            for(int i = 0; i < b.getDecorator().getTreeDensity() && att < max; ) {
                att++;
                int y = 255;
                while(y > 1) {
                    if(chunk.getBlock(x, y, z).getType().isAir() && chunk.getBlock(x, y-1, z).getType().isSolid()) break;
                    y--;
                }
                if(y == 0) continue;
                origin = chunk.getBlock(x, y, z).getLocation().add(0, 1, 0);
                b = grid.getBiome(origin, GenerationPhase.POPULATE);
                try {
                    if(b.getDecorator().getTrees().get(random).plant(origin, random, false, Terra.getInstance())) i++;
                } catch(NullPointerException ignore) {}
                x = random.nextInt(16);
                z = random.nextInt(16);
            }
        }
    }
}
