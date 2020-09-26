package com.dfsek.terra.population;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.generation.UserDefinedDecorator;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.population.GaeaBlockPopulator;
import org.polydev.gaea.profiler.ProfileFuture;
import org.polydev.gaea.util.WorldUtil;

import java.util.Random;

public class TreePopulator extends GaeaBlockPopulator {
    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        ProfileFuture tree = TerraProfiler.fromWorld(world).measure("TreeGenTime");
        int x = random.nextInt(16); // Decrease chances of chunk-crossing trees
        int z = random.nextInt(16);
        Location origin = chunk.getBlock(x, 0, z).getLocation();
        Biome b = TerraBiomeGrid.fromWorld(world).getBiome(origin);
        if(((UserDefinedDecorator) b.getDecorator()).getTreeChance() < random.nextInt(100)) return;
        int numTrees = 0;
        for(int i = 0; i < 48; i++) {
            int y = WorldUtil.getHighestValidSpawnAt(chunk, x, z);
            if(y <= 0) continue;
            origin = chunk.getBlock(x, y, z).getLocation().add(0, 1, 0);
            b = TerraBiomeGrid.fromWorld(world).getBiome(origin);
            numTrees++;
            try {
                b.getDecorator().getTrees().get(random).plant(origin, random, false, Terra.getInstance());
            } catch(NullPointerException ignored) {}
            if(numTrees >= b.getDecorator().getTreeDensity()) break;
            x = random.nextInt(16); // Decrease chances of chunk-crossing trees
            z = random.nextInt(16);
        }
        if(tree!=null) tree.complete();
    }
}
